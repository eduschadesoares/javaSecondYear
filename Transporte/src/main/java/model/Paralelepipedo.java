/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author idomar
 */
public class Paralelepipedo extends FiguraGeo3d {

    double larg;
    double prof;

    public Paralelepipedo() {

    }

    public Paralelepipedo(double larg, double prof, double alt, Material material) {
        this.larg = larg;
        this.prof = prof;
        this.alt = alt;
        this.material = material;
    }

    public Paralelepipedo(double larg, double prof, double alt, boolean fragil, Material material) {
        this.larg = larg;
        this.prof = prof;
        this.alt = alt;
        this.fragil = fragil;
        this.material = material;
    }

    public double getLarg() {
        return larg;
    }

    public double getAreaDaBase() {
        return prof * larg;
    }

    public double getVolume() {
        return getAreaDaBase() * alt;
    }

    public void setLarg(double larg) {
        if (larg <= 0) {
            this.larg = 0.1;
        } else {
            this.larg = larg;
        }
    }

    public double getProf() {
        return prof;
    }

    public void setProf(double prof) {
        if (prof <= 0) {
            this.prof = 0.1;
        } else {
            this.prof = prof;
        }
    }

    @Override
    public String toString() {
        return "Paralelepipedo{" + "larg=" + larg + ", prof=" + prof + ", alt=" + alt + ", mat=" + material + '}';
    }

}
