package model;

import java.util.ArrayList;
import java.util.List;

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
