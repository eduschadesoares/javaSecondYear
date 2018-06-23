/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import static config.Config.i18n;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;

/**
 *
 * @author mrgomes
 */
public class NossoPopOver {

    private FXMLLoader loader;

    public NossoPopOver(String arquivoFXML,
            String titulo, Node node) {
        try {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(arquivoFXML));
            loader.setResources(i18n);

            if (node == null) {
                Scene scene = new Scene((Parent) loader.load());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle(titulo);
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
                PopOver popOverCRUD = new PopOver();
                popOverCRUD.setContentNode((Node) loader.load());
                popOverCRUD.setAutoFix(true);
                popOverCRUD.setAutoHide(false);
                popOverCRUD.setHideOnEscape(true);
                popOverCRUD.setHeaderAlwaysVisible(true);
                popOverCRUD.setArrowLocation(
                        PopOver.ArrowLocation.TOP_LEFT);
                popOverCRUD.setTitle(titulo);
                popOverCRUD.show(node);

            }

        } catch (IOException ex) {
            Logger.getLogger(NossoPopOver.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public FXMLLoader getLoader() {
        return loader;
    }

}
