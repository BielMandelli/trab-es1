package br.unioeste.padaria.assistant.dto;

public record AssistantIntent(
        String action,
        String recipeName,
        Long recipeId,
        String ingredientName
) {
}

