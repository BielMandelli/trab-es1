package br.unioeste.padaria.receita.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record SimulacaoReceitaDTO(
        Long idReceita,
        String nomeReceita,
        Integer lotes,
        BigDecimal custoEstimado,
        Integer maximoLotesPossivel,
        boolean ingredientesSuficientes,
        List<SimulacaoIngredienteDTO> ingredientes
) {
}