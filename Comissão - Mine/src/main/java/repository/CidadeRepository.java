package repository;

import java.util.List;
import model.Cidade;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document
public interface CidadeRepository extends MongoRepository<Cidade, String> {

    public Cidade findByNome(String nome);    
    public Cidade findByNomeIgnoreCase(String nome);
    public List<Cidade> findByNomeLikeIgnoreCase(String nome);

}
