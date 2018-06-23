package view;

import static config.Config.ALTERAR;
import static config.Config.EXCLUIR;
import static config.Config.INCLUIR;
import static config.Config.i18n;
import static config.DAO.cidadeRepository;
import static config.DAO.empresaRepository;
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

public class CRUDCidadeController implements Initializable {

    public CidadeController controllerPai;

    public Cidade cidade = null;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField txtFldNome, txtFldUf;

    @FXML
    private Button btnConfirma;

    @FXML
    private void btnCancelaClick() {
        anchorPane.getScene().getWindow().hide();
    }

    @FXML
    private void btnConfirmaClick() {
        controllerPai.cidade.setNome(txtFldNome.getText());
        controllerPai.cidade.setUf(txtFldUf.getText());

        try {
            switch (controllerPai.acao) {
                case INCLUIR:
                    try {
                        cidadeRepository.insert(new Cidade(txtFldNome.getText(), txtFldUf.getText()));
                    } catch (Exception e) {
                        if (e.getMessage().contains("duplicate key")) {
                            cidadeRepository.findByNomeIgnoreCase(txtFldNome.getText());
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setHeaderText(i18n.getString("cidJaExiste.text"));
                            alert.setContentText(i18n.getString("cidJaExiste.text"));
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
                            alert.setHeaderText(i18n.getString("cidJaExiste.text"));
                            alert.setContentText(i18n.getString("cidJaExiste.text"));
                            alert.showAndWait();
                        } else {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case EXCLUIR:
                    if (empresaRepository.countByCidade(controllerPai.cidade) == 0) {
                        cidadeRepository.delete(controllerPai.cidade);
                        break;
                    }
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(i18n.getString("cidVinculada.text"));
                    alert.setContentText(i18n.getString("cidVinculada.text"));
                    alert.showAndWait();
//                    controllerPai.tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
                    break;
                default:
                    break;
            }
            controllerPai.tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(new Sort(new Sort.Order("nome")))));
            controllerPai.tblView.refresh();
            controllerPai.tblView.getSelectionModel().clearSelection();
            controllerPai.tblView.getSelectionModel().select(controllerPai.cidade);
            anchorPane.getScene().getWindow().hide();
            controllerPai.tblView.requestFocus();
        } catch (Exception e) {
            System.err.println(e.getMessage());

        }
    }

    public void setControllerPai(CidadeController controllerPai) {
        this.controllerPai = controllerPai;
        txtFldNome.setText(controllerPai.cidade.getNome());
        txtFldUf.setText(controllerPai.cidade.getUf());
        txtFldNome.setDisable(controllerPai.acao == EXCLUIR);
        txtFldUf.setDisable(controllerPai.acao == EXCLUIR);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        btnConfirma.disableProperty().bind(txtFldRA.textProperty().isEmpty().or(txtFldNome.textProperty().isEmpty().or(txtFldEmail.textProperty().isEmpty().or(txtFldLogin.textProperty().isEmpty().or(txtFldNaturalidade.textProperty().isEmpty())))));
        btnConfirma.disableProperty().bind(txtFldNome.textProperty().isEmpty().or(txtFldUf.textProperty().isEmpty()));
//        btnConfirma.disableProperty().bind(txtFldUf.textProperty().isEmpty());

    }

}
