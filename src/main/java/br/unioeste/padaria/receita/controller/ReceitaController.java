package br.unioeste.padaria.receita.controller;

import br.unioeste.padaria.receita.model.dto.*;
import br.unioeste.padaria.receita.model.entity.Receita;
import br.unioeste.padaria.receita.model.entity.ReceitaIngrediente;
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
    public ResponseEntity<ReceitaDTO> salvarReceita(
            @Valid @RequestBody CriarReceitaDTO dto) {
        Receita receita = receitaService.salvarReceita(dto);
        return ResponseEntity.ok(receitaService.formatarReceita(receita));
    }

    @GetMapping
    public ResponseEntity<Page<ReceitaDTO>> listarTodasReceitas(
            @RequestParam(required = false) String nomeReceita,
            Pageable pageable) {
        return ResponseEntity.ok(receitaService.listarTodasReceitas(nomeReceita, pageable));
    }

    @GetMapping("/{idReceita}")
    public ResponseEntity<ReceitaDTO> buscarReceitaPorId(
            @PathVariable Long idReceita) {
        Receita receita = receitaService.buscarReceitaPorId(idReceita);
        return ResponseEntity.ok(receitaService.formatarReceita(receita));
    }

    @PatchMapping("/{idReceita}")
    public ResponseEntity<ReceitaDTO> atualizarReceita(
            @PathVariable Long idReceita,
            @Valid @RequestBody AtualizarReceitaDTO dto) {
        Receita receita = receitaService.atualizarReceita(idReceita, dto);
        return ResponseEntity.ok(receitaService.formatarReceita(receita));
    }

    @PostMapping("/{idReceita}/ingrediente")
    public ResponseEntity<ReceitaIngrediente> adicionarReceitaIngrediente(
            @PathVariable Long idReceita,
            @Valid @RequestBody CriarReceitaIngredienteDTO dto) {
        return ResponseEntity.ok(receitaService.adicionarReceitaIngrediente(idReceita, dto));
    }

    @PatchMapping("/{idReceita}/ingrediente/{idIngrediente}")
    public ResponseEntity<ReceitaIngrediente> atualizarQuantidadeIngrediente(
            @PathVariable Long idReceita,
            @PathVariable Long idIngrediente,
            @Valid @RequestBody AtualizarQuantidadeIngredienteDTO dto) {
        return ResponseEntity.ok(receitaService.atualizarQuantidadeIngrediente(idReceita, idIngrediente, dto));
    }

    @DeleteMapping("/{idReceita}/ingrediente/{idIngrediente}")
    public ResponseEntity<Void> removerReceitaIngrediente(
            @PathVariable Long idReceita,
            @PathVariable Long idIngrediente) {
        receitaService.removerReceitaIngrediente(idReceita, idIngrediente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idReceita}")
    public ResponseEntity<Void> deletarReceita(
            @PathVariable Long idReceita) {
        receitaService.deletar(idReceita);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simular")
    public ResponseEntity<SimulacaoReceitaDTO> simularReceita(@RequestBody @Valid CriarSimulacaoReceitaDTO dto){
        return ResponseEntity.ok(receitaService.simularReceita(dto));
    }
}
