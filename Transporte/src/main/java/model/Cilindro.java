package model;

public class Cilindro extends FiguraGeo3d {

    double raio;
    final double pi = 3.14;

    public Cilindro() {
    }

    public Cilindro(double raio, double alt, Material material) {
        this.raio = raio;
        this.setAlt(alt);
        this.setMaterial(material);
    }

    public Cilindro(double raio, double alt, boolean fragil, Material material) {
        this.raio = raio;
        this.setAlt(alt);
        this.fragil = fragil;
        this.setMaterial(material);
    }

    @Override
    public String toString() {
        return "Cilindro{" + "raio=" + raio + ", altura=" + getAlt() + ", pi=" + pi + ", material=" + getMaterial() + '}';
    }

    public double getRaio() {
        return raio;
    }

    public void setRaio(double raio) {
        if (raio <= 0) {
            this.raio = 0.1;
        } else {
            this.raio = raio;
        }
    }

    public double getAreaDaBase() {

        return pi * (raio * raio);
    }

    public double getAreaLateral() {

        return (2 * pi * raio) * getAlt();
    }

    public double getAreaTotal() {

        return getAreaLateral() + (2 * getAreaDaBase());
    }

    public double getVolume() {
        return getAreaDaBase() * getAlt();
    }

}
