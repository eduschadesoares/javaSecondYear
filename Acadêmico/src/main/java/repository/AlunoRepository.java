/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import model.Aluno;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.cdi.MongoRepositoryBean;

/**
 *
 * @author eduardo
 */
public interface AlunoRepository extends MongoRepository <Aluno, String> {
    
    public Aluno findByRa(String ra);
    
    
}
