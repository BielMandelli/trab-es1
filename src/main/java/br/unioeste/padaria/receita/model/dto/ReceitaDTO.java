package br.unioeste.padaria.receita.model.dto;

import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record ReceitaDTO(
        Long idReceita,
        String nomeReceita,
        Integer rendimento,
        Integer tempoPreparacao,
        List<ReceitaIngredienteDTO> ingredientes,
        BigDecimal custoPorUnidade,
        BigDecimal custoPorReceita
) {

}
