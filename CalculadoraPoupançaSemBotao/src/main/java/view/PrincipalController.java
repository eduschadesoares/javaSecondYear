package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrincipalController implements Initializable {

    @FXML
    private TextField txtFldValorInicial;

    @FXML
    private TextField txtFldMensal;

    @FXML
    private TextField txtFldParcelas;

    @FXML
    private TextField txtFldJuros;

    @FXML
    private Label lblFinalProjetado;

    @FXML
    private Label lblTotalInvestido;

    @FXML
    private Label lblTotalGanho;

    private void calculaValor(boolean preenchido) {
        if (preenchido) {
            double inicial, mensal, juros, projetado, investido, ganho;
            int parcelas;
            try {
                inicial = Double.parseDouble(txtFldValorInicial.getText());
                mensal = Double.parseDouble(txtFldMensal.getText());
                juros = Double.parseDouble(txtFldJuros.getText());
                parcelas = Integer.parseInt(txtFldParcelas.getText());
                projetado = valorFinalProjetado(inicial, mensal, juros, parcelas);
                investido = valorTotalInvestido(inicial, mensal, parcelas);
                ganho = projetado - investido;
                lblFinalProjetado.setText(String.valueOf(String.format("%.2f", projetado)));
                lblTotalInvestido.setText(String.valueOf(String.format("%.2f", investido)));
                lblTotalGanho.setText(String.valueOf(String.format("%.2f", ganho)));
            } catch (NumberFormatException e) {
                lblFinalProjetado.setText(" ");
                lblTotalInvestido.setText(" ");
                lblTotalGanho.setText(" ");
            }
        }
        else{
            lblFinalProjetado.setText(" ");
            lblTotalInvestido.setText(" ");
            lblTotalGanho.setText(" ");
        }
    }

    private double valorFinalProjetado(double inicial, double mensal, double juros, int parcelas) {
        int i;
        for (i = 0; i < parcelas; i++) {
            inicial = inicial + (inicial * (juros / 100)) + mensal;
        }
        inicial = inicial + (inicial * (juros / 100));
        return inicial;
    }

    private double valorTotalInvestido(double inicial, double mensal, int parcelas) {
        return inicial + (mensal * parcelas);
    }

    private boolean testaEntradas() {
        return (!txtFldValorInicial.getText().isEmpty()
                && !txtFldMensal.getText().isEmpty()
                && !txtFldParcelas.getText().isEmpty()
                && !txtFldJuros.getText().isEmpty());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtFldValorInicial.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldValorInicial.setText(oldValue);
                    } else {
                        txtFldValorInicial.setText(newValue);
                    }
                    calculaValor(testaEntradas());
                });
        txtFldMensal.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldMensal.setText(oldValue);
                    } else {
                        txtFldMensal.setText(newValue);
                    }
                    calculaValor(testaEntradas());
                });
        txtFldParcelas.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d*?")
                    && !newValue.isEmpty()) {
                        txtFldParcelas.setText(oldValue);
                    } else {
                        txtFldParcelas.setText(newValue);
                    }
                    calculaValor(testaEntradas());
                });
        txtFldJuros.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches(
                            "\\d{1}(\\"
                            + "."
                            + "\\d*)?")
                    && !newValue.isEmpty()) {
                        txtFldJuros.setText(oldValue);
                    } else {
                        txtFldJuros.setText(newValue);
                    }
                    calculaValor(testaEntradas());
                });
    }
}
