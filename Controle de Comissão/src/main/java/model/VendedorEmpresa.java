/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import static config.Config.nf;
import static config.Config.nfc;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author mrgomes
 */

@Document
@CompoundIndexes({
        @CompoundIndex(
            name = "vendedorEmpresa_idx",
            def = "{'vendedor': 1, 'empresa': 1}", unique = true)
})
public class VendedorEmpresa {
    
    @Id
    private String id;
    
    @DBRef
    Vendedor vendedor;
    
    @DBRef
    Empresa empresa;
    
    private int numNotas;
    private double valorTotal;
    private double valorComissao;

    public VendedorEmpresa() {
    }

    public VendedorEmpresa(Vendedor vendedor, Empresa empresa, int numNotas, double valorTotal, double valorComissao) {
        setVendedor(vendedor);
        setEmpresa(empresa);
        setNumNotas(numNotas);
        setValorTotal(valorTotal);
        setValorComissao(valorComissao);
    }

    public String getId() {
        return id;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }
    
    public String getEmpresaFantasia(){
        if(empresa != null) {
            return empresa.getFantasia();
        } else return "";
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public int getNumNotas() {
        return numNotas;
    }

    public void setNumNotas(int numNotas) {
        this.numNotas = numNotas;
    }

    public double getValorTotal() {
        return valorTotal;
    }
    
    public String getFormatValorTotal() {
        return nfc.format(valorTotal);
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorComissao() {
        return valorComissao;
    }
    
    public String getFormatValorComissao() {
        return nfc.format(valorComissao);
    }

    public void setValorComissao(double valorComissao) {
        this.valorComissao = valorComissao;
    }
    
    public void addNumNotas(){
        this.numNotas++;
    }
    
    public void addValorTotal(double valorTotal){
        this.valorTotal += valorTotal;
    }
    
    public void addValorComissao(double valorComissao){
         this.valorComissao += valorComissao;
    }
    
    public Double getPercComissao() {
        if (valorTotal != 0) {
            return (valorComissao/valorTotal*100);
            
        } else {
            return 0.0;
        }
    }
    
    public String getFormatPercComissao(){
        return nf.format(getPercComissao()) + " %";
    }
    
    public double getBonus(){
        return getValorComissao() * vendedor.getPercentualBonus()/100;
    }
    
    public String getFormatBonus(){
        return nfc.format(getBonus());
    }
    
    public double getMediaNotas(){
        return valorTotal/numNotas;
    }
    
    public String getFormatMediaNotas(){
        return nfc.format(getMediaNotas());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.vendedor);
        hash = 31 * hash + Objects.hashCode(this.empresa);
        hash = 31 * hash + this.numNotas;
        hash = 31 * hash + (int) (Double.doubleToLongBits(this.valorTotal) ^ (Double.doubleToLongBits(this.valorTotal) >>> 32));
        hash = 31 * hash + (int) (Double.doubleToLongBits(this.valorComissao) ^ (Double.doubleToLongBits(this.valorComissao) >>> 32));
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
        final VendedorEmpresa other = (VendedorEmpresa) obj;
        if (this.numNotas != other.numNotas) {
            return false;
        }
        if (Double.doubleToLongBits(this.valorTotal) != Double.doubleToLongBits(other.valorTotal)) {
            return false;
        }
        if (Double.doubleToLongBits(this.valorComissao) != Double.doubleToLongBits(other.valorComissao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.vendedor, other.vendedor)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return vendedor + " - " + empresa;
    }
    
    
    
}
