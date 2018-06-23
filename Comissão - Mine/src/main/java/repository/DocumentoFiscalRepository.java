package repository;

import java.util.List;
import model.Cidade;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document
public interface DocumentoFiscalRepository extends MongoRepository<DocumentoFiscal, String> {

    public DocumentoFiscal findByEmpresaAndNota(Empresa empresa, String nota);

    public List<DocumentoFiscal> findByVendedor(Vendedor vendedor);

    public Integer countByVendedor(Vendedor vendedor);
    
    public List <DocumentoFiscal> findByEmpresa(Empresa empresa);
}
