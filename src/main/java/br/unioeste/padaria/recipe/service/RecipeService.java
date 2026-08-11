package br.unioeste.padaria.recipe.service;

import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import br.unioeste.padaria.ingredient.repository.IngredientRepository;
import br.unioeste.padaria.recipe.model.Recipe;
import br.unioeste.padaria.recipe.model.RecipeIngredient;
import br.unioeste.padaria.recipe.model.dto.RecipeCreateDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeIngredientDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeIngredientQuantityDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeUpdateDTO;
import br.unioeste.padaria.recipe.repository.RecipeRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.persistence.criteria.Path;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public Recipe save(@Valid RecipeCreateDTO dto) {
        Recipe recipe = new Recipe();

        recipe.setName(dto.name());
        recipe.setSellingPrice(dto.sellingPrice());
        recipe.setPerfomance(dto.perfomance());
        recipe.setPreparationTime(dto.preparationTime());

        return recipeRepository.save(recipe);
    }

    public Page<Recipe> findAll(String name, Pageable pageable) {
        Specification<Recipe> specification = Specification.allOf();

        if (name != null && !name.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("name", name));
        }

        return recipeRepository.findAll(specification, pageable);
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> notFound("Recipe", id));
    }

    @Transactional
    public Recipe update(Long id, @Valid RecipeUpdateDTO dto) {
        Recipe recipe = this.findById(id);

        if (dto.name() != null) {
            recipe.setName(dto.name());
        }
        if (dto.sellingPrice() != null) {
            recipe.setSellingPrice(dto.sellingPrice());
        }
        if (dto.perfomance() != null) {
            recipe.setPerfomance(dto.perfomance());
        }
        if (dto.preparationTime() != null) {
            recipe.setPreparationTime(dto.preparationTime());
        }

        return recipeRepository.save(recipe);
    }

    @Transactional
    public RecipeIngredient addIngredient(Long recipeId, @Valid RecipeIngredientDTO dto) {
        Recipe recipe = findById(recipeId);

        boolean alreadyAdded = recipe.getIngredientList().stream().anyMatch(item -> item.getIngredient().getId().equals(dto.ingredientId()));

        if (alreadyAdded) {
            throw new ResponseStatusException(BAD_REQUEST, "Ingredient is already in this recipe");
        }

        Ingredient ingredient = ingredientRepository.findById(dto.ingredientId()).orElseThrow(() -> notFound("Ingredient", dto.ingredientId()));

        RecipeIngredient recipeIngredient = new RecipeIngredient();

        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.quantity());
        recipe.getIngredientList().add(recipeIngredient);

        recipeRepository.save(recipe);

        return recipeIngredient;
    }

    @Transactional
    public RecipeIngredient updateIngredientQuantity(Long recipeId, Long ingredientId, @Valid RecipeIngredientQuantityDTO dto) {
        RecipeIngredient recipeIngredient = findRecipeIngredient(recipeId, ingredientId);
        recipeIngredient.setQuantity(dto.quantity());

        return recipeIngredient;
    }

    @Transactional
    public void removeIngredient(Long recipeId, Long ingredientId) {
        Recipe recipe = findById(recipeId);
        RecipeIngredient recipeIngredient = findRecipeIngredient(recipe, ingredientId);
        recipe.getIngredientList().remove(recipeIngredient);

        recipeRepository.save(recipe);
    }

    @Transactional
    public void delete(Long id) {
        recipeRepository.delete(findById(id));
    }

    private RecipeIngredient findRecipeIngredient(Long recipeId, Long ingredientId) {
        return findRecipeIngredient(findById(recipeId), ingredientId);
    }

    private RecipeIngredient findRecipeIngredient(Recipe recipe, Long ingredientId) {
        return recipe.getIngredientList().stream()
                .filter(item -> item.getIngredient().getId().equals(ingredientId))
                .findFirst()
                .orElseThrow(() -> notFound("Ingredient in recipe", ingredientId));
    }

    private ResponseStatusException notFound(String resource, Long id) {
        return new ResponseStatusException(NOT_FOUND, resource + " with id " + id + " not found");
    }
}
