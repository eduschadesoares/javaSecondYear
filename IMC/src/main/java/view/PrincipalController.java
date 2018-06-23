package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PrincipalController implements Initializable {

    @FXML
    private Label lblResult;

    @FXML
    private Label lblClass;

    @FXML
    private TextField txtFldAltura;

    @FXML
    private TextField txtFldPeso;

    @FXML
    private RadioButton rdBtnGenero;
    
    @FXML
    private Button btnCalc;
    
    private double calculoImc(double altura, double peso) {
        return peso / (altura * altura);
    }

    private void classificacaoImc() {
        double IMC;
        IMC = calculoImc(Double.parseDouble(txtFldAltura.getText()), Double.parseDouble(txtFldPeso.getText()));

        lblResult.setText(String.valueOf(String.format("%.2f", IMC)));

        if ((IMC <= 20.7 && rdBtnGenero.isSelected()) || (IMC <= 19.1 && !rdBtnGenero.isSelected())) {
            lblClass.setText("Abaixo do peso");
        } else if ((IMC <= 26.4 && rdBtnGenero.isSelected()) || (IMC <= 25.8 && !rdBtnGenero.isSelected())) {
            lblClass.setText("Peso Normal");
        } else if ((IMC <= 27.8 && rdBtnGenero.isSelected()) || (IMC <= 27.3 && !rdBtnGenero.isSelected())) {
            lblClass.setText("Pouco acima do peso");
        } else if ((IMC <= 31.1 && rdBtnGenero.isSelected()) || (IMC <= 32.3 && !rdBtnGenero.isSelected())) {
            lblClass.setText("Acima do peso");
        } else {
            lblClass.setText("Obeso");
        }
    }

    @FXML
    private void btnClick(ActionEvent event) {
        classificacaoImc();
    }

    private boolean desativaBtn(){
        return (txtFldAltura.getText().isEmpty() || (txtFldPeso.getText().isEmpty()));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                  //  btnCalc()
                });
        
        txtFldPeso.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldPeso.setText(oldValue);
                    } else {
                        txtFldPeso.setText(newValue);
                    }
                });
                
    }
}
