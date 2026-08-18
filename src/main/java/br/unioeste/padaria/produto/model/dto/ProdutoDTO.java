package br.unioeste.padaria.produto.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProdutoDTO(
        Long idReceita,
        String nomeReceita,
        BigDecimal precoVenda,
        Integer rendimento,
        Integer tempoPreparacao,
        List<ProdutoIngredienteDTO> ingredientes,
        BigDecimal custoPorUnidade,
        BigDecimal custoPorProduto
) {

}
