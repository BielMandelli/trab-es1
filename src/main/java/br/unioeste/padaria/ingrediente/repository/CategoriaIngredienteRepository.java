package br.unioeste.padaria.ingrediente.repository;

import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaIngredienteRepository extends JpaRepository<CategoriaIngrediente, Long>, JpaSpecificationExecutor<CategoriaIngrediente> {

}
