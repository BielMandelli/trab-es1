package br.unioeste.padaria.ingredient.controller;

import br.unioeste.padaria.ingredient.model.dto.IngredientCategoryCreateDTO;
import br.unioeste.padaria.ingredient.model.dto.IngredientCreateDTO;
import br.unioeste.padaria.ingredient.model.dto.IngredientUnitCreateDTO;
import br.unioeste.padaria.ingredient.model.entity.Ingredient;
import br.unioeste.padaria.ingredient.model.entity.IngredientCategory;
import br.unioeste.padaria.ingredient.model.entity.IngredientUnit;
import br.unioeste.padaria.ingredient.service.IngredientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    private IngredientService ingredientService;

    @PostMapping("/unit")
    public ResponseEntity<IngredientUnit> saveUnit(
            @Valid @RequestBody IngredientUnitCreateDTO dto) {
        return ResponseEntity.ok(ingredientService.saveUnit(dto));
    }

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> saveCategory(
            @Valid @RequestBody IngredientCategoryCreateDTO dto) {
        return ResponseEntity.ok(ingredientService.saveCategory(dto));
    }

    @PostMapping
    public ResponseEntity<Ingredient> save(
            @Valid @RequestBody IngredientCreateDTO dto) {
        return ResponseEntity.ok(ingredientService.save(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Ingredient>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            Pageable pageable) {
        return ResponseEntity.ok(ingredientService.findAll(name, categoryName, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingredient> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findById(id));
    }

    @GetMapping("/category")
    public ResponseEntity<Page<IngredientCategory>> findAllCategories(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(ingredientService.findAllCategories(name, pageable));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<IngredientCategory> findCategoryById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findCategoryById(id));
    }

    @GetMapping("/unit")
    public ResponseEntity<Page<IngredientUnit>> findAllUnits(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(ingredientService.findAllUnits(name, pageable));
    }

    @GetMapping("/unit/{id}")
    public ResponseEntity<IngredientUnit> findUnitById
            (@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.findUnitById(id));
    }
}
