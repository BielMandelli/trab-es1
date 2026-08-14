package br.unioeste.padaria.ingrediente.controller;

import br.unioeste.padaria.ingrediente.model.dto.CriarCategoriaIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.dto.CriarIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.dto.CriarUnidadeIngredienteDTO;
import br.unioeste.padaria.ingrediente.model.entity.CategoriaIngrediente;
import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.model.entity.UnidadeIngrediente;
import br.unioeste.padaria.ingrediente.service.IngredienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/ingrediente")
public class IngredienteController {

    private IngredienteService ingredienteService;

    @PostMapping("/unidade")
    public ResponseEntity<UnidadeIngrediente> salvarUnidade(
            @Valid @RequestBody CriarUnidadeIngredienteDTO dto) {
        return ResponseEntity.ok(ingredienteService.salvarUnidade(dto));
    }

    @PostMapping("/categoria")
    public ResponseEntity<CategoriaIngrediente> salvarCategoria(
            @Valid @RequestBody CriarCategoriaIngredienteDTO dto) {
        return ResponseEntity.ok(ingredienteService.salvarCategoria(dto));
    }

    @PostMapping
    public ResponseEntity<Ingrediente> salvarIngrediente(
            @Valid @RequestBody CriarIngredienteDTO dto) {
        return ResponseEntity.ok(ingredienteService.salvarIngrediente(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Ingrediente>> listarTodosIngredientes(
            @RequestParam(required = false) String nomeIngrediente,
            @RequestParam(required = false) String nomeCategoriaIngrediente,
            Pageable pageable) {
        return ResponseEntity.ok(ingredienteService.listarTodosIngredientes(nomeIngrediente, nomeCategoriaIngrediente, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingrediente> buscarIngredientePorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(ingredienteService.buscarIngredientePorId(id));
    }

    @GetMapping("/categoria")
    public ResponseEntity<Page<CategoriaIngrediente>> listarTodasCategoriasIngrediente(
            @RequestParam(required = false) String nomeCategoriaIngrediente,
            Pageable pageable) {
        return ResponseEntity.ok(ingredienteService.listarTodasCategoriasIngrediente(nomeCategoriaIngrediente, pageable));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<CategoriaIngrediente> buscarCategoriaIngredientePorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(ingredienteService.buscarCategoriaIngredientePorId(id));
    }

    @GetMapping("/unidade")
    public ResponseEntity<Page<UnidadeIngrediente>> listarTodasUnidadesIngrediente(
            @RequestParam(required = false) String nomeUnidadeIngrediente,
            Pageable pageable) {
        return ResponseEntity.ok(ingredienteService.listarTodasUnidadesIngrediente(nomeUnidadeIngrediente, pageable));
    }

    @GetMapping("/unidade/{id}")
    public ResponseEntity<UnidadeIngrediente> buscarUnidadeIngredientePorId
            (@PathVariable Long id) {
        return ResponseEntity.ok(ingredienteService.buscarUnidadeIngredientePorId(id));
    }
}
