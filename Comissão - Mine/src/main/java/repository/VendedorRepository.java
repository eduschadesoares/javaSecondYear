package repository;

import model.Vendedor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document
public interface VendedorRepository extends MongoRepository<Vendedor, String> {

    public Vendedor findByNome(String nome);
    
}
