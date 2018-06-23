/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static config.Config.INCLUIR;
import static config.Config.ALTERAR;
import static config.Config.EXCLUIR;
import static config.Config.i18n;
import static config.DAO.cidadeRepository;
import static config.DAO.empresaRepository;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Cidade;
import model.Empresa;
import org.springframework.data.domain.Sort;
import utility.NossoPopOver;

public class CRUDEmpresaController implements Initializable {

    private EmpresaController controllerPai;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label lblNome;

    @FXML
    private TextField txtFldRazao, txtFldFantasia, txtFldCNPJ, txtFldObs;

    @FXML
    private Button btnConfirma;

    @FXML
    public ComboBox<Cidade> cmbBxCidade;

    @FXML
    private void btnCancelaClick() {
        anchorPane.getScene().getWindow().hide();
        controllerPai.tblView.requestFocus();
    }

    @FXML
    private void btnConfirmaClick() {
        controllerPai.empresa.setNome(txtFldRazao.getText());
        controllerPai.empresa.setFantasia(txtFldFantasia.getText());
        controllerPai.empresa.setCidade(cmbBxCidade.getSelectionModel().getSelectedItem());
        controllerPai.empresa.setObservacao(txtFldObs.getText());

        empresaRepository.save(controllerPai.empresa);

        controllerPai.tblView.setItems(FXCollections.observableList(empresaRepository.findAll(new Sort(new Sort.Order("nome")))));
        controllerPai.tblView.refresh();
        anchorPane.getScene().getWindow().hide();
        controllerPai.tblView.requestFocus();
    }

    public void setControllerPai(EmpresaController controllerPai) {
        this.controllerPai = controllerPai;
        lblNome.setText(controllerPai.empresa.getNome().toUpperCase());
        txtFldFantasia.setText(controllerPai.empresa.getFantasia());
        txtFldRazao.setText(controllerPai.empresa.getNome());
        txtFldCNPJ.setText(controllerPai.empresa.getCnpj());
        txtFldObs.setText(controllerPai.empresa.getObservacao());
//        txtFldCNPJ.setDisable(true);
        cmbBxCidade.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
        cmbBxCidade.getSelectionModel().select(controllerPai.empresa.getCidade());

//        cmbBxCidade.setPromptText(controllerPai.empresa.getCidade().getNome());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnConfirma.disableProperty().bind(txtFldFantasia.textProperty().isEmpty().or(txtFldRazao.textProperty().isEmpty().or(txtFldCNPJ.textProperty().isEmpty())));
        cmbBxCidade.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")))));

        txtFldCNPJ.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldCNPJ.setText(oldValue);
                    } else {
                        txtFldCNPJ.setText(newValue);
                    }
                    btnConfirma.setDisable(true);
                });

    }

}
