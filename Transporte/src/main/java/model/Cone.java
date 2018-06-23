/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Idomar
 */
public class Cone extends FiguraGeo3d {

    double diametro, raio;
    final double pi = 3.14;

    public Cone(double diametro, double alt, Material material) {
        this.setDiametro(diametro);
        this.setAlt(alt);
        this.setMaterial(material);
        this.setRaio(raio);
    }

    public Cone(double diametro, double alt, boolean fragil, Material material) {
        this.setDiametro(diametro);
        this.setAlt(alt);
        this.setMaterial(material);
        this.setRaio(raio);
        this.fragil = fragil;
    }

    public double getDiametro() {
        return diametro;
    }

    public void setDiametro(double diametro) {
        this.diametro = diametro;
    }

    public void setRaio(double raio) {
        this.raio = getDiametro() / 2;
    }

    public double getAreaDaBase() {
        return pi * (raio * raio);
    }

    public double getVolume() {
        return (getAreaDaBase() * getAlt()) / 3;
    }
}
