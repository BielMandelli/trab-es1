package br.unioeste.padaria.assistant.dto;

public record IntencaoAssistenteDTO(
        String acao,
        String nomeReceita,
        Long idReceita,
        String nomeIngrediente
) {
}

