/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.Event;
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
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;
import utility.NossoPopOver;

/**
 * FXML Controller class
 *
 * @author mrgomes
 */
public class EmpresaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    public Empresa empresa;
    
    public static List<Empresa> lstFiliais = new ArrayList<Empresa>();

    @FXML
    public TableView<Empresa> tblView;

    @FXML
    public TextField txtFldFiltro;

    @FXML
    public Button btnAlertCidade, btnFiltrarCNPJ;

    @FXML
    public Label lbRand;

    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) {
        if (ev.getCode() == KeyCode.ENTER) {
            if (ev.getText().isEmpty()) {
                tblView.setItems(FXCollections.observableList(empresaRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")))));
            } else {
                tblView.setItems(FXCollections.observableList(empresaRepository.findByNomeLikeIgnoreCaseOrFantasiaLikeIgnoreCase(txtFldFiltro.getText(), txtFldFiltro.getText())));
            }
        }
    }

    @FXML
    public void acAlertInfoCidade() {
        empresa = tblView.getSelectionModel().getSelectedItem();
        int c = empresaRepository.countByCidade(empresa.getCidade());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(i18n.getString("infoCidade.text"));
        alert.setHeaderText(empresa.getCidade().toString().toUpperCase());
        if (c == 1) alert.setContentText(i18n.getString("quantasEmpresasNaCidadeApenas1.text") + " " + i18n.getString("quantasEmpresasNaCidade2sing.text"));
        else alert.setContentText(i18n.getString("quantasEmpresasNaCidade1.text") + " " + c + " " + i18n.getString("quantasEmpresasNaCidade2.text"));
        alert.showAndWait();
    }
    
    @FXML
    public void acFiltrarCNPJ(){
        String sub = empresa.getCnpj().substring(0, 8);

        for (Empresa e:empresaRepository.findAll()){
            if(e.getCnpj().startsWith(sub)) lstFiliais.add(e);
        }
        
        tblView.setItems(FXCollections.observableList(lstFiliais));
    }

    @FXML
    public void acAtualizarEmpresa(Event event) {
        MouseEvent me = null;

        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;

            if ((me.getClickCount() == 2) && !tblView.getSelectionModel().isEmpty()) {
                empresa = tblView.getSelectionModel().getSelectedItem();

                atualizaEmpresa();
            }
        }
    }

    public void atualizaEmpresa() {
        String cena = "/fxml/CRUDEmpresa.fxml";

        NossoPopOver popOver = null;
        popOver = new NossoPopOver(cena, "Atualizar " + empresa.getNome(), null);

        CRUDEmpresaController controller = popOver.getLoader().getController();
        controller.setControllerPai(this);
    }

    private void atualizaRandom() {
        Random rand = new Random();
        int r = rand.nextInt(501);
        lbRand.setText(String.valueOf(r));
    }

    public void contagem() {
        KeyFrame frame = new KeyFrame(Duration.millis(1000), e -> atualizaRandom());
        Timeline tl = new Timeline(frame);
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tblView.setItems(FXCollections.observableList(empresaRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));

        contagem();

    }
}
