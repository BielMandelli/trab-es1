package br.unioeste.padaria.produto.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarProdutoIngredienteDTO(
        @NotNull
        Long idIngrediente,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal quantidade
) {
}
