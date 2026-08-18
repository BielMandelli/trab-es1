package br.unioeste.padaria.ingrediente.model.dto;

import java.math.BigDecimal;

public record IngredienteInfoDTO(
        Integer estoqueBaixo,
        BigDecimal valorTotalEstoque
) {
}
