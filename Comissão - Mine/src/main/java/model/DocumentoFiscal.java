package model;

import static config.Config.df;
import static config.Config.nfc;
import static config.DAO.docFiscalRepository;
import static config.DAO.empresaRepository;
import static config.DAO.vendedorRepository;
import java.text.ParseException;
import java.time.LocalDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document
public class DocumentoFiscal {

    @Id
    private String id;

    @DBRef
    private Vendedor vendedor;

    @DBRef
    private Empresa empresa;

    private String nota;
    private Double valorNota, valorCredito;
    private LocalDate emissao;
    private String situacaoCredito;

    public DocumentoFiscal() {
    }

    public DocumentoFiscal(Empresa empresa, Vendedor vendedor, String nota, LocalDate emissao, double valorNota, double valorCredito, String situacaoCredito) {
        setEmpresa(empresa);
        setVendedor(vendedor);
        setNota(nota);
        setEmissao(emissao);
        setValorNota(valorNota);
        setValorCredito(valorCredito);
        setSituacaoCredito(situacaoCredito);
    }

    public DocumentoFiscal(String linha, Vendedor vendedor) throws ParseException {
        String[] partes = linha.split(";");

        String cnpj = partes[0].replaceAll("[./-]+", "");

        Empresa empresa = new Empresa();
        if (empresaRepository.findByCnpj(cnpj) == null) {
            empresa.setCnpj(cnpj);
            empresa.setNome(partes[1]);
            empresa.setFantasia(partes[1]);
            //empresa.setCidade(cidadeRepository.findByNome("Ponta Grossa"));
            empresaRepository.insert(empresa);
        }
        empresa = empresaRepository.findByCnpj(cnpj);
        vendedor = vendedorRepository.findOne(vendedor.getId());

        setNota(partes[2]);
        setEmissao(LocalDate.parse(partes[3], df));
        setValorNota(nfc.parse(partes[4]).doubleValue());
        setValorCredito(nfc.parse(partes[5]).doubleValue());
        setSituacaoCredito(partes[6]);

        DocumentoFiscal docGravacao = new DocumentoFiscal(empresa, vendedor, nota, emissao, valorNota, valorCredito, situacaoCredito);

        docFiscalRepository.insert(docGravacao);

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

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDate getEmissao() {
        return emissao;
    }

    public void setEmissao(LocalDate emissao) {
        this.emissao = emissao;
    }

    public Double getValorNota() {
        return valorNota;
    }

    public void setValorNota(Double valorNota) {
        this.valorNota = valorNota;
    }

    public Double getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getSituacaoCredito() {
        return situacaoCredito;
    }

    public void setSituacaoCredito(String situacaoCredito) {
        this.situacaoCredito = situacaoCredito;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.vendedor);
        hash = 97 * hash + Objects.hashCode(this.empresa);
        hash = 97 * hash + Objects.hashCode(this.nota);
        hash = 97 * hash + Objects.hashCode(this.valorNota);
        hash = 97 * hash + Objects.hashCode(this.valorCredito);
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
        final DocumentoFiscal other = (DocumentoFiscal) obj;
        if (!Objects.equals(this.nota, other.nota)) {
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
        return "DocumentoFiscal{" + "Vendedor=" + vendedor + ", Empresa=" + empresa + ", Nota=" + nota + ", Valor Nota=" + valorNota + ", Valor Credito=" + valorCredito +  ", Situação do Credito=" + situacaoCredito + '}';
    }

}
