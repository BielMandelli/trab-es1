package br.unioeste.padaria.assistant.dto;

import jakarta.validation.constraints.NotBlank;

public record RequisicaoChatDTO(
        @NotBlank
        String mensagem
) {
}

