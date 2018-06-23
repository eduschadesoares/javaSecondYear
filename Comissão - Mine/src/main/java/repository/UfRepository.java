package repository;

import java.util.List;
import model.Uf;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document
public interface UfRepository extends MongoRepository<Uf, String> {

    public Uf findByUf(String uf);    
    public Uf findByUfIgnoreCase(String uf);
    public List<Uf> findByUfLikeIgnoreCase(String uf);

}
