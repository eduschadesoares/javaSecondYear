/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javax.annotation.Resource;

/**
 *
 * @author mrgomes
 */
public class Config {
    
    public static final char INCLUIR = 'I';
    public static final char ALTERAR = 'A';
    public static final char EXCLUIR = 'E';
    
    public static final ResourceBundle i18n = ResourceBundle.getBundle(String.format("i18n.Bundle_%s", "en_US"));
    public static final DateTimeFormatter df = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
    public static final NumberFormat nfc = NumberFormat.getCurrencyInstance();
    public static final NumberFormat nf = NumberFormat.getNumberInstance();
            
}
