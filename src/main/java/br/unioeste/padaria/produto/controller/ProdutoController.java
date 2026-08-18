package br.unioeste.padaria.produto.controller;

import br.unioeste.padaria.produto.model.dto.*;
import br.unioeste.padaria.produto.model.entity.Produto;
import br.unioeste.padaria.produto.model.entity.ProdutoIngrediente;
import br.unioeste.padaria.produto.service.ProdutoService;
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
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoDTO> salvarProduto(
            @Valid @RequestBody CriarProdutoDTO dto) {
        Produto produto = produtoService.salvarProduto(dto);
        return ResponseEntity.ok(produtoService.formatarProduto(produto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoDTO>> listarTodosProdutos(
            @RequestParam(required = false) String nomeProduto,
            Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodosProdutos(nomeProduto, pageable));
    }

    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(
            @PathVariable Long idProduto) {
        Produto produto = produtoService.buscarProdutoPorId(idProduto);
        return ResponseEntity.ok(produtoService.formatarProduto(produto));
    }

    @PatchMapping("/{idProduto}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @PathVariable Long idProduto,
            @Valid @RequestBody AtualizarProdutoDTO dto) {
        Produto produto = produtoService.atualizarProduto(idProduto, dto);
        return ResponseEntity.ok(produtoService.formatarProduto(produto));
    }

    @PostMapping("/{idProduto}/ingrediente")
    public ResponseEntity<ProdutoIngrediente> adicionarProdutoIngrediente(
            @PathVariable Long idProduto,
            @Valid @RequestBody CriarProdutoIngredienteDTO dto) {
        return ResponseEntity.ok(produtoService.adicionarProdutoIngrediente(idProduto, dto));
    }

    @PatchMapping("/{idProduto}/ingrediente/{idIngrediente}")
    public ResponseEntity<ProdutoIngrediente> atualizarQuantidadeIngrediente(
            @PathVariable Long idProduto,
            @PathVariable Long idIngrediente,
            @Valid @RequestBody AtualizarQuantidadeIngredienteDTO dto) {
        return ResponseEntity.ok(produtoService.atualizarQuantidadeIngrediente(idProduto, idIngrediente, dto));
    }

    @DeleteMapping("/{idProduto}/ingrediente/{idIngrediente}")
    public ResponseEntity<Void> removerProdutoIngrediente(
            @PathVariable Long idProduto,
            @PathVariable Long idIngrediente) {
        produtoService.removerProdutoIngrediente(idProduto, idIngrediente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> deletarProduto(
            @PathVariable Long idProduto) {
        produtoService.deletar(idProduto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/simular")
    public ResponseEntity<SimulacaoProdutoDTO> simularProduto(@RequestBody @Valid CriarSimulacaoProdutoDTO dto){
        return ResponseEntity.ok(produtoService.simularProduto(dto));
    }
}
