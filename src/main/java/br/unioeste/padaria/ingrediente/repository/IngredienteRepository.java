package br.unioeste.padaria.ingrediente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long>, JpaSpecificationExecutor<Ingrediente> {
}
