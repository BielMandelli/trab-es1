package br.unioeste.padaria.ingrediente.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarUnidadeIngredienteDTO(
        @NotNull
        @NotEmpty
        String nomeUnidade,
        @NotNull
        @NotEmpty
        String abreviacaoUnidade
) {

}
