package model;

/**
 *
 * @author eduardo
 */
public class Jogo {

    public String timeA, timeB;
    public byte golA, golB, pontos;

    public String getTimeA() {
        return timeA;
    }

    public void setTimeA(String timeA) {
        this.timeA = timeA;
    }

    public String getTimeB() {
        return timeB;
    }

    public void setTimeB(String timeB) {
        this.timeB = timeB;
    }

    public byte getGolA() {
        return golA;
    }

    public void setGolA(byte golA) {
        this.golA = golA;
    }

    public byte getGolB() {
        return golB;
    }

    public void setGolB(byte golB) {
        this.golB = golB;
    }

    public Jogo() {
    }

    public Jogo(String timeA, byte golA, String timeB, byte golB) {
        this.timeA = timeA.toUpperCase();
        this.timeB = timeB.toUpperCase();
        this.golA = golA; // se golA fosse String: this.golA = Byte.parseByte(golA)
        this.golB = golB;
    }

    public Jogo(String timeA, String golA, String timeB, String golB) {
        this.timeA = timeA.toUpperCase();
        this.timeB = timeB.toUpperCase();
        this.golA = Byte.parseByte(golA); // se golA fosse String: this.golA = Byte.parseByte(golA)
        this.golB = Byte.parseByte(golB);
    }

    public Jogo(String linha) {
        String[] partes = linha.split("\\,");

        this.timeA = partes[0].toUpperCase();
        this.golA = Byte.parseByte(partes[1]);
        this.timeB = partes[2].toUpperCase();
        this.golB = Byte.parseByte(partes[3]);
    }

    public byte getPontos() {
        return pontos;
    }

    public void setPontos(byte pontos) {
        this.pontos = pontos;
    }

    @Override
    public String toString() {
        return "Jogo{" + "timeA=" + timeA + ", timeB=" + timeB + ", golA=" + golA + ", golB=" + golB + ", pontos=" + pontos + '}';
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
