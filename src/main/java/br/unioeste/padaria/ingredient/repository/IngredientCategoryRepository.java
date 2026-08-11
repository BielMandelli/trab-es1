package br.unioeste.padaria.ingredient.repository;

import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import br.unioeste.padaria.ingredient.model.entity.IngredientCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long>, JpaSpecificationExecutor<IngredientCategory> {

}
