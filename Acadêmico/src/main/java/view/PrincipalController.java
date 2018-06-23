package view;

import static config.DAO.alunoRepository;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Aluno;
import repository.AlunoRepository;

public class PrincipalController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Inserir manuelmente
        Aluno alInsere1 = new Aluno("16416423", "José Carlo", "josecarlo@hotmail.com");
        alInsere1 = alunoRepository.insert(alInsere1);
        Aluno alInsere2 = new Aluno("16316323", "João Paz", "joaopaz@hotmail.com");
        alInsere2 = alunoRepository.insert(alInsere2);
        Aluno alInsere3 = new Aluno("16216223", "Quatro Braços", "quatrobracos@hotmail.com");
        alInsere3 = alunoRepository.insert(alInsere3);
        Aluno alInsere4 = new Aluno("16116123", "XLR8 10", "xlr8@hotmail.com");
        alInsere4 = alunoRepository.insert(alInsere4);
        Aluno alInsere5 = new Aluno("16016023", "Carlos", "carlos@hotmail.com");
        alInsere5 = alunoRepository.insert(alInsere5);

        //Listar alunos carregando na memória
        /*      List<Aluno> lstAluno = new ArrayList<Aluno>();
        
        lstAluno = alunoRepository.findAll();
        
        for(Aluno al : lstAluno) {
            System.err.println(al);
        } */
        
        //Encontra por algum parâmetro (AlunoRepository.java)
        Aluno alBusca = alunoRepository.findByRa("16416423");
        System.err.println(alBusca);

        //Altera dados
//      alBusca.setNome("Eduardo Soares");
//      alunoRepository.save(alBusca);
    }
}
