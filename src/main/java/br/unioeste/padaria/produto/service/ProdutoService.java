package br.unioeste.padaria.produto.service;

import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.repository.IngredienteRepository;
import br.unioeste.padaria.produto.model.dto.*;
import br.unioeste.padaria.produto.model.entity.Produto;
import br.unioeste.padaria.produto.model.entity.ProdutoIngrediente;
import br.unioeste.padaria.produto.repository.ProdutoRepository;
import br.unioeste.padaria.utils.SpecificationUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final IngredienteRepository ingredienteRepository;

    @Transactional
    public Produto salvarProduto(@Valid CriarProdutoDTO dto) {
        Produto produto = new Produto();

        produto.setNomeProduto(dto.nomeProduto());
        produto.setCustoVenda(dto.precoVenda());
        produto.setRendimento(dto.rendimento());
        produto.setTempoPreparacao(dto.tempoPreparacao());

        for (CriarProdutoIngredienteDTO criarProdutoIngredienteDTO : dto.ingredientes()) {

            Ingrediente ingrediente = this.buscarIngredientePorId(criarProdutoIngredienteDTO.idIngrediente());

            boolean jaAdicionado = produto.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(criarProdutoIngredienteDTO.idIngrediente()));

            if (jaAdicionado) continue;

            ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();

            produtoIngrediente.setProduto(produto);
            produtoIngrediente.setIngrediente(ingrediente);
            produtoIngrediente.setQuantidade(criarProdutoIngredienteDTO.quantidade());

            produto.getIngredientList().add(produtoIngrediente);
        }

        return produtoRepository.save(produto);
    }

    public Page<ProdutoDTO> listarTodosProdutos(String nomeProduto, Pageable pageable) {
        Specification<Produto> specification = Specification.allOf();

        if (nomeProduto != null && !nomeProduto.isBlank()) {
            specification = specification.and(SpecificationUtils.containsIgnoreCase("nomeProduto", nomeProduto));
        }

        Page<Produto> receitaPage = produtoRepository.findAll(specification, pageable);

        return receitaPage.map(this::formatarProduto);
    }

    public Produto buscarProdutoPorId(Long idProduto) {
        return produtoRepository.findById(idProduto).orElseThrow(() -> naoEncontradoErro("Produto", idProduto));
    }

    public Ingrediente buscarIngredientePorId(Long idIngrediente) {
        return ingredienteRepository.findById(idIngrediente).orElseThrow(() -> naoEncontradoErro("Ingrediente", idIngrediente));
    }

    @Transactional
    public Produto atualizarProduto(Long idProduto, @Valid AtualizarProdutoDTO dto) {
        Produto produto = this.buscarProdutoPorId(idProduto);

        if (dto.nomeProduto() != null) {
            produto.setNomeProduto(dto.nomeProduto());
        }
        if (dto.custoVenda() != null) {
            produto.setCustoVenda(dto.custoVenda());
        }
        if (dto.rendimento() != null) {
            produto.setRendimento(dto.rendimento());
        }
        if (dto.tempoPreparacao() != null) {
            produto.setTempoPreparacao(dto.tempoPreparacao());
        }

        return produtoRepository.save(produto);
    }

    @Transactional
    public ProdutoIngrediente adicionarProdutoIngrediente(Long idProduto, @Valid CriarProdutoIngredienteDTO dto) {
        Produto produto = buscarProdutoPorId(idProduto);

        boolean jaAdicionado = produto.getIngredientList().stream().anyMatch(item -> item.getIngrediente().getIdIngrediente().equals(dto.idIngrediente()));

        if (jaAdicionado) throw new ResponseStatusException(BAD_REQUEST, "Ingrediente já está nessa receita");

        Ingrediente ingrediente = this.buscarIngredientePorId(dto.idIngrediente());

        ProdutoIngrediente produtoIngrediente = new ProdutoIngrediente();

        produtoIngrediente.setIdProdutoIngrediente(produto.getIdProduto());
        produtoIngrediente.setProduto(produto);
        produtoIngrediente.setIngrediente(ingrediente);
        produtoIngrediente.setQuantidade(dto.quantidade());
        produto.getIngredientList().add(produtoIngrediente);

        produtoRepository.save(produto);

        return produtoIngrediente;
    }

    @Transactional
    public ProdutoIngrediente atualizarQuantidadeIngrediente(Long idProduto, Long idIngrediente, @Valid AtualizarQuantidadeIngredienteDTO dto) {
        ProdutoIngrediente produtoIngrediente = encontrarProdutoIngrediente(idProduto, idIngrediente);

        produtoIngrediente.setQuantidade(dto.quantidade());

        return produtoIngrediente;
    }

    @Transactional
    public void removerProdutoIngrediente(Long idReceita, Long idIngrediente) {
        Produto produto = buscarProdutoPorId(idReceita);
        ProdutoIngrediente produtoIngrediente = encontrarProdutoIngrediente(produto, idIngrediente);

        produto.getIngredientList().remove(produtoIngrediente);

        produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long idProduto) {
        produtoRepository.delete(buscarProdutoPorId(idProduto));
    }

    private ProdutoIngrediente encontrarProdutoIngrediente(Long idProduto, Long idIngrediente) {
        return encontrarProdutoIngrediente(buscarProdutoPorId(idProduto), idIngrediente);
    }

    private ProdutoIngrediente encontrarProdutoIngrediente(Produto produto, Long ingredientId) {
        return produto.getIngredientList().stream()
                .filter(item -> item.getIngrediente().getIdIngrediente().equals(ingredientId))
                .findFirst()
                .orElseThrow(() -> naoEncontradoErro("Ingrediente na Receita", ingredientId));
    }

    private ResponseStatusException naoEncontradoErro(String entidade, Long id) {
        return new ResponseStatusException(NOT_FOUND, entidade + " com id " + id + " não encontrado");
    }

    public ProdutoDTO formatarProduto(Produto produto) {

        List<ProdutoIngredienteDTO> ingredienteList = produto.getIngredientList()
                .stream()
                .map(produtoIngrediente -> {
                    Ingrediente ingrediente = produtoIngrediente.getIngrediente();

                    return new ProdutoIngredienteDTO(
                            ingrediente.getIdIngrediente(),
                            ingrediente.getNomeIngrediente(),
                            ingrediente.getCategoriaIngrediente(),
                            ingrediente.getUnidadeIngrediente(),
                            ingrediente.getPrecoPorUnidade(),
                            ingrediente.getEstoqueAtual(),
                            produtoIngrediente.getQuantidade()
                    );
                })
                .toList();

        BigDecimal precoPorUnidade = calcularPrecoPorUnidade(produto.getIngredientList());
        BigDecimal precoPorReceita = calcularPrecoPorProduto(precoPorUnidade, produto.getRendimento());

        return new ProdutoDTO(
                produto.getIdProduto(),
                produto.getNomeProduto(),
                produto.getCustoVenda(),
                produto.getRendimento(),
                produto.getTempoPreparacao(),
                ingredienteList,
                precoPorUnidade,
                precoPorReceita
        );
    }

    private BigDecimal calcularPrecoPorUnidade(List<ProdutoIngrediente> ingredienteList){
        BigDecimal custoTotal = BigDecimal.ZERO;

        for (ProdutoIngrediente produtoIngrediente : ingredienteList){
            BigDecimal valorUnidade = produtoIngrediente.getIngrediente().getPrecoPorUnidade();
            BigDecimal quantidade = produtoIngrediente.getQuantidade();

            BigDecimal custoIngrediente = valorUnidade.multiply(quantidade);
            custoTotal = custoTotal.add(custoIngrediente);
        }

        return custoTotal;
    }

    private BigDecimal calcularPrecoPorProduto(BigDecimal precoPorUnidade, Integer rendimento){
        return precoPorUnidade.multiply(BigDecimal.valueOf(rendimento));
    }

    public SimulacaoProdutoDTO simularProduto(@Valid CriarSimulacaoProdutoDTO dto) {
        Produto produto = this.buscarProdutoPorId(dto.idProduto());

        if (dto.lotes() <= 0) {throw new ResponseStatusException(BAD_REQUEST, "A quantidade de lotes deve ser maior que zero");}

        BigDecimal lotesDecimal = BigDecimal.valueOf(dto.lotes());

        List<SimulacaoIngredienteDTO> ingredientes = new ArrayList<>();

        int maximoLotes = Integer.MAX_VALUE;
        boolean ingredientesSuficientes = true;

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (ProdutoIngrediente produtoIngrediente : produto.getIngredientList()) {

            Ingrediente ingrediente = produtoIngrediente.getIngrediente();

            BigDecimal quantidadePorLote = produtoIngrediente.getQuantidade();
            BigDecimal quantidadeNecessaria = quantidadePorLote.multiply(lotesDecimal);

            BigDecimal estoqueAtual = BigDecimal.valueOf(ingrediente.getEstoqueAtual());
            BigDecimal saldoAposProducao = estoqueAtual.subtract(quantidadeNecessaria);

            boolean suficiente = estoqueAtual.compareTo(quantidadeNecessaria) >= 0;

            if (!suficiente) ingredientesSuficientes = false;

            int lotesPossiveis = estoqueAtual.divideToIntegralValue(quantidadePorLote).intValue();

            maximoLotes = Math.min(maximoLotes, lotesPossiveis);

            BigDecimal custoIngrediente = ingrediente.getPrecoPorUnidade().multiply(quantidadeNecessaria);
            custoTotal = custoTotal.add(custoIngrediente);

            ingredientes.add(
                    new SimulacaoIngredienteDTO(
                            ingrediente.getIdIngrediente(),
                            ingrediente.getNomeIngrediente(),
                            ingrediente.getCategoriaIngrediente(),
                            ingrediente.getUnidadeIngrediente(),
                            quantidadeNecessaria,
                            estoqueAtual,
                            saldoAposProducao,
                            suficiente
                    )
            );
        }

        return new SimulacaoProdutoDTO(
                produto.getIdProduto(),
                produto.getNomeProduto(),
                dto.lotes(),
                custoTotal,
                maximoLotes,
                ingredientesSuficientes,
                ingredientes
        );
    }
}
