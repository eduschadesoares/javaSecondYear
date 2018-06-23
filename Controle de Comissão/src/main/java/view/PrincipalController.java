package view;

import config.Config;
import static config.Config.df;
import static config.DAO.cidadeRepository;
import static config.DAO.docFiscalRepository;
import static config.DAO.vendedorRepository;
import static config.DAO.empresaRepository;
import static config.DAO.empresaRepository;
import static config.DAO.vendedorEmpresaRepository;
import static config.DAO.vendedorRepository;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.Vendedor;
import model.Empresa;
import model.Cidade;
import model.DocumentoFiscal;
import model.VendedorEmpresa;
import java.util.Random;

public class PrincipalController implements Initializable {
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}
