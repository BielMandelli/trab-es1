package br.unioeste.padaria.assistente.dto;

public record IntencaoAssistenteDTO(
        String acao,
        String nomeReceita,
        Long idReceita,
        String nomeIngrediente
) {
}

