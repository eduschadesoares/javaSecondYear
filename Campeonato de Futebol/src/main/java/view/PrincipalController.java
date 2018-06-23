package view;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Jogo;
import model.Time;
import utilit.Dados;

public class PrincipalController implements Initializable {

    @FXML
    private TableView<Time> tblVwTimes;

    @FXML
    public Label lblNomeTime;

    @FXML
    public StackPane pnJogos;

    @FXML
    public TableView<Jogo> tblVwJogo;

    @FXML
    public RadioButton rdBtnTodos;

    @FXML
    public Button btnSalvarJsonCampClick;

    private Time timeSel;
    public List<Jogo> listTemp = new ArrayList<Jogo>();

    @FXML
    private void btnAbrirClick() {
//        final Stage stage = null;
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Escolha o seu arquivo:");
//        fileChooser.setInitialDirectory(new File("Partidas\\"));

        dados = new Dados(abrirArquivo());
//        lstPrinc = dados.ler();

        tblVwTimes.setItems(FXCollections.observableList(dados.ler()));
        btnSalvarJsonCampClick.setDisable(false);
    }

    @FXML
    private void btnFecharProgClick() {
        clearDirectory();
        System.exit(0);

    }

    @FXML
    private void btnSalvarJsonCamp() {
        String nomeArq;
        nomeArq = "JSON\\Campeonato.json";
        dados.gravarJogoCamp(nomeArq);
    }
    
    @FXML
    private void btnCriarCampClick() {
        
        
        
    }

    @FXML
    private void tblVwClick(Event event) {
        MouseEvent me = null;
        if (event.getEventType() == MOUSE_CLICKED) {
            me = (MouseEvent) event;
            if (me.getClickCount() == 2) {
                pnJogos.setVisible(true);
                rdBtnTodos.setSelected(true);
                timeSel = tblVwTimes.getSelectionModel().getSelectedItem();
                lblNomeTime.setText(timeSel.getNome());

                tblVwJogo.setItems(FXCollections.observableList(timeSel.getJogos()));
                System.out.println(timeSel.getJogos());
            }
        }
    }

    @FXML
    private void btnFecharTimeClick() {
        pnJogos.setVisible(false);
        tblVwTimes.requestFocus();
    }

    @FXML
    private void btnAbrirJsonClick() {
        timeSel.setJogos(dados.lerJogoTime(timeSel));
        tblVwJogo.setItems(FXCollections.observableList(timeSel.getJogos()));
        rdBtnTodos.setSelected(true);
    }

    @FXML
    private void btnSalvarJsonJogo() {
        dados.gravarJogosTime(timeSel);
    }

    @FXML
    private void btnLimparListaClick() {
        timeSel.getJogos().clear();
        tblVwJogo.setItems(FXCollections.observableList(timeSel.getJogos()));
        System.out.println("Lista foi limpa com sucesso!");

    }

    @FXML
    private void rdBtnFiltroTodos() {
        tblVwJogo.setItems(FXCollections.observableList(timeSel.getJogos()));
    }

    @FXML
    private void rdBtnFiltroVit() {
        listTemp.clear();
        for (Jogo j : timeSel.getJogos()) {
            if (j.getTimeA().equals(timeSel.getNome()) && j.getGolA() > j.getGolB()) {
                listTemp.add(j);
            }
        }
        tblVwJogo.setItems(FXCollections.observableList(listTemp));
        tblVwJogo.refresh();
    }

    @FXML
    private void rdBtnFiltroDer() {
        listTemp.clear();
        for (Jogo j : timeSel.getJogos()) {
            if (j.getTimeA().equals(timeSel.getNome()) && j.getGolA() < j.getGolB()) {
                listTemp.add(j);
            }
        }
        tblVwJogo.setItems(FXCollections.observableList(listTemp));
        tblVwJogo.refresh();
    }

    @FXML
    private void rdBtnFiltroEmp() {
        listTemp.clear();
        for (Jogo j : timeSel.getJogos()) {
            if (j.getGolA() == j.getGolB()) {
                listTemp.add(j);
            }
        }
        tblVwJogo.setItems(FXCollections.observableList(listTemp));
        tblVwJogo.refresh();
    }

    public String abrirArquivo() {
        final Stage stage = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o seu arquivo:");
        fileChooser.setInitialDirectory(new File("Partidas\\"));
        return String.valueOf(fileChooser.showOpenDialog(stage));
    }

    public void clearDirectory() {
        try {
            File folder = new File("JSON\\");
            if (folder.isDirectory()) {
                File[] sun = folder.listFiles();
                for (File toDelete : sun) {
                    toDelete.delete();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private Dados dados;

    private List<Time> lstPrinc = new ArrayList<Time>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pnJogos.setVisible(false);

    }
}
