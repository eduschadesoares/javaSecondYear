/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static config.Config.ALTERAR;
import static config.Config.INCLUIR;
import static config.Config.EXCLUIR;
import static config.Config.i18n;
import config.DAO;
import static config.DAO.cidadeRepository;
import static config.DAO.empresaRepository;
import static config.DAO.vendedorEmpresaRepository;
import static config.DAO.vendedorRepository;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Cidade;
import org.springframework.data.domain.Sort;

/**
 * FXML Controller class
 *
 * @author mbilo
 */
public class CRUDCidadeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private CidadeController controllerPai;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField txtFldNome;

    @FXML
    private Button btnConfirma;

    @FXML
    private void btnCancelaClick() {
        anchorPane.getScene().getWindow().hide();
    }

    @FXML
    private void btnConfirmaClick() {
        controllerPai.cidade.setNome(txtFldNome.getText());
        try {
            switch (controllerPai.acao) {
                case INCLUIR:
                    try {
                        cidadeRepository.insert(new Cidade(txtFldNome.getText()));
                    } catch (Exception e) {
                        if (e.getMessage().contains("duplicate key")) {
                            cidadeRepository.findByNomeIgnoreCase(txtFldNome.getText());
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText(i18n.getString("cidadeJaExiste.text"));
                            alert.setContentText(i18n.getString("cidadeJaExisteCompleto.text"));
                            alert.showAndWait();
                        } else {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case ALTERAR:
                    try {
                        cidadeRepository.save(controllerPai.cidade);
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        if (e.getMessage().contains("duplicate key")) {
                            cidadeRepository.findByNomeIgnoreCase(txtFldNome.getText());
                            alert.setHeaderText(i18n.getString("cidadeJaExiste.text"));
                            alert.setContentText(i18n.getString("cidadeJaExisteCompleto.text"));
                            alert.showAndWait();
                        } else {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case EXCLUIR:
                    if (empresaRepository.countByCidade(controllerPai.cidade) == 0) cidadeRepository.delete(controllerPai.cidade); //
                    else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setHeaderText(i18n.getString("cidadeVinculada1.text"));
                        alert.setContentText(i18n.getString("cidadeVinculada2.text"));
                        alert.showAndWait();
                    }
//                    controllerPai.tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
                    break;
                default:
                    break;
            }
            controllerPai.tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
            controllerPai.tblView.refresh();
            controllerPai.tblView.getSelectionModel().clearSelection();
            atualizaTabela();
            controllerPai.tblView.getSelectionModel().select(controllerPai.cidade);
            anchorPane.getScene().getWindow().hide();
            controllerPai.tblView.requestFocus();
        } catch (Exception e) {
            System.err.println(e.getMessage());

        }
    }
    
    public void atualizaTabela() {
        controllerPai.tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
    }
    
    public void setControllerPai(CidadeController controllerPai) {
        this.controllerPai = controllerPai;
        txtFldNome.setText(controllerPai.cidade.getNome());
        txtFldNome.setDisable(controllerPai.acao == EXCLUIR);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnConfirma.disableProperty().bind(txtFldNome.textProperty().isEmpty());
    }

}
