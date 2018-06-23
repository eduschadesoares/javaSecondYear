package view;

import config.Config;
import static config.Config.df;
import config.DAO;
import static config.DAO.cidadeRepository;
import static config.DAO.docFiscalRepository;
import static config.DAO.vendedorRepository;
import static config.DAO.empresaRepository;
import static config.DAO.empresaRepository;
import static config.DAO.vendedorEmpresaRepository;
import static config.DAO.vendedorRepository;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.Vendedor;
import model.Empresa;
import model.Cidade;
import model.DocumentoFiscal;
import model.VendedorEmpresa;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.util.Duration;
import model.Uf;
import repository.UfRepository;

public class PrincipalController implements Initializable {

    @FXML
    public Label lblTitle, lblTime;

    public Timeline timeline;

//    @FXML
//    public Tab tabCidade;
    //VARIÁVEIS DA FUNÇÃO PISCA
    int i = 0; //PARA INCREMENTAR INFINITO
    boolean ligaLblText; //BOOL PARA SETAR O LABEL

    /*    @FXML
    private void acAtualizaCidade (Event event) {
        CidadeController cidade = new CidadeController();
        cidade.atualizaTableView();
        
    } */
    private void loop() {
        //VELOCIDADE COM QUE VAI PISCAR O LABEL (1000 = 1 SEG)
        KeyFrame frame = new KeyFrame(Duration.millis(50), e -> pisca());
        Timeline tl = new Timeline(frame);
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private void pisca() {
        i =2;
        if (i % 2 == 0) {
            ligaLblText = true;
        } else {
            ligaLblText = false;
        }

        lblTitle.setVisible(ligaLblText);

    }

    private void atualizaHora() {
        GregorianCalendar date = new GregorianCalendar();
        int dia, mes, ano, hr, min, seg;

        dia = date.get(Calendar.DAY_OF_MONTH); //PEGA DIA
        mes = date.get(Calendar.MONTH); //PEGA MÊS
        ano = date.get(Calendar.YEAR); //PEGA ANO

        hr = date.get(Calendar.HOUR_OF_DAY); //PEGA HORA
        min = date.get(Calendar.MINUTE); //PEGA MINUTO
        seg = date.get(Calendar.SECOND); //PEGA SEGUNDO

        //NÃO SERÁ UTILIZADO O LABEL MOSTRANDO NÚMEROS RANDOMICOS AQUI
/*        Integer randomNum;
        randomNum = 1 + (int) (Math.random() * 200); 

        lblRand.setText(Integer.toString(randomNum)); */
        //FORMATA A STRING PARA MOSTRAR NO LABEL O FORMATO (HH:MM:SS)
        lblTime.setText(String.format("%02d", hr) + ":" + String.format("%02d", min) + ":" + String.format("%02d", seg));
    }

    public void relogio() {
        //TEMPO PARA A ATUALIZAÇÃO DO RELÓGIO
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
        relogio(); //CHAMADA DA FUNÇÃO PARA RODAR NA THREAD
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //INICIALIZA O LOOP COM O LABEL PISCANDO
        loop();

        //INICIALIZA O RELÓGIO NUMA THREAD (TEM QUE ESTAR NUM TRY)
        try {
            relogioManual();
        } catch (InterruptedException ex) {
            Logger.getLogger(EmpresaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        UF uf = UFRepository.insert(new UF("Paraná", "Paranaense", "pr"));

//        UFRepository.insert(new UF("Paraná", "Paranaense", "pr"));
//        UF uf = new UF("Paraná", "Paranaense", "pr");

//        UfRepository.insert(new UF("Paraná", "Paranaense", "pr"));
        
        
        

        /*
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
        
        Vendedor ven = new Vendedor("João da Silva");
        ven = vendedorRepository.insert(ven);

        Empresa emp = new Empresa("22660046000170",
                "Lojão do Queima", cid);
        emp = empresaRepository.insert(emp);
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven,"120",LocalDate.parse("01/05/2017", df), 1230, 110));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "118", LocalDate.parse("12/05/2017", df), 2230, 81));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "128", LocalDate.parse("14/05/2017", df), 2370, 110));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "127", LocalDate.parse("17/05/2017", df), 2230, 210));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "132", LocalDate.parse("18/05/2017", df), 5230, 310));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "133", LocalDate.parse("19/05/2017", df), 6230, 100));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "134", LocalDate.parse("20/05/2017", df), 2230, 110));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "144", LocalDate.parse("21/05/2017", df), 8230, 810));
        
        VendedorEmpresa vendEmp = new VendedorEmpresa(ven, emp,
                8, 45098.80, 2345.9);
        vendedorEmpresaRepository.insert(vendEmp);

        
        emp = new Empresa("56660046000170","Maxi Tango", cid);
        emp = empresaRepository.insert(emp);
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven,"140",LocalDate.parse("01/05/2017", df), 1230, 110));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "148", LocalDate.parse("15/05/2017", df), 1230, 81));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "138", LocalDate.parse("15/05/2017", df), 1370, 10));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "137", LocalDate.parse("12/05/2017", df), 1230, 20));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "132", LocalDate.parse("13/05/2017", df), 1230, 30));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "134", LocalDate.parse("14/05/2017", df), 1230, 10));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "144", LocalDate.parse("25/05/2017", df), 1230, 10));
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven, "178", LocalDate.parse("27/05/2017", df), 1230, 80));

        vendEmp = new VendedorEmpresa(ven, emp,
                8, 54045.80, 4545.9);
        vendedorEmpresaRepository.insert(vendEmp);
        
        ven = new Vendedor("Joaquim Manuel");
        ven = vendedorRepository.insert(ven);
        
        emp = empresaRepository.findByCnpj("22660046000170");
        
        docFiscalRepository.insert(new DocumentoFiscal(emp, ven,"6140",LocalDate.parse("10/05/2017", df), 100230, 12110));
        
        
        vendEmp = new VendedorEmpresa(ven, emp,
                1, 100230.0, 12110.0);
        
        vendedorEmpresaRepository.insert(vendEmp);*/
        
        
        //EXPRESSÕES REGULARES EXEMPLO
//        System.out.println("Data: " + "60.484.433/0001-91".matches("([A-Z]|[a-z]){2}(" + "(\\d{9})" + "([A-Z]|[a-z]){2})?"));
//
//        System.out.println("Data2: " + "60.484.433/0001-91".matches("[0-9]{2}" + "." + "[0-9]{3}" + "." + "[0-9]{3}" + "/" + "[0-9]{4}" + "-" + "[0-9]{2}"));
//       
//        System.out.println("Data3: " + "60.484.433/0001-91".matches("\\d{2}" + "." + "\\d{3}" + "." + "[0-9]{3}" + "/" + "[0-9]{4}" + "-" + "[0-9]{2}"));
//
//        System.out.println("Data4: " + "60.484.433/0001-91".matches("[0-9]{2}.[0-9]{3}.[0-9]{3}/[0-9]{4}-[0-9]{2}"));

    }

}
