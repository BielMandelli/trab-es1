package br.unioeste.padaria.unidade.controller;

import br.unioeste.padaria.unidade.model.dto.AtualizarUnidadeMedidaDTO;
import br.unioeste.padaria.unidade.model.dto.CriarUnidadeMedidaDTO;
import br.unioeste.padaria.unidade.model.entity.UnidadeMedida;
import br.unioeste.padaria.unidade.service.UnidadeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/unidade")
public class UnidadeController {

    UnidadeService unidadeService;

    @PostMapping
    public ResponseEntity<UnidadeMedida> salvarUnidade(
            @Valid @RequestBody CriarUnidadeMedidaDTO dto) {
        return ResponseEntity.ok(unidadeService.salvarUnidade(dto));
    }

    @GetMapping
    public ResponseEntity<Page<UnidadeMedida>> listarTodasUnidadeMedida(
            @RequestParam(required = false) String nomeUnidadeMedida,
            Pageable pageable) {
        return ResponseEntity.ok(unidadeService.listarTodasUnidadesMedida(nomeUnidadeMedida, pageable));
    }

    @GetMapping("/{idUnidadeMedida}")
    public ResponseEntity<UnidadeMedida> buscarUnidadeMedidaPorId(
            @PathVariable Long idUnidadeMedida) {
        return ResponseEntity.ok(unidadeService.buscarUnidadeMedidaPorId(idUnidadeMedida));
    }

    @PutMapping("/{idUnidadeMedida}")
    public ResponseEntity<UnidadeMedida> atualizarUnidadeMedida(
            @PathVariable Long idUnidadeMedida,
            @Valid @RequestBody AtualizarUnidadeMedidaDTO dto) {
        return ResponseEntity.ok(unidadeService.atualizarUnidadeMedida(dto, idUnidadeMedida));
    }

    @DeleteMapping("/{idUnidadeMedida}")
    public ResponseEntity<UnidadeMedida> deletarUnidadeMedida(
            @PathVariable Long idUnidadeMedida) {
        unidadeService.deletar(idUnidadeMedida);
        return ResponseEntity.noContent().build();
    }
}
