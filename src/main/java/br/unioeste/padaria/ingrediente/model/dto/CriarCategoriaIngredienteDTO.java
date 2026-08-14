package br.unioeste.padaria.ingrediente.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CriarCategoriaIngredienteDTO(
        @NotNull
        @NotEmpty
        String nomeCategoria
) {

}
