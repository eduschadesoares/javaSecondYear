/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Cidade;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import model.VendedorEmpresa;
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;
 
/**
 * FXML Controller class
 *
 * @author idomar
 */
public class VendedorEmpresaController implements Initializable {
 
    /**
     * Initializes the controller class.
     */
    
    public Empresa empresa;
    public Vendedor vendSelec;
    
    private BufferedReader br = null;
    
    public List<Cidade> lstCidades = cidadeRepository.findAll();
    
    @FXML
    public TableView<VendedorEmpresa> tblView;
 
    @FXML
    public ComboBox<Vendedor> cmbVendedor;
    
    @FXML
    public TextField txtFldFiltro;
    
    @FXML
    public ToolBar tlBarStatus;
    
    @FXML
    public Label lbStatus;
    
    @FXML
    public ProgressBar progressBar;
    
    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) throws ParseException /*******/ {
        if(ev.getCode() == KeyCode.ENTER){
            try {
                if (nf.parse(txtFldFiltro.getText()).doubleValue() > 0)
                    tblView.setItems(FXCollections.observableList(
                        vendedorEmpresaRepository.findByVendedorAndValorComissaoGreaterThan(vendSelec, nf.parse(txtFldFiltro.getText()).doubleValue())));
                else if (nf.parse(txtFldFiltro.getText()).doubleValue() < 0)
                    tblView.setItems(FXCollections.observableList(
                        vendedorEmpresaRepository.findByVendedorAndValorComissaoGreaterThan(vendSelec, nf.parse(txtFldFiltro.getText()).doubleValue()*(-1))));
                else if (nf.parse(txtFldFiltro.getText()).doubleValue() == 0)
                    tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findAll()));
                else if (txtFldFiltro.getText() == "") tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findAll()));
            } catch (ParseException e) {
                if (e.getMessage().contains("Unparseable number: \"\"")) tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findAll()));
                else System.out.println(e.getMessage());
            }
        }
            
    }
    
    @FXML
    public void acTotalizarVendedor(){        
        vendSelec = cmbVendedor.getSelectionModel().getSelectedItem();
        
        vendedorEmpresaRepository.deleteByVendedor(vendSelec);
        
        for (DocumentoFiscal d: docFiscalRepository.findByVendedor(vendSelec)){
            VendedorEmpresa vemp = vendedorEmpresaRepository.findByVendedorAndEmpresa(vendSelec, d.getEmpresa());

            if(vemp == null){
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
            System.out.println(Config.i18n.getString("repositoriolimpo.text"));
        }

    }

    @FXML
    public void btnRandomizaClick() {
        
        popularBanco();
        
        List<Empresa> lstEmpresa = new ArrayList<Empresa>(empresaRepository.findAll());
        List<Vendedor> lstVendedor = new ArrayList<Vendedor>(vendedorRepository.findAll());
        
        Random rand = new Random();
        String dia = "", mes = "";

        for (int i = 0; i <= 100; i++) {

            DocumentoFiscal docFisc = new DocumentoFiscal(lstEmpresa.get(rand.nextInt(lstEmpresa.size())),
                    lstVendedor.get(rand.nextInt(lstVendedor.size())),
                        String.valueOf(rand.nextInt(100) + 1),
                        LocalDate.parse("24/10/2017", df),
                        rand.nextDouble(),
                        rand.nextDouble());
            docFiscalRepository.insert(docFisc);
        }
        limpainterface();
        atualizaVendedor();
    }
    
    @FXML
    public void btnImportarClick2(){ //não é usado
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
    } // não é usado
    
    @FXML
    private void btnImportarClick() {

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

    }

    @FXML
    public void acVerificar(){
        int max = 10000000;
//        tlBarStatus.setVisible(true);
        
        /*
        for( int i = 0; i <= max; i++){
            lbStatus.setText("Proc.:" + Integer.toString(i));
        }
        */
        
        final Task threadVerifica = new Task<Integer>(){
            @Override
            protected Integer call() throws InterruptedException{
                for (int i = 0; i <= max; i++){
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
                        if(newValue == Worker.State.SUCCEEDED) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Finalizado");
                            alert.setHeaderText("Verificação concluída");
                            alert.setContentText("Arquivo OK.");
                            alert.showAndWait();
                        }
                    } 
                });
        
    }
    
    
    public int cidadeRandom(){
        //Random rand = new Random();
        return new Random().nextInt(cidadeRepository.findAll().size()-1);
    }
    
    public void popularBanco(){
        vendedorRepository.insert(new Vendedor("Robson"));
        vendedorRepository.insert(new Vendedor("Nelso"));
        vendedorRepository.insert(new Vendedor("Shrek"));
        vendedorRepository.insert(new Vendedor("Glauber"));
        vendedorRepository.insert(new Vendedor("Matheus"));
        vendedorRepository.insert(new Vendedor("Eduardo"));
        vendedorRepository.insert(new Vendedor("Davi"));
        vendedorRepository.insert(new Vendedor("Lucas"));
        vendedorRepository.insert(new Vendedor("Silvia"));
        vendedorRepository.insert(new Vendedor("Glaucia"));
        vendedorRepository.insert(new Vendedor("Guilherme"));
        vendedorRepository.insert(new Vendedor("Gustavo"));

        
        Cidade cid = cidadeRepository.insert(new Cidade("Ponta Grossa"));
        cidadeRepository.insert(new Cidade("Curitiba"));
        cidadeRepository.insert(new Cidade("Castro"));
        cidadeRepository.insert(new Cidade("Carambeí"));
        cidadeRepository.insert(new Cidade("Palmas"));
        cidadeRepository.insert(new Cidade("Londrina"));
        cidadeRepository.insert(new Cidade("Toledo"));
        cidadeRepository.insert(new Cidade("Guarapuava"));
        cidadeRepository.insert(new Cidade("Pato Branco"));
        cidadeRepository.insert(new Cidade("Cascavel"));
        cidadeRepository.insert(new Cidade("Maringá"));
        cidadeRepository.insert(new Cidade("Jaguariaíva"));
        cidadeRepository.insert(new Cidade("Tibagi"));
        cidadeRepository.insert(new Cidade("Arapoti"));
        cidadeRepository.insert(new Cidade("Prudentópolis"));
        cidadeRepository.insert(new Cidade("Irati"));
        cidadeRepository.insert(new Cidade("Pinhão"));
        cidadeRepository.insert(new Cidade("Canoinhas"));
        cidadeRepository.insert(new Cidade("Mallet"));
        cidadeRepository.insert(new Cidade("Reserva"));
        
        empresaRepository.insert(new Empresa("57332435000150", "Taco Burger Cocina Mexicana", cid));
        empresaRepository.insert(new Empresa("72575521000188", "Brasileirinho Delivery", cid));
        empresaRepository.insert(new Empresa("48108127000155", "Bibas Lanches", cid));
        empresaRepository.insert(new Empresa("15243110000151", "Cat's Burger", cid));
        empresaRepository.insert(new Empresa("59833723000114", "Brother's Burger", cid));
        empresaRepository.insert(new Empresa("49281637000192", "Faculdade do Lanche", cid));
        empresaRepository.insert(new Empresa("72572801000132", "China in Box", cid));
        empresaRepository.insert(new Empresa("96114256000107", "Boteco da Visconde", cid));
        empresaRepository.insert(new Empresa("11907213000118", "Roots Bar", cid));
        empresaRepository.insert(new Empresa("80680724000162", "Pizzas e Esfihas Fornalha", cid));
        empresaRepository.insert(new Empresa("92570601000158", "Bora Bora Espetos Bar", cid));
        empresaRepository.insert(new Empresa("15857429000177", "Lanchonete Papel Carbono", cid));
        empresaRepository.insert(new Empresa("35782335000193", "BoteKing BarBurgueria", cid));
        empresaRepository.insert(new Empresa("48057526000134", "Piwiarnia Snooker Bar", cid));
        empresaRepository.insert(new Empresa("52338154000191", "Ferri Pizza", cid));
        empresaRepository.insert(new Empresa("14051422000109", "Coffee Maria's", cid));
        empresaRepository.insert(new Empresa("17721486000150", "Boliche Strike 7 Bar ", cid));
        empresaRepository.insert(new Empresa("34437534000100", "Donna Pizza & Pub", cid));
        empresaRepository.insert(new Empresa("80221013000120", "Tozetto Vila Estrela", cid));
        empresaRepository.insert(new Empresa("80221013000220", "Tozetto Nova Russia", cid));
        
        
        
    }
 
    public void atualizaVendedor() {
        vendSelec = cmbVendedor.getSelectionModel().getSelectedItem();
        
//        System.out.println(docFiscalRepository.countByVendedor(vendSelec));
        
        tblView.setItems(FXCollections.observableList(vendedorEmpresaRepository.findByVendedor(vendSelec)));
    }
    
    public void limpainterface(){
        cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")) )));
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
                    
                DocumentoFiscal docfiscal = new DocumentoFiscal(empresaRepository.findByCnpj(cnpj), vend, partes[2], LocalDate.parse(partes[3]), valorDouble, creditoDouble);

                docFiscalRepository.insert(docfiscal);
            }
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo (1): " + e.getMessage());
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        cmbVendedor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    atualizaVendedor();
                    acTotalizarVendedor();/////////////////////////////////////////////////////////////////
                }
        );
        
        cmbVendedor.setItems(FXCollections.observableList(vendedorRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));
        
//        tlBarStatus.setVisible(false);
    }
}