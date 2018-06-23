package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PrincipalController implements Initializable {

    @FXML
    private Label lblResul;

    @FXML
    private TextField txtFldValue;

    @FXML
    private RadioButton rdBtnCelFar;

    //Não mexer na parada
    @FXML
    private void btnConvert(ActionEvent event) {
        double value;
        try {
            value = Double.parseDouble(txtFldValue.getText());

            value = convertCelToFar(value, rdBtnCelFar.isSelected());

            lblResul.setText(String.valueOf(value));
            System.out.println("You've just converted!");
            //System.out.println(String.valueOf(valor));
            
        } catch (Exception e) {
            lblResul.setText("Erro");
            System.out.println("Error!");
        }
    }

    private double convertCelToFar(double value, boolean normal) {
        if (normal) {
            return (value * 1.8 + 32);
        } else {
            return ((value - 32) / 1.8);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
