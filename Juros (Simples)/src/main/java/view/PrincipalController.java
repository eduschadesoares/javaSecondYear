package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrincipalController implements Initializable {

    @FXML
    private Label lblResultSaldoFinal;

    @FXML
    private Label lblResultTtlDepositado;

    @FXML
    private Label lblResultJurosFinais;

    @FXML
    private Button btnCalcular;

    @FXML
    private TextField txtFldVlrInicial;

    @FXML
    private TextField txtFldVlrMensal;

    @FXML
    private TextField txtFldNumMeses;

    @FXML
    private TextField txtFldTxJrs;

    @FXML
    private void btnClickCalcularJuros(ActionEvent event) {
        double inicial, mensal, meses, juros, vFinal, vDepositado, rendimento;
        try {
            inicial = Double.parseDouble(txtFldVlrInicial.getText());
            mensal = Double.parseDouble(txtFldVlrMensal.getText());
            meses = Double.parseDouble(txtFldNumMeses.getText());
            juros = Double.parseDouble(txtFldTxJrs.getText());
            vFinal = calculoJuros(inicial, mensal, meses, juros);
            vDepositado = totalDepositado(inicial, mensal, meses);
            rendimento = vFinal - vDepositado;
            lblResultSaldoFinal.setText(String.valueOf(String.format("%.2f", vFinal)));
            lblResultTtlDepositado.setText(String.valueOf(String.format("%.2f", vDepositado)));
            lblResultJurosFinais.setText(String.valueOf(String.format("%.2f", rendimento)));

         //   lblResultSaldoFinal.setText(String.valueOf(calculoJuros(inicial, mensal, mensal, juros)));
        //    lblResultTtlDepositado.setText(String.valueOf(totalDepositado(inicial, mensal, meses)));

        } catch (NumberFormatException e) {
            lblResultSaldoFinal.setText("Erro ao converter");
            lblResultTtlDepositado.setText(" ");
            lblResultTtlDepositado.setText(" ");
        }

    }

    public double calculoJuros(double inicial, double mensal, double meses, double juros) {
        for (int i = 0; i < meses; i++) {
            inicial = inicial + (inicial * (juros / 100)) + mensal;
        }
        inicial = inicial + (inicial * (juros / 100));
        return inicial;
    }

    public double totalDepositado(double inicial, double mensal, double meses) {
        return inicial + (mensal * meses);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
