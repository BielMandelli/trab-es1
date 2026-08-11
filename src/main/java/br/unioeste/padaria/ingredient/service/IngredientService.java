package br.unioeste.padaria.ingredient.service;

import br.unioeste.padaria.ingredient.model.dto.IngredientCategoryCreateDTO;
import br.unioeste.padaria.ingredient.model.dto.IngredientCreateDTO;
import br.unioeste.padaria.ingredient.model.dto.IngredientUnitCreateDTO;
import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import br.unioeste.padaria.ingredient.model.entity.IngredientCategory;
import br.unioeste.padaria.ingredient.model.entity.IngredientUnit;
import br.unioeste.padaria.ingredient.repository.IngredientCategoryRepository;
import br.unioeste.padaria.ingredient.repository.IngredientRepository;
import br.unioeste.padaria.ingredient.repository.IngredientUnitRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.persistence.criteria.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientUnitRepository ingredientUnitRepository;

    public IngredientUnit saveUnit(@Valid IngredientUnitCreateDTO dto) {
        IngredientUnit ingredientUnit = new IngredientUnit();
        ingredientUnit.setName(dto.name());
        ingredientUnit.setAbbreviation(dto.abbreviation());
        return ingredientUnitRepository.save(ingredientUnit);
    }

    public IngredientCategory saveCategory(@Valid IngredientCategoryCreateDTO dto) {
        IngredientCategory ingredientCategory = new IngredientCategory();
        ingredientCategory.setName(dto.name());
        return ingredientCategoryRepository.save(ingredientCategory);
    }

    public Ingredient save(@Valid IngredientCreateDTO dto) {
        Ingredient ingredient = new Ingredient();

        IngredientUnit ingredientUnit = ingredientUnitRepository.findById(dto.idUnit()).orElseThrow(() -> notFound("Ingredient unit", dto.idUnit()));
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findById(dto.idCategory()).orElseThrow(() -> notFound("Ingredient category", dto.idCategory()));

        ingredient.setName(dto.name());
        ingredient.setIngredientCategory(ingredientCategory);
        ingredient.setIngredientUnit(ingredientUnit);
        ingredient.setPricePerUnit(dto.pricePerUnit());
        ingredient.setStock(dto.stock());
        ingredient.setMinStock(dto.minStock());

        return ingredientRepository.save(ingredient);
    }

    public Page<Ingredient> findAll(String name, String categoryName, Pageable pageable) {
        Specification<Ingredient> specification = Specification.allOf();

        if (name != null && !name.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("name", name));
        }

        if (categoryName != null && !categoryName.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("ingredientCategory.name", categoryName));
        }

        return ingredientRepository.findAll(specification, pageable);
    }

    public Page<IngredientCategory> findAllCategories(String name, Pageable pageable) {
        Specification<IngredientCategory> specification = Specification.allOf();

        if (name != null && !name.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("name", name));
        }

        return ingredientCategoryRepository.findAll(specification, pageable);
    }

    public Page<IngredientUnit> findAllUnits(String name, Pageable pageable) {
        Specification<IngredientUnit> specification = Specification.allOf();

        if (name != null && !name.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("name", name));
        }

        return ingredientUnitRepository.findAll(specification, pageable);
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> notFound("Ingredient", id));
    }

    public IngredientCategory findCategoryById(Long id) {
        return ingredientCategoryRepository.findById(id).orElseThrow(() -> notFound("Ingredient category", id));
    }

    public IngredientUnit findUnitById(Long id) {
        return ingredientUnitRepository.findById(id).orElseThrow(() -> notFound("Ingredient unit", id));
    }



    private ResponseStatusException notFound(String resource, Long id) {
        return new ResponseStatusException(NOT_FOUND, resource + " with id " + id + " not found");
    }
}

