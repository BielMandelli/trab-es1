package br.unioeste.padaria.assistant.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDTO(
        @NotBlank
        String message
) {
}

