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
public abstract class FiguraGeo3d {

    String id;
    double alt;
    boolean fragil = false;
    Material material;
    
    abstract public double getAreaDaBase();
    abstract public double getVolume();


    public double getAlt() {
        return alt;
    }

    public double getPeso() {
        return getVolume() * material.getDensidade() ;
    }

    public void setAlt(double alt) {
        if (alt <= 0) {
            this.alt = 0.1;
        } else {
            this.alt = alt;
        }
    }

    public boolean isFragil() {
        return fragil;
    }

    public void setFragil(boolean fragil) {
        this.fragil = fragil;
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public String getTipo(){        
        return this.getClass().getName();        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }  

}
