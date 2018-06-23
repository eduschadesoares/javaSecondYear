/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mrgomes
 */
@Document
public class Vendedor {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nome;

    public double percentualBonus = 1.5;
    
    public Vendedor() {
    }

    public Vendedor(String nome) {
        setNome(nome);
    }
    
    public Vendedor(String nome, double percentualBonus) {
        setNome(nome);
        setPercentualBonus(percentualBonus);
    }
    
    public String getId(){
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPercentualBonus() {
        return percentualBonus;
    }

    public void setPercentualBonus(double percentualBonus) {
        this.percentualBonus = percentualBonus;
    }

    
    
    @Override
    public String toString() {
        return nome;
    }
    
    

}
