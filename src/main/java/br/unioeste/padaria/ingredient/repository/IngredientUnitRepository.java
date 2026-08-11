package br.unioeste.padaria.ingredient.repository;

import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import br.unioeste.padaria.ingredient.model.entity.IngredientCategory;
import br.unioeste.padaria.ingredient.model.entity.IngredientUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientUnitRepository extends JpaRepository<IngredientUnit, Long>, JpaSpecificationExecutor<IngredientUnit> {
}
