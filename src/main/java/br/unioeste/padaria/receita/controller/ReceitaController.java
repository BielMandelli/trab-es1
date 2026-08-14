package br.unioeste.padaria.receita.controller;

import br.unioeste.padaria.receita.model.entity.Receita;
import br.unioeste.padaria.receita.model.entity.ReceitaIngrediente;
import br.unioeste.padaria.receita.model.dto.CriarReceitaDTO;
import br.unioeste.padaria.receita.model.dto.ReceitaIngredienteDTO;
import br.unioeste.padaria.receita.model.dto.ReceitaIngredienteQuantidadeDTO;
import br.unioeste.padaria.receita.model.dto.AtualizarReceitaDTO;
import br.unioeste.padaria.receita.service.ReceitaService;
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
@RequestMapping("/receita")
public class ReceitaController {

    private final ReceitaService receitaService;

    @PostMapping
    public ResponseEntity<Receita> salvarReceita(
            @Valid @RequestBody CriarReceitaDTO dto) {
        return ResponseEntity.ok(receitaService.salvarReceita(dto));
    }

    @GetMapping
    public ResponseEntity<Page<Receita>> listarTodasReceitas(
            @RequestParam(required = false) String nomeReceita,
            Pageable pageable) {
        return ResponseEntity.ok(receitaService.listarTodasReceitas(nomeReceita, pageable));
    }

    @GetMapping("/{idReceita}")
    public ResponseEntity<Receita> buscarReceitaPorId(
            @PathVariable Long idReceita) {
        return ResponseEntity.ok(receitaService.buscarReceitaPorId(idReceita));
    }

    @PatchMapping("/{idReceita}")
    public ResponseEntity<Receita> atualizarReceita(
            @PathVariable Long idReceita,
            @Valid @RequestBody AtualizarReceitaDTO dto) {
        return ResponseEntity.ok(receitaService.atualizarReceita(idReceita, dto));
    }

    @PostMapping("/{idReceita}/ingrediente")
    public ResponseEntity<ReceitaIngrediente> adicionarReceitaIngrediente(
            @PathVariable Long idReceita,
            @Valid @RequestBody ReceitaIngredienteDTO dto) {
        return ResponseEntity.ok(receitaService.adicionarReceitaIngrediente(idReceita, dto));
    }

    @PatchMapping("/{idReceita}/ingrediente/{idIngrediente}")
    public ResponseEntity<ReceitaIngrediente> atualizarQuantidadeIngrediente(
            @PathVariable Long idReceita,
            @PathVariable Long idIngrediente,
            @Valid @RequestBody ReceitaIngredienteQuantidadeDTO dto) {
        return ResponseEntity.ok(receitaService.atualizarQuantidadeIngrediente(idReceita, idIngrediente, dto));
    }

    @DeleteMapping("/{idReceita}/ingredient/{idIngrediente}")
    public ResponseEntity<Void> removerReceitaIngrediente(
            @PathVariable Long idReceita,
            @PathVariable Long idIngrediente) {
        receitaService.removerReceitaIngrediente(idReceita, idIngrediente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idReceita}")
    public ResponseEntity<Void> delete(
            @PathVariable Long idReceita) {
        receitaService.deletar(idReceita);
        return ResponseEntity.noContent().build();
    }
}
