package br.unioeste.padaria.recipe.controller;

import br.unioeste.padaria.recipe.model.Recipe;
import br.unioeste.padaria.recipe.model.RecipeIngredient;
import br.unioeste.padaria.recipe.model.dto.RecipeCreateDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeIngredientDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeIngredientQuantityDTO;
import br.unioeste.padaria.recipe.model.dto.RecipeUpdateDTO;
import br.unioeste.padaria.recipe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Recipe> save(
            @Valid @RequestBody RecipeCreateDTO dto) {
        return ResponseEntity.ok(recipeService.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Recipe>> findAll(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(recipeService.findAll(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(recipeService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Recipe> update(
            @PathVariable Long id,
            @Valid @RequestBody RecipeUpdateDTO dto) {
        return ResponseEntity.ok(recipeService.update(id, dto));
    }

    @PostMapping("/{recipeId}/ingredient")
    public ResponseEntity<RecipeIngredient> addIngredient(
            @PathVariable Long recipeId,
            @Valid @RequestBody RecipeIngredientDTO dto) {
        return ResponseEntity.ok(recipeService.addIngredient(recipeId, dto));
    }

    @PatchMapping("/{recipeId}/ingredient/{ingredientId}")
    public ResponseEntity<RecipeIngredient> updateIngredientQuantity(
            @PathVariable Long recipeId,
            @PathVariable Long ingredientId,
            @Valid @RequestBody RecipeIngredientQuantityDTO dto) {
        return ResponseEntity.ok(recipeService.updateIngredientQuantity(recipeId, ingredientId, dto));
    }

    @DeleteMapping("/{recipeId}/ingredient/{ingredientId}")
    public ResponseEntity<Void> removeIngredient(
            @PathVariable Long recipeId,
            @PathVariable Long ingredientId) {
        recipeService.removeIngredient(recipeId, ingredientId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        recipeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
