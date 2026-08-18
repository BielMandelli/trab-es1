package br.unioeste.padaria.unidade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AtualizarUnidadeMedidaDTO(
        String nomeUnidadeMedida,
        String abreviacao
) {

}
