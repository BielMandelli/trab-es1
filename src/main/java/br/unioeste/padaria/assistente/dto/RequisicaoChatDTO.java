package br.unioeste.padaria.assistente.dto;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoChatDTO(
        @NotBlank
        String mensagem
) {
}

