package br.unioeste.padaria.receita.model.dto;

import br.unioeste.padaria.unidade.model.UnidadeMedida;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record SimulacaoReceitaDTO(
        Long idReceita,
        String nomeReceita,
        UnidadeMedida unidadeMedida,
        Integer lotes,
        BigDecimal custoEstimado,
        Integer maximoLotesPossivel,
        boolean ingredientesSuficientes,
        List<SimulacaoIngredienteDTO> ingredientes
) {
}