package view;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import config.Config;
import static config.Config.ALTERAR;
import static config.Config.EXCLUIR;
import static config.Config.INCLUIR;
import static config.Config.df;
import static config.Config.i18n;
import static config.Config.nf;
import static config.Config.nfc;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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
import org.controlsfx.control.textfield.TextFields;
import org.springframework.dao.support.DaoSupport;
import org.springframework.data.domain.Sort;
import repository.DocumentoFiscalRepository;
import repository.EmpresaRepository;
import repository.CidadeRepository;
import utility.NossoPopOver;

public class CidadeController implements Initializable {

    public char acao;
    public Cidade cidade;

    @FXML
    public TableView<Cidade> tblView;

    @FXML
    public TextField txtFldFiltro;

    @FXML
    public Button btnAddCid, btnRemCid, btnEditCid, btnAtualiza, btnAlertaCid;

    @FXML
    private void onKeyPressFiltrar(KeyEvent ev) throws ParseException {
        //Faz um auto-complete no textfield procurando as empresas relacionadas
/*        TextFields.bindAutoCompletion(txtFldFiltro, cidadeRepository.findByNomeLikeIgnoreCase(
                txtFldFiltro.getText()));
         */

        //ESPERA A TECLA ENTER SER PRESSIONADA PARA FAZER A BUSCA DA CIDADE
//        if (ev.getCode() == KeyCode.ENTER) {
        tblView.setItems(FXCollections.observableList(cidadeRepository.findByNomeLikeIgnoreCase(
                txtFldFiltro.getText())));
//        }

        /*            try {
            if (ev.getText().isEmpty()) {
                cidadeRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "nome")));
            } else {
                cidadeRepository.findByNomeLikeIgnoreCase(txtFldFiltro.getText());
            }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        if (ev.getCode() == KeyCode.F5) {
            atualizaTableView();
        } */
    }

    @FXML
    public void acAtualizarCidade(Event event) {
        MouseEvent me = null;

        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;

            if ((me.getClickCount() == 2) && !tblView.getSelectionModel().isEmpty()) {
                acAlertarCid();
//                acEditar();
            } else if (me.getButton() == MouseButton.SECONDARY) //CLick botão Direito
            {
                acExcluir();
            }

        }
    }

    @FXML
    private void acIncluir() {
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

    @FXML
    private void acAtualizar() {
        atualizaTableView();
    }

    @FXML
    private void acAlertarCid() {
        try {
            //Seleciona a cidade do item que foi selecionado na TableView
            cidade = tblView.getSelectionModel().getSelectedItem();
            //Pega a qtde de empresas emcontradas no empresaRepository pesquisadas pela cidade
            //Passar qtdeEmpresaCidade por um .toString() para converter de int para string 
            Integer qtdeEmpresaCidade = empresaRepository.countByCidade(cidade);

            int qtdeNotas = 0;
            //Valor notas por cidade
            double valorTodasAsNotas = 0;

            //Lista de empresas encontradas pelo parametro cidade FindByCidade
            for (Empresa e : empresaRepository.findByCidade(cidade)) {
                //Lista de documento fiscal encontradas pelo parâmetro empresa FindByEmpresa
                for (DocumentoFiscal doc : docFiscalRepository.findByEmpresa(e)) {
                    valorTodasAsNotas = valorTodasAsNotas + doc.getValorNota();
                }
                //Pega o tamanho da lista docFiscal e vai incrementando para dizer a qtde de pedidos em uma cidade
                qtdeNotas = qtdeNotas + docFiscalRepository.findByEmpresa(e).size();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(cidade.getNome().toUpperCase());

            if (qtdeEmpresaCidade == 0) {
                alert.setContentText("Não existem empresas nessa cidade!");
            } else if (qtdeEmpresaCidade == 1) {
                alert.setContentText("Existe " + qtdeEmpresaCidade.toString() + " empresa em " + cidade + ("\n")
                        + "Foi realizados " + qtdeNotas + " pedidos." + ("\n")
                        + "Foi vendido R$" + String.format("%.2f", valorTodasAsNotas) + " nessa cidade.");
            } else {
                alert.setContentText("Existe " + qtdeEmpresaCidade.toString() + " empresa em " + cidade + ("\n")
                        + "Foi realizados " + qtdeNotas + " pedidos." + ("\n")
                        + "Foi vendido R$" + String.format("%.2f", valorTodasAsNotas) + " nessa cidade.");
            }

            alert.showAndWait();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void atualizaTableView() {
        tblView.setItems(FXCollections.observableList(cidadeRepository.findAll(
                new Sort(
                        new Sort.Order(Sort.Direction.ASC, "nome"))
        )));
        tblView.refresh();
        tblView.getSelectionModel().selectFirst();
    }

    private void showCRUD() {
        String scene = "/fxml/CRUDCidade.fxml";
        NossoPopOver popOver = null;

        switch (acao) {
            case INCLUIR:
                popOver = new NossoPopOver(scene, i18n.getString("addCid.text"), null);
                break;
            case EXCLUIR:
                popOver = new NossoPopOver(scene, i18n.getString("remCid.text"), null);
                break;
            case ALTERAR:
                popOver = new NossoPopOver(scene, i18n.getString("editCid.text"), null);
                break;
        }

        CRUDCidadeController controller = popOver.getLoader().getController();
        controller.setControllerPai(this);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atualizaTableView();
    }
}
