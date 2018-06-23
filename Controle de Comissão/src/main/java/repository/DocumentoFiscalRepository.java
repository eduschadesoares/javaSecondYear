/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import model.DocumentoFiscal;
import model.Empresa;
import model.Vendedor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author mrgomes
 */

@Document
public interface DocumentoFiscalRepository extends MongoRepository<DocumentoFiscal, String>{
    public DocumentoFiscal findByEmpresaAndNota(Empresa empresa, String nota);
    public List<DocumentoFiscal> findByVendedor(Vendedor vendedor);
    public Integer countByVendedor(Vendedor vendedor);
}
