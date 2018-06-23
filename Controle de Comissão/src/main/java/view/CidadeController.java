/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
 
import com.sun.org.apache.xml.internal.security.utils.I18n;
import config.Config;
import static config.Config.ALTERAR;
import static config.Config.EXCLUIR;
import static config.Config.INCLUIR;
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
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Cidade;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import model.VendedorEmpresa;
import org.springframework.dao.support.DaoSupport;
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;
import repository.CidadeRepository;
import utility.NossoPopOver;
 
/**
 * FXML Controller class
 *
 * @author mrgomes
 */
public class CidadeController implements Initializable {
 
    /**
     * Initializes the controller class.
     */
    public char acao;
    public Cidade cidade;   
    
    @FXML
    public TableView<Cidade> tblView;
     
    @FXML
    public TextField txtFldFiltro;
    
    @FXML
    public Button btnAdd, btnRem, btnEdit; 
    
    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) {
        if(ev.getCode() == KeyCode.ENTER){
            if(ev.getText().isEmpty()) tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")))));
            else tblView.setItems(FXCollections.observableList(cidadeRepository.findByNomeLikeIgnoreCase(txtFldFiltro.getText())));
        }
    }
    
    @FXML
    private void acIncluir() {
        acao = INCLUIR;
        cidade = new Cidade();
        showCRUD();
    }
    
    public void acComboIncluir() {
        acao = INCLUIR;
        cidade = new Cidade();
        showCRUD();
    }

    @FXML
    private void acExcluir() {
        acao = EXCLUIR;
        cidade = tblView.getSelectionModel().getSelectedItem();
        showCRUD();
    }
    
    @FXML
    private void acEditar() {
        acao = ALTERAR;
        cidade = tblView.getSelectionModel().getSelectedItem();
        showCRUD();
    }
    
    private void showCRUD() {
        String scene = "/fxml/CRUDCidade.fxml";
        NossoPopOver popOver = null;

        switch (acao) {
            case INCLUIR:
                popOver = new NossoPopOver(scene, i18n.getString("addCidade.text"), null);
                break;
            case EXCLUIR:
                popOver = new NossoPopOver(scene, i18n.getString("remCidade.text"), null);
                break;
            case ALTERAR:
                popOver = new NossoPopOver(scene, i18n.getString("editCidade.text"), null);
                break;
        }

        CRUDCidadeController controller = popOver.getLoader().getController();
        controller.setControllerPai(this);

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));
        
    }
}