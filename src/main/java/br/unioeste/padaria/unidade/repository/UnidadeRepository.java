package br.unioeste.padaria.unidade.repository;

import br.unioeste.padaria.unidade.model.entity.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UnidadeRepository extends JpaRepository<UnidadeMedida, Long>, JpaSpecificationExecutor<UnidadeMedida> {
}
