package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eduardo
 */
public class Time {

    String nome;
    int vit = 0, der = 0, emp = 0;
    int golP = 0, golN = 0;
    int cla;
    byte clas;

    List<Jogo> jogos = new ArrayList<Jogo>();

    public Time() {
    }

    public Time(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVit() {
        return vit;
    }

    public byte getClas() {
        return clas;
    }

    public void setClas(byte clas) {
        this.clas = clas;
    }

    public void setVit(int vit) {
        this.vit = vit;
    }

    public int getDer() {
        return der;
    }

    public void setDer(int der) {
        this.der = der;
    }

    public int getEmp() {
        return emp;
    }

    public void setEmp(int emp) {
        this.emp = emp;
    }

    public int getGolP() {
        return golP;
    }

    public void setGolP(int golP) {
        this.golP = golP;
    }

    public int getGolN() {
        return golN;
    }

    public void setGolN(int golN) {
        this.golN = golN;
    }

    // metodos
    public int getSaldoGols() {
        return golP - golN;
    }

    public int getPontos() {
        return (vit * 3) + emp;
    }

    public int getpartidas() {
        return vit + emp + der;
    }

    public void addVit() {
        this.vit++;
    }

    public void addDer() {
        this.der++;
    }

    public void addEmp() {
        this.emp++;
    }

    public void addGolP(byte gols) {
        this.golP += gols;
    }

    public void addGolN(byte gols) {
        this.golN += gols;
    }

    public int getCla() {
        return cla;
    }

    public void setCla(int cla) {
        if (cla >= 0) {
            this.cla = cla;
        } else {
            this.cla = 0;
        }
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    public void setJogos(List<Jogo> jogos) {
        this.jogos = jogos;
    }

    @Override
    public String toString() {
        return "Time{" + "nome=" + nome + ", vit=" + vit + ", der=" + der + ", emp=" + emp + ", golP=" + golP + ", golN=" + golN + '}';
    }

}
