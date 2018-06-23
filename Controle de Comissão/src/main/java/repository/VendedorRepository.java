/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Vendedor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author mrgomes
 */

@Document
public interface VendedorRepository extends MongoRepository<Vendedor, String>{
    public Vendedor findByNome(String nome);
}
