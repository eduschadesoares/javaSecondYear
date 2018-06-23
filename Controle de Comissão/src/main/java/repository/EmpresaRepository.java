/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import model.Cidade;
import model.Empresa;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author mrgomes
 */

@Document
public interface EmpresaRepository extends MongoRepository<Empresa, String>{
    
    public Empresa findByCnpj(String cnpj);
    public List<Empresa> findByNomeLikeIgnoreCaseOrFantasiaLikeIgnoreCase(String nome, String fantasia);
    public List<Empresa> findByNomeLikeIgnoreCase(String nome);
    public int countByCidade(Cidade cidade);
    
}
