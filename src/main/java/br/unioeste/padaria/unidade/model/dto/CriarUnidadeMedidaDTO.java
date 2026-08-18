package br.unioeste.padaria.unidade.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarUnidadeMedidaDTO(
        @NotNull
        @NotEmpty
        String nomeUnidadeMedida,
        @NotNull
        @NotEmpty
        String abreviacao
) {

}
