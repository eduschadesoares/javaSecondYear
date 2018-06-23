package view;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import config.Config;
import static config.Config.df;
import static config.Config.i18n;
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
import static java.lang.Integer.max;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cidade;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import model.VendedorEmpresa;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;
import utility.NossoPopOver;

public class EmpresaController implements Initializable {

    private EmpresaController controllerPai;

    public Empresa empresa;

    public Random rand = new Random();

    @FXML
    public TableView<Empresa> tblView;

    @FXML
    public TextField txtFldFiltro;

    @FXML
    public Button btnAtualizaEmp, btnEditEmp, btnAlert, btnCorrigir;

    @FXML
    public Label lblHour, lblRand;

    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) throws ParseException {
//        if (ev.getCode() == KeyCode.ENTER) {
        tblView.setItems(FXCollections.observableList(
                empresaRepository.findByNomeLikeIgnoreCaseOrFantasiaLikeIgnoreCase(txtFldFiltro.getText(),
                        txtFldFiltro.getText())));
//    }


        /*            try {
            if (ev.getText().isEmpty()) {
                empresaRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")));
            } else {
                empresaRepository.findByNomeLikeIgnoreCaseOrFantasiaLikeIgnoreCase(txtFldFiltro.getText(), txtFldFiltro.getText());
            }
            } catch (Exception e) { 
                System.out.println(e.getMessage());
            }
        }
         */
    }

    @FXML
    public void acAtualizarEmpresa(Event event) {
        MouseEvent me = null;

        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;

            if ((me.getClickCount() == 2) && !tblView.getSelectionModel().isEmpty()) {
                acAlert();
            } else if (me.getButton() == MouseButton.SECONDARY) //CLick botão Direito
            {
                empresa = tblView.getSelectionModel().getSelectedItem();
                atualizaEmpresa();
            }
        }
    }

    @FXML
    public void acEditarEmp() {
        empresa = tblView.getSelectionModel().getSelectedItem();
        atualizaEmpresa();
        atualizaTableView();
    }

    public void atualizaEmpresa() {
        String cena = "/fxml/CRUDEmpresa.fxml";

        NossoPopOver popOver = null;
        popOver = new NossoPopOver(cena, empresa.getNome(), null);

        CRUDEmpresaController controller = popOver.getLoader().getController();
        controller.setControllerPai(this);
    }

    @FXML
    public void acAtualizarEmp() {
        atualizaTableView();
    }

    public void atualizaTableView() {
        tblView.setItems(FXCollections.observableList(empresaRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));
        tblView.refresh();
        tblView.getSelectionModel().selectFirst();
    }

    @FXML
    private void acAlert() {
        //Seleciona a empresa já selecionada no TableView
        empresa = tblView.getSelectionModel().getSelectedItem();

        //Qtde empresas emcontradas por cidade (mesmo do cidadeController)
        int qntdEmpresas = empresaRepository.countByCidade(empresa.getCidade());
        
        //QTDE NOTAS DA EMPRESA
        int qntdNotasEmp;

        //Valor das notas por empresa
        double somaValorNotas = 0;

        List<DocumentoFiscal> lstDocFiscal = new ArrayList<DocumentoFiscal>();
        lstDocFiscal = docFiscalRepository.findByEmpresa(empresa);
        //PEGA A QNTD DE NOTAS QUE É O TAMANHO DA LISTA
        qntdNotasEmp = lstDocFiscal.size();
        
        //A lstDocFiscal pode ser declarada já na inicialização dentro do for de lista
        for (DocumentoFiscal doc : docFiscalRepository.findByEmpresa(empresa)) {
            somaValorNotas = somaValorNotas + doc.getValorNota();
        }

        try {
            //Pega o nome da cidade selecionada no TableView
            String cidade = tblView.getSelectionModel().getSelectedItem().getCidade().getNome();

            Alert alert = new Alert(Alert.AlertType.INFORMATION); //Estrutura do alert
            alert.setHeaderText(empresa.getNome());

            if (qntdEmpresas == 1) {
                alert.setContentText("Existe " + qntdEmpresas + " empresa em " + cidade + "\n"
                        + "A empresa " + empresa.getFantasia().toUpperCase() + " tem " + qntdNotasEmp + " notas" + "\n"
                        + "A empresa comprou: R$" + String.format("%.2f", somaValorNotas));
            } else {
                alert.setContentText("Existem " + qntdEmpresas + " empresas em " + cidade + "\n"
                        + "A empresa " + empresa.getFantasia().toUpperCase() + " tem " + qntdNotasEmp + " notas" + "\n"
                        + "A empresa comprou: R$" + String.format("%.2f", somaValorNotas));
            }

            alert.showAndWait();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /*    @FXML
    private void acCorrigir() {
        empresa = tblView.getSelectionModel().getSelectedItem();
        
//        int qntdEmpresas = empresaRepository.countByCidade(empresa.getCidade());
        try {
//            String cidade = tblView.getSelectionModel().getSelectedItem().getCidade().getNome();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(empresa.getNome());
            alert.setContentText("O nome da fantasia e o CNPJ da empresa " + empresa.getNome() + " padronizado!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                cnpj(empresa);
                upper(empresa);
                atualizaTableView();
            } else {
                alert.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } 
    } */
    @FXML
    public void acCorrigirTodos() {
//        empresa = tblView.getSelectionModel().getSelectedItem(); //NÃO PRECISA SER SELECIONADO NADA AQUI

        List<Empresa> lstEmpresa = new ArrayList<Empresa>();
        lstEmpresa = empresaRepository.findAll(); //É UTILIZADO PARA PEGAR O SIZE DA LISTA

        List<Cidade> lstCidade = new ArrayList<Cidade>();
        lstCidade = cidadeRepository.findAll();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Padronização de cadastro de empresa");
        alert.setContentText("Todas as " + lstEmpresa.size() + " empresas serão padronizadas!");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            for (Empresa e : lstEmpresa) {
                formattedFantasia(e); //FORMATA O NOME FANTASIA
                formattedCNPJ(e); //FORMATA O CNPJ (TIRAR SINAIS DE PONTUAÇÃO)
                relacionarCidades(e, lstCidade); //ADD UMA CIDADE AS EMPRESAS
            }
            atualizaTableView();
        } else {
            alert.close();
        }

    }

    public void formattedCNPJ(Empresa empresa) {
        //PEGA O CNPJ DA EMPRESA
        String CNPJ = (empresa.getCnpj());

        //REMOVE TODOS OS SINAIS DE PONTUAÇÃO
        CNPJ = CNPJ.replaceAll("[./-]+", "");

        //PASSA PARA A CLASSE
        empresa.setCnpj(CNPJ);

        //SALVA NO BANCO
        empresaRepository.save(empresa);

    }

    public void formattedFantasia(Empresa empresa) {
        String fantasia = (empresa.getFantasia().toLowerCase());

        //CONVERTE PARA ARRAY DE CHAR
        char[] frase = fantasia.toCharArray();

        for (int i = 0; i < fantasia.length() - 1; i++) {
            frase[0] = Character.toUpperCase(frase[0]);
            if ((fantasia.charAt(i) == ' ') || (fantasia.charAt(i) == '.') || (fantasia.charAt(i) == '/')
                    || (fantasia.charAt(i) == '-') || (fantasia.charAt(i) == '&') || (fantasia.charAt(i) == '(')
                    || (fantasia.charAt(i) == ')') || (fantasia.charAt(i) == ':') || (fantasia.charAt(i) == '+')
                    || (fantasia.charAt(i) == '#') || (fantasia.charAt(i) == '!') || (fantasia.charAt(i) == '@')
                    || (fantasia.charAt(i) == '"') || (fantasia.charAt(i) == '?') || (fantasia.charAt(i) == 'º')
                    || (fantasia.charAt(i) == 'ª') || (fantasia.charAt(i) == '=')) {

                //COLOCA O PRÓXIMO CARACTERE EM MAIÚSCULO TUDO O QUE É SEGUIDO DAS CONDIÇÕES ACIMA
                frase[i + 1] = Character.toUpperCase(frase[i + 1]);
            }
        }

        //CONVERTE NOVAMENTE PARA STRING
        fantasia = String.valueOf(frase);

        //PASSA PARA A CLASSE
        empresa.setFantasia(fantasia);

        //SALVA NO BANCO
        empresaRepository.save(empresa);

    }

    public void relacionarCidades(Empresa empresa, List<Cidade> lstCidade) {
        //AJUDA
//        System.out.println("QNtd de cidade passadas: " + lstCidade.size());

        //CRIA UMA NOVA CIDADE
        Cidade cidade = new Cidade();

        //GERA UM NÚMERO RANDOMICO ENTRE 0 E O TAMANHO DA LISTA DE CIDADE
        Integer cidadeRand;
        cidadeRand = 0 + (int) (Math.random() * lstCidade.size());

        //CIDADE CRIADA VAI RECEBER A CIDADE ALEATÓRIA
        cidade = lstCidade.get(cidadeRand);

        try {
            if (lstCidade.size() > 0) { //SE A LISTA NÃO FOR VAZIA
                empresa.setCidade(cidade); //SETA A EMPRESA RANDOMICA PARA A EMPRESA SELECIONADA E PASSADA COMO PARÂMETRO
            } else {
                System.out.println("Não há cidades");
                return;
            }
        } catch (Exception e) {
            System.out.println("Não existem cidades");
            System.err.println(e.getMessage());

        }
        //SALVA NO BANCO
        empresaRepository.save(empresa);
    }

    private void atualizaHora() {
        GregorianCalendar date = new GregorianCalendar();
        int dia, mes, ano, hr, min, seg;

        dia = date.get(Calendar.DAY_OF_MONTH);
        mes = date.get(Calendar.MONTH);
        ano = date.get(Calendar.YEAR);

        hr = date.get(Calendar.HOUR_OF_DAY);
        min = date.get(Calendar.MINUTE);
        seg = date.get(Calendar.SECOND);

        //REGA UM NÚMERO RANDOMICO ENTRE 1 E 200
        Integer randomNum;
        randomNum = 1 + (int) (Math.random() * 200);

        //MANDA FORMATANDO DE INT PARA STRING O NÚMERO RANDOMICO
        lblRand.setText(Integer.toString(randomNum)); //FORMAT

//        lblHour.setText(String.format("%02d", hr) + ":" + String.format("%02d", min) + ":" + String.format("%02d", seg));
    }

    public void relogio() {
        //DURAÇÃO DA ATUALIZAÇÃO DO KEYFRAME PARA A CHAMADA DA FUNÇÃO 
        KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> atualizaHora());
        Timeline tl = new Timeline(frame);
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    public void relogioManual() throws InterruptedException {
        final Task threadVerifica = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        Thread t = new Thread(threadVerifica);
        t.setDaemon(true);
        t.start();
        relogio(); //STARTA O RELÓGIO
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try { //INICIA O RELÓRIO NUMA THREAD
            relogioManual();

        } catch (InterruptedException ex) {
            Logger.getLogger(EmpresaController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        atualizaTableView();
    }

}
