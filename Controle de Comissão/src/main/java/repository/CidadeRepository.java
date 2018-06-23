/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import java.util.List;
import model.Cidade;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author mrgomes
 */

@Document
public interface CidadeRepository extends MongoRepository<Cidade, String>{
    
    public Cidade findByNome(String nome);
    public Cidade findByNomeIgnoreCase(String nome);
    public List<Cidade> findByNomeLikeIgnoreCase(String nome);
}
