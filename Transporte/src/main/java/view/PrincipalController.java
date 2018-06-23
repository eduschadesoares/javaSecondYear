package view;

import static com.sun.javafx.util.Utils.split;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.Cilindro;
import model.Cone;
import model.FiguraGeo3d;
import model.Material;
import model.Paralelepipedo;

public class PrincipalController implements Initializable {

    private List<Material> lstMaterial = new ArrayList<Material>();
    private List<FiguraGeo3d> lstObj = new ArrayList<FiguraGeo3d>();
    private List<String> lstFigura = new ArrayList<String>();

    @FXML
    private ComboBox cmbMaterial;

    @FXML
    private TextField txtFldAltura;

    @FXML
    private TextField txtFldSegundo;

    @FXML
    private TextField txtFldProfundidade;

    @FXML
    private TextField txtFldId;

    @FXML
    private RadioButton rdBtnParalelepipedo;

    @FXML
    private RadioButton rdBtnCilindro;

    @FXML
    private RadioButton rdBtnCone;

    @FXML
    private Label lblSegundo;

    @FXML
    private Label lblNumObj;

    @FXML
    private Label lblNomeObj;

    @FXML
    private Label lblPesoMaisPesado;
    
    @FXML 
    private Label lblId;

    @FXML
    private VBox vBxTerceiro;

    @FXML
    private Label lblPesoTotal;

    @FXML
    private CheckBox cbxFragil;

    @FXML
    private Label lblCustoTotal;

    @FXML
    private Button btnAdicionar;

    @FXML
    private void btnRemoverPrimeiroClick(ActionEvent event) {
        if (lstObj.size() > 0) {

            lstObj.remove(0);
            mostraDados();
        }
    }

    @FXML
    private void btnAdicionarClick(ActionEvent event) {
        double altura = Double.parseDouble(txtFldAltura.getText());
        Material mat = (Material) cmbMaterial.getSelectionModel().getSelectedItem();

        try {
            if (rdBtnParalelepipedo.isSelected()) {
                double largura = Double.parseDouble(txtFldSegundo.getText());
                double profundidade = Double.parseDouble(txtFldProfundidade.getText());
                Paralelepipedo p = new Paralelepipedo(largura, profundidade, altura, cbxFragil.isSelected(), mat);
                lstObj.add(p);
                txtFldId.getText();
            } else if (rdBtnCilindro.isSelected()) {
                double raio = Double.parseDouble(txtFldSegundo.getText());
                Cilindro c = new Cilindro(raio, altura, cbxFragil.isSelected(), mat);
                txtFldId.getText();
                lstObj.add(c);
            } else if (rdBtnCone.isSelected()) {
                double raio = Double.parseDouble(txtFldSegundo.getText());
                Cone c = new Cone(raio, altura, cbxFragil.isSelected(), mat);
                txtFldId.getText();
                lstObj.add(c);
            }
        } catch (NumberFormatException e) {
            System.out.println("Valores inseridos são inválidos");
        }

        mostraDados();
        limpaCampos();

    }

    @FXML
    private void rdBtnParalelepipedoClick(ActionEvent event
    ) {
        lblSegundo.setText("Largura");
        vBxTerceiro.setVisible(true);

    }

    @FXML
    private void rdBtnCilindroConeClick(ActionEvent event
    ) {
        lblSegundo.setText("Raio");
        vBxTerceiro.setVisible(false);
    }

    @FXML
    private void cmbMaterialClick(ActionEvent event
    ) {
        Material matSel;
        matSel = (Material) cmbMaterial.getSelectionModel().getSelectedItem();
        //System.out.println(matSel);
    }

    private FiguraGeo3d maisPesado() {
        if (lstObj.size() > 0) {
            FiguraGeo3d pesado = lstObj.get(0);
            for (FiguraGeo3d f : lstObj) {
                if (f.getPeso() > pesado.getPeso()) {
                    pesado = f;
                }
            }
            return pesado;

        } else {
            return null;
        }
    }

    void limpaCampos() {
        txtFldAltura.clear();
        txtFldProfundidade.clear();
        txtFldSegundo.clear();
        txtFldId.clear();
        txtFldAltura.requestFocus();
        cbxFragil.setSelected(false);
    }

    double pesoTotal() {
        double total = 0;

        for (FiguraGeo3d f : lstObj) {
            total += f.getPeso();
        }
        return total;
    }

    double custoFrete(FiguraGeo3d obj) {
        double custoBase = 1.5;

        if (obj.isFragil()) {
            custoBase *= 1.25;
        }
        if (obj.getVolume() > 5 && obj.getVolume() < 10) {
            custoBase *= 1.3;
        } else if (obj.getVolume() > 10) {
            custoBase *= 2;
        }

        return custoBase * obj.getPeso();
    }

    double custoTransporte() {
        double custoTotal = 0;
        for (FiguraGeo3d f : lstObj) {
            custoTotal += custoFrete(f);
        }
        return custoTotal;
    }

    private boolean desativaBotao() {
        String padraoId = "([A-Z]|[a-z]){2}\\d{9}([A-Z]|[a-z]){2}?";

        if (vBxTerceiro.isVisible()) {
            return (txtFldAltura.getText().isEmpty()
                    || txtFldProfundidade.getText().isEmpty()
                    || txtFldSegundo.getText().isEmpty()
                    || !txtFldId.getText().matches(padraoId));
        } else {
            return (txtFldAltura.getText().isEmpty()
                    || txtFldSegundo.getText().isEmpty()
                    || !txtFldId.getText().matches(padraoId));
        }
    }

void mostraDados() {
        if (lstObj.size() > 0) {
            lblNumObj.setText(String.valueOf(lstObj.size()));

            FiguraGeo3d pes = maisPesado();

            String s = pes.getClass().getName();

            String[] nome = s.split("\\.");
            lblNomeObj.setText(nome[1]);
            lblPesoMaisPesado.setText(String.valueOf(pes.getPeso()));

            lblPesoTotal.setText(String.valueOf(pesoTotal()));
            lblCustoTotal.setText(String.valueOf(custoTransporte()));
            lblId.setText(txtFldId.getText());
            //System.out.println(pes.getId());
        }
    }

    private void listeners() {
        txtFldAltura.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldAltura.setText(oldValue);
                    } else {
                        txtFldAltura.setText(newValue);
                    }
                    btnAdicionar.setDisable(desativaBotao());
                });

        txtFldProfundidade.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldProfundidade.setText(oldValue);
                    } else {
                        txtFldProfundidade.setText(newValue);
                    }
                    btnAdicionar.setDisable(desativaBotao());
                });

        txtFldSegundo.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldSegundo.setText(oldValue);
                    } else {
                        txtFldSegundo.setText(newValue);
                    }
                    btnAdicionar.setDisable(desativaBotao());
                });

        txtFldId.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "^([A-Z]||[a-z])("
                            + "([A-Z]||[a-z])("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "\\d("
                            + "([A-Z]||[a-z])("
                            + "([A-Z]||[a-z]))?)?)?)?)?)?)?)?)?)?)?)?"
                    )
                    && !newValue.isEmpty() || (newValue.matches("\\D\\d") || newValue.matches("\\d"))) {
                        txtFldId.setText(oldValue.toUpperCase());
                    } else {
                        txtFldId.setText(newValue.toUpperCase());
                    }
                    btnAdicionar.setDisable(desativaBotao());
                });
    }

    @Override
        public void initialize(URL url, ResourceBundle rb) {

        lstMaterial.add(new Material("Aço", 7.5));
        lstMaterial.add(new Material("Aluminio", 1.5));
        lstMaterial.add(new Material("Madeira", 0.1));
        lstMaterial.add(new Material("Gesso", 0.3));

        cmbMaterial.setItems(FXCollections.observableList(lstMaterial));
        cmbMaterial.getSelectionModel().selectFirst();

        listeners();
        mostraDados();
    }
}
