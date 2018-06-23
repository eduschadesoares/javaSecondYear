package repository;

import java.util.List;
import model.Cidade;
import model.Empresa;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document
public interface EmpresaRepository extends MongoRepository<Empresa, String> {

    public Empresa findByNome(String nome);
    public Empresa findByCnpj(String cnpj);
    
    public List<Empresa> findByCidade(Cidade cidade);
    
    public List<Empresa> findByNomeLikeIgnoreCase(String nome);
    public List<Empresa> findByNomeLikeIgnoreCaseOrFantasiaLikeIgnoreCase(String nome, String fantasia);
    
    public Integer countByCidade(Cidade cidade);
}
