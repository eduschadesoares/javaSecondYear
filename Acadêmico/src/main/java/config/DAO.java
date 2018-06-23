/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import repository.AlunoRepository;

/**
 *
 * @author eduardo
 */
public class DAO {
    private static final AnnotationConfigApplicationContext ctx
            = new AnnotationConfigApplicationContext(DBConfig.class);
    
    public static AlunoRepository alunoRepository 
            = ctx.getBean(AlunoRepository.class);
    
}
