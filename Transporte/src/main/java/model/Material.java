/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author idomar
 */
public class Material {

    String nome;
    double densidade;

    public Material() {

    }

    public Material(String nome, double densidade) {
        this.nome = nome;
        this.densidade = densidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome.isEmpty()) {
            this.nome = "vazio";
        } else {
            this.nome = nome;
        }
    }

    public double getDensidade() {
        return densidade;
    }

    public void setDensidade(double densidade) {
        this.densidade = densidade;
    }

    private List<Material> lstMaterial = new ArrayList<Material>();

    @Override
    public String toString() {
        return nome;
    }

}
