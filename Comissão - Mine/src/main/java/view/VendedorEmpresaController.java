package view;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import config.Config;
import static config.Config.df;
import static config.Config.nf;
import config.DAO;
import static config.DAO.cidadeRepository;
import static config.DAO.docFiscalRepository;
import static config.DAO.empresaRepository;
import static config.DAO.vendedorEmpresaRepository;
import static config.DAO.vendedorRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Cidade;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import model.VendedorEmpresa;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;

public class VendedorEmpresaController implements Initializable {

    private VendedorEmpresa controllerPai;

    public Empresa empresa;
    public Vendedor vendSelec;
    public VendedorEmpresa vendEmpSelec;

    private BufferedReader br = null;

    @FXML
    public TableView<VendedorEmpresa> tblView;

    @FXML
    public ComboBox<Vendedor> cmbVendedor;

    @FXML
    public TextField txtFldFiltro;

    @FXML
    public ToolBar tlBarStatus;

    @FXML
    public Label lbStatus, lblVerificando;

    @FXML
    public ProgressBar progressBar;

    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) throws ParseException {
        if (ev.getCode() == KeyCode.ENTER) {
            if (nf.parse(txtFldFiltro.getText()).doubleValue() > 0) {
                tblView.setItems(FXCollections.observableList(
                        vendedorEmpresaRepository.findByVendedorAndValorComissaoGreaterThan(vendSelec, nf.parse(txtFldFiltro.getText()).doubleValue())));
            } else if (nf.parse(txtFldFiltro.getText()).doubleValue() < 0) {
                tblView.setItems(FXCollections.observableList(
                        vendedorEmpresaRepository.findByVendedorAndValorComissaoGreaterThan(vendSelec, nf.parse(txtFldFiltro.getText()).doubleValue() * (-1))));
            } else if (nf.parse(txtFldFiltro.getText()).doubleValue() == 0 || txtFldFiltro.getText().isEmpty()) {
                tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findAll()));
            }
        }
    }

    @FXML
    public void acTotalizarVendedor() {
        vendSelec = cmbVendedor.getSelectionModel().getSelectedItem();

        vendedorEmpresaRepository.deleteByVendedor(vendSelec);

        for (DocumentoFiscal d : docFiscalRepository.findByVendedor(vendSelec)) {
            VendedorEmpresa vemp = vendedorEmpresaRepository.findByVendedorAndEmpresa(vendSelec, d.getEmpresa());

            if (vemp == null) {
                vemp = new VendedorEmpresa(vendSelec, d.getEmpresa(), 1, d.getValorNota(), d.getValorCredito());
                vendedorEmpresaRepository.insert(vemp);
            } else {
                vemp.addNumNotas();
                vemp.addValorTotal(d.getValorNota());
                vemp.addValorComissao(d.getValorCredito());
                vendedorEmpresaRepository.save(vemp);
            }
        }

        atualizaVendedor();

    }

    @FXML
    public void acVerificaEmpresa(Event event) {
        MouseEvent me = null;

        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;
            if ((me.getClickCount() == 2) && !tblView.getSelectionModel().isEmpty()) {
                vendEmpSelec = tblView.getSelectionModel().getSelectedItem();

                Empresa empresa = vendEmpSelec.getEmpresa();

                List<DocumentoFiscal> lstDocFiscal = new ArrayList<DocumentoFiscal>();

                lstDocFiscal = docFiscalRepository.findByEmpresa(empresa);

                LocalDate dataUltimaVenda = lstDocFiscal.get(0).getEmissao();
                System.out.println("Primeiro: " + dataUltimaVenda);

//        for (int i = 0; i < lstDocFiscal.size(); i++) {
//            if(dataUltimaVenda.is)
//        }
                for (DocumentoFiscal doc : lstDocFiscal) {
                    if (dataUltimaVenda.isBefore(doc.getEmissao())) {
                        dataUltimaVenda = doc.getEmissao();
                        System.out.println("Atualizando: " + dataUltimaVenda);
                    }
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(vendEmpSelec.getEmpresaFantasia().toUpperCase());
                alert.setContentText("Ultima nota emitida em: " + dataUltimaVenda.toString());
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void btnLimparClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, Config.i18n.getString("confirmacaoapagarbanco.text"), ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(Config.i18n.getString("dialogconfirmation.text"));
        alert.setTitle(Config.i18n.getString("limparbanco.text"));
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.YES)) {
            docFiscalRepository.deleteAll();
            vendedorEmpresaRepository.deleteAll();
            empresaRepository.deleteAll();
            vendedorRepository.deleteAll();
            cidadeRepository.deleteAll();

            atualizaVendedor();
            limpainterface();
//            System.out.println(Config.i18n.getString("repositoriolimpo.text"));
        }

    }

    @FXML
    public void btnRandomizaClick() {

        popularBanco();

        List<Empresa> lstEmpresa = new ArrayList<Empresa>(empresaRepository.findAll());
        List<Vendedor> lstVendedor = new ArrayList<Vendedor>(vendedorRepository.findAll());

        String dia = "", mes = "";

        //Gera valor e situação randomica
        Random rand = new Random();
        double randomNum;
        int randomSit;
        String situacao;

        for (int i = 0; i <= 100; i++) {
            randomSit = 0 + (int) (Math.random() * 10);
            if (randomSit % 2 == 0) {
                situacao = "CALCULADO";
            } else {
                situacao = "NOTA NÃO PARTICIPANTE";
            }

            DocumentoFiscal docFisc = new DocumentoFiscal(lstEmpresa.get(rand.nextInt(lstEmpresa.size())),
                    lstVendedor.get(rand.nextInt(lstVendedor.size())),
                    String.valueOf(rand.nextInt(100) + 1),
                    LocalDate.parse("24/10/2017", df),
                    //                    rand.nextDouble(),
                    //                    rand.nextDouble(),
                    randomNum = 50 + (int) (Math.random() * 350),
                    randomNum = 0.5 + (int) (Math.random() * 3.5),
                    situacao);
            docFiscalRepository.insert(docFisc);
        }
        limpainterface();
        atualizaVendedor();

//        System.out.println(lstCidades);
        System.out.println(lstEmpresa);

    }

    @FXML
    public void btnImportarClick2() {
        String linha;
        String[] partes;
        DocumentoFiscal docGravacao = null;
        Vendedor vendedorArquivo;

        final Stage stage = null;

        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(Config.i18n.getString("abrirarq.text"));
        fileChooser.setInitialDirectory(new File("src\\main\\resources\\dados"));
        File file = new File(String.valueOf(fileChooser.showOpenDialog(stage)));

//        if(!file.getName().endsWith(".txt")){
//            System.out.println(Config.i18n.getString("arquivotxt.text"));
//        } else {
        String[] nomeVendedor = file.getName().split(".");

        vendedorArquivo = new Vendedor(nomeVendedor[0]);
        vendedorRepository.insert(vendedorArquivo);

        try {
            br = new BufferedReader(new FileReader(file));
            linha = br.readLine();
            while ((linha = br.readLine()) != null) {

                DocumentoFiscal docFiscal = new DocumentoFiscal(linha, vendedorArquivo);

            }

            cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(
                    new Sort(
                            new Sort.Order(Sort.Direction.ASC, "nome"))
            )));
            atualizaVendedor();
            br.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
//        }
    }

    @FXML
    private void btnImportarClick() {
        try {
            chamaImporta();
        } catch (InterruptedException ex) {
            Logger.getLogger(EmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void chamaImporta() throws InterruptedException {
        final Task threadVerifica = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        Thread t = new Thread(threadVerifica);
        t.setDaemon(true);
        t.start();
        importa(); //CHAMADA DA FUNÇÃO PARA RODAR NA THREAD
    }

    public void importa() {
        String linha;
        String[] partes;
        DocumentoFiscal docGravacao = null;
        Vendedor vendedorArquivo;

//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        final Stage stage = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Config.i18n.getString("abrirarq.text"));
        fileChooser.setInitialDirectory(new File("src\\main\\resources\\dados"));

        File file = new File(String.valueOf(fileChooser.showOpenDialog(stage)));

        String[] nomeVendedor = file.getName().split("[.]+");

        vendedorArquivo = new Vendedor(nomeVendedor[0]);
        vendedorRepository.insert(vendedorArquivo);
        try {
            br = new BufferedReader(new FileReader(file));
            linha = br.readLine();
            while ((linha = br.readLine()) != null) {

                DocumentoFiscal docFiscal = new DocumentoFiscal(linha, vendedorArquivo);
               

            }
            cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(
                    new Sort(
                            new Sort.Order(Sort.Direction.ASC, "nome"))
            )));

            limpainterface();
            atualizaVendedor();
            acTotalizarVendedor();
            br.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (br != null) {

                    br.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        cmbVendedor.getSelectionModel().selectFirst();

        
    }
    
    @FXML
    public void acVerificar() {
        int max = 10000000;
        lblVerificando.setVisible(true);

        /*        tlBarStatus.setVisible(true);

        for( int i = 0; i <= max; i++){
            lbStatus.setText("Proc.:" + Integer.toString(i));
        }
                
         */
        final Task threadVerifica = new Task<Integer>() {
            @Override
            protected Integer call() throws InterruptedException {
                for (int i = 0; i <= max; i++) {
                    updateMessage("Proc.:" + Integer.toString(i));
                    updateProgress(i, max);
                }
                return 1;
            }
        };

        Thread t = new Thread(threadVerifica);
        t.setDaemon(true);
        t.start();

        tlBarStatus.visibleProperty().bind(threadVerifica.runningProperty());
        lbStatus.textProperty().bind(threadVerifica.messageProperty());
        progressBar.progressProperty().bind(threadVerifica.progressProperty());

        threadVerifica.stateProperty().
                addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                        if (newValue == Worker.State.SUCCEEDED) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Finalizado");
                            alert.setHeaderText("Verificação concluída");
                            alert.setContentText("Arquivo OK.");
                            alert.showAndWait();
                        }
                    }
                });

    }

    public void popularBanco() {
        vendedorRepository.insert(new Vendedor("Robson"));
        vendedorRepository.insert(new Vendedor("Nelso"));
        vendedorRepository.insert(new Vendedor("Gabriela"));

        cidadeRepository.insert(new Cidade("Ponta Grossa", "pr"));
        cidadeRepository.insert(new Cidade("Curitiba", "pr"));
        cidadeRepository.insert(new Cidade("Castro", "pr"));
        cidadeRepository.insert(new Cidade("Carambeí", "pr"));
        cidadeRepository.insert(new Cidade("Palmas", "pr"));
        cidadeRepository.insert(new Cidade("Londrina", "pr"));
        cidadeRepository.insert(new Cidade("Toledo", "pr"));
        cidadeRepository.insert(new Cidade("Guarapuava", "pr"));
        cidadeRepository.insert(new Cidade("Pato Branco", "pr"));
        cidadeRepository.insert(new Cidade("Cascavel", "pr"));
        cidadeRepository.insert(new Cidade("Maringá", "pr"));
        cidadeRepository.insert(new Cidade("Jaguariaíva", "pr"));
        cidadeRepository.insert(new Cidade("Tibagi", "pr"));
        cidadeRepository.insert(new Cidade("Arapoti", "pr"));
        cidadeRepository.insert(new Cidade("Prudentópolis", "pr"));
        cidadeRepository.insert(new Cidade("Irati", "pr"));
        cidadeRepository.insert(new Cidade("Pinhão", "pr"));
        cidadeRepository.insert(new Cidade("Canoinhas", "pr"));
        cidadeRepository.insert(new Cidade("Mallet", "pr"));
        cidadeRepository.insert(new Cidade("Reserva", "pr"));

        empresaRepository.insert(new Empresa("57.332.435/0001-50", "Taco Burger Cocina Mexicana"));
        empresaRepository.insert(new Empresa("72.575.521/0001-88", "Brasileirinho Delivery"));
        empresaRepository.insert(new Empresa("48.108.127/0001-55", "Bibas Lanches"));
        empresaRepository.insert(new Empresa("15.243.110/0001-51", "Cat's Burger"));
        empresaRepository.insert(new Empresa("59.833.723/0001-14", "Brother's Burger"));
        empresaRepository.insert(new Empresa("49.281.637/0001-92", "Faculdade do Lanche"));
        empresaRepository.insert(new Empresa("72.572.801/0001-32", "China in Box"));
        empresaRepository.insert(new Empresa("96.114.256/0001-07", "Boteco da Visconde"));
        empresaRepository.insert(new Empresa("11.907.213/0001-18", "Roots Bar"));
        empresaRepository.insert(new Empresa("80.680.724/0001-62", "Pizzas e Esfihas Fornalha"));
        empresaRepository.insert(new Empresa("92.570.601/0001-58", "Bora Bora Espetos Bar"));
        empresaRepository.insert(new Empresa("15.857.429/0001-77", "Lanchonete Papel Carbono"));
        empresaRepository.insert(new Empresa("35.782.335/0001-93", "BoteKing BarBurgueria"));
        empresaRepository.insert(new Empresa("48.057.526/0001-34", "Piwiarnia Snooker Bar"));
        empresaRepository.insert(new Empresa("52.338.154/0001-91", "Ferri Pizza"));
        empresaRepository.insert(new Empresa("14.051.422/0001-09", "Coffee Maria's"));
        empresaRepository.insert(new Empresa("17.721.486/0001-50", "Boliche Strike 7 Bar "));
        empresaRepository.insert(new Empresa("34.437.534/0001-00", "Donna Pizza & Pub"));

    }

    public void atualizaVendedor() {
        vendSelec = cmbVendedor.getSelectionModel().getSelectedItem();

//        System.out.println(docFiscalRepository.countByVendedor(vendSelec));
        tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findByVendedor(vendSelec)));
    }

    public void limpainterface() {
        cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")))));
        tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findAll()));
        cmbVendedor.getSelectionModel().selectFirst();
    }

    public void lerArquivo(Vendedor vend) {
        String nomeArq = "src\\main\\resources\\dados\\" + vend.getNome() + ".txt";
        String linha;
        try {
            br = new BufferedReader(new FileReader(nomeArq));
            linha = br.readLine();
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");

                String cnpj = partes[0].replaceAll("[./-]", "");

                String valor = partes[4].replaceAll("R$ ", "");
                valor = valor.replace(",", ".");
                Double valorDouble = Double.parseDouble(valor);

                String credito = partes[5].replaceAll("R$ ", "");
                credito = credito.replace(",", ".");
                Double creditoDouble = Double.parseDouble(credito);

                String situacao = partes[6];

                DocumentoFiscal docfiscal = new DocumentoFiscal(empresaRepository.findByCnpj(cnpj), vend, partes[2], LocalDate.parse(partes[3]), valorDouble, creditoDouble, situacao);

                docFiscalRepository.insert(docfiscal);
            }
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo (1): " + e.getMessage());
        }

    }

    @FXML
    public void acAlertVen() {
//        vendSelec = cmbVendedor.getSelectionModel().getSelectedItem();

        List<DocumentoFiscal> lstDoc = new ArrayList<DocumentoFiscal>(docFiscalRepository.findAll());
        int tamanho = lstDoc.size();
        Empresa empresaMaior = lstDoc.get(0).getEmpresa();
        double valorTotal = 0;
        double comissao = 0;
        comissao = lstDoc.get(0).getValorNota();
//        for (DocumentoFiscal doc : lstDoc) {
//            comissao = comissao + doc.getValorNota();
//            if(comissao < )
//        }

        for (int i = 0; i < tamanho; i++) {
            if (comissao < lstDoc.get(i).getValorNota()) {
                comissao = lstDoc.get(i).getValorNota();
                empresaMaior = lstDoc.get(i).getEmpresa();
            }
            valorTotal = lstDoc.get(i).getValorNota();

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION); //Estrutura do alert
        alert.setHeaderText("Maior empresa");
        alert.setContentText(empresaMaior.toString() + "\n"
                //                + "Valor Total: " + valorTotal + "\n"
                + "Valor Total: " + comissao);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbVendedor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    atualizaVendedor();
                    acTotalizarVendedor();
                }
        );

        cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));

        cmbVendedor.getSelectionModel().selectFirst();

//        tlBarStatus.setVisible(false);
    }
}
