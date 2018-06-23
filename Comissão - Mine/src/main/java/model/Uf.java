package model;

import java.util.Objects;
import javax.management.openmbean.ArrayType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Uf {

    @Id
    private String id;
    
    private String nome;

    private String gentilico;

    @Indexed(unique = true)
    private char[] Uf;

    public Uf() {
    }

    public Uf(String nome, String gentilico, String Uf) {
        setNome(nome);
        setGentilico(gentilico);
        setUf(Uf);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGentilico() {
        return gentilico;
    }

    public void setGentilico(String gentilico) {
        this.gentilico = gentilico;
    }

    public char[] getUf() {
        return Uf;
    }

    public void setUf(String uf) {
        String uf2 = uf.toString();
        uf2 = uf2.toUpperCase();
        Uf = uf2.toCharArray();
        this.Uf = Uf;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Uf other = (Uf) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
