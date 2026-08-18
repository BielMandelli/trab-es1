package br.unioeste.padaria.produto.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record SimulacaoProdutoDTO(
        Long idProduto,
        String nomeProduto,
        Integer lotes,
        BigDecimal custoEstimado,
        Integer maximoLotesPossivel,
        boolean ingredientesSuficientes,
        List<SimulacaoIngredienteDTO> ingredientes
) {
}