package br.unioeste.padaria.assistant.service;

import br.unioeste.padaria.assistant.dto.AssistantIntent;
import br.unioeste.padaria.assistant.dto.ChatResponseDTO;
import br.unioeste.padaria.receita.model.entity.Receita;
import br.unioeste.padaria.receita.model.entity.ReceitaIngrediente;
import br.unioeste.padaria.receita.service.ReceitaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private static final Logger logger = LoggerFactory.getLogger(AssistantService.class);

    private static final String SYSTEM_PROMPT = """
            You are an intent classifier for a bakery system. Reply ONLY with valid JSON, without markdown.
            Use exactly these keys: action, recipeName, recipeId, ingredientName.
            The action must be one of: FIND_RECIPE_BY_NAME, FIND_RECIPE_BY_ID, LIST_RECIPES,
            FIND_INGREDIENT_BY_NAME, LIST_INGREDIENTS, UNKNOWN.
            Use null for data that is not present. Interpret Portuguese user messages.
            """;

    private final ReceitaService receitaController;
    private final br.unioeste.padaria.ingredient.service.IngredienteService ingredienteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-3.5-flash}")
    private String model;

    public ChatResponseDTO chat(String userMessage) {
        AssistantIntent intent = interpret(userMessage);
        String response = switch (intent.action()) {
            case "FIND_RECIPE_BY_NAME" -> findRecipeByName(intent.recipeName());
            case "FIND_RECIPE_BY_ID" -> findRecipeById(intent.recipeId());
            case "LIST_RECIPES" -> listRecipes();
            case "FIND_INGREDIENT_BY_NAME" -> findIngredientByName(intent.ingredientName());
            case "LIST_INGREDIENTS" -> listIngredients();
            default -> "Não entendi o pedido. Você pode pedir uma receita ou um ingrediente pelo nome.";
        };
        return new ChatResponseDTO(intent.action(), response);
    }

    private AssistantIntent interpret(String userMessage) {
        if (apiKey.isBlank()) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "Configure the GEMINI_API_KEY environment variable to use the assistant");
        }

        String body;
        try {
            body = restClient.post()
                    .uri("/v1beta/models/" + model + ":generateContent")
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .body("""
                            {
                              "system_instruction": {
                                "parts": [{"text": %s}]
                              },
                              "contents": [{
                                "role": "user",
                                "parts": [{"text": %s}]
                              }],
                              "generationConfig": {
                                "response_mime_type": "application/json",
                                "response_schema": {
                                  "type": "OBJECT",
                                  "properties": {
                                    "action": {
                                      "type": "STRING",
                                      "enum": ["FIND_RECIPE_BY_NAME", "FIND_RECIPE_BY_ID", "LIST_RECIPES", "FIND_INGREDIENT_BY_NAME", "LIST_INGREDIENTS", "UNKNOWN"]
                                    },
                                    "recipeName": {"type": "STRING", "nullable": true},
                                    "recipeId": {"type": "INTEGER", "nullable": true},
                                    "ingredientName": {"type": "STRING", "nullable": true}
                                  },
                                  "required": ["action", "recipeName", "recipeId", "ingredientName"],
                                  "propertyOrdering": ["action", "recipeName", "recipeId", "ingredientName"]
                                },
                                "max_output_tokens": 1024
                              }
                            }
                            """.formatted(
                            toJson(SYSTEM_PROMPT),
                            toJson(userMessage)
                    ))
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException exception) {
            logger.error("Gemini returned HTTP {}: {}", exception.getStatusCode(),
                    exception.getResponseBodyAsString());
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "Gemini API is unavailable. Check the application console for details.", exception);
        } catch (RestClientException exception) {
            logger.error("Could not call Gemini API", exception);
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "Gemini API is unavailable. Check the application console for details.", exception);
        }

        logger.info("Gemini raw response: {}", body);
        try {
            JsonNode response = objectMapper.readTree(body);
            String text = extractOutputText(response);
            return objectMapper.readValue(text, AssistantIntent.class);
        } catch (JsonProcessingException exception) {
            logger.error("Gemini response could not be parsed: {}", body, exception);
            throw new ResponseStatusException(SERVICE_UNAVAILABLE,
                    "The AI response could not be interpreted. Check the application console for details.", exception);
        }
    }

    private String findRecipeByName(String name) {
        if (name == null || name.isBlank()) {
            return "Informe o nome da receita que deseja consultar.";
        }
        Page<Receita> recipes = receitaController.findAll(name, PageRequest.of(0, 10));
        if (recipes.isEmpty()) {
            return "Não encontrei uma receita com o nome " + name + ".";
        }
        return recipes.getContent().stream().map(this::formatRecipe).collect(Collectors.joining("\n"));
    }

    private String findRecipeById(Long id) {
        if (id == null) {
            return "Informe o código da receita que deseja consultar.";
        }
        return formatRecipe(receitaController.buscarReceitaPorId(id));
    }

    private String listRecipes() {
        Page<Receita> recipes = receitaController.findAll(null, PageRequest.of(0, 10));
        if (recipes.isEmpty()) {
            return "Não há receitas cadastradas.";
        }
        return "Receitas cadastradas: " + recipes.getContent().stream()
                .map(Receita::getName)
                .collect(Collectors.joining(", ")) + ".";
    }

    private String findIngredientByName(String name) {
        if (name == null || name.isBlank()) {
            return "Informe o nome do ingrediente que deseja consultar.";
        }
        Page<br.unioeste.padaria.ingredient.model.entity.Ingrediente> ingredients = ingredienteService.findAll(name, null, PageRequest.of(0, 10));
        if (ingredients.isEmpty()) {
            return "Não encontrei um ingrediente com o nome " + name + ".";
        }
        return ingredients.getContent().stream().map(this::formatIngredient).collect(Collectors.joining("\n"));
    }

    private String listIngredients() {
        Page<br.unioeste.padaria.ingredient.model.entity.Ingrediente> ingredients = ingredienteService.findAll(null, null, PageRequest.of(0, 10));
        if (ingredients.isEmpty()) {
            return "Não há ingredientes cadastrados.";
        }
        return "Ingredientes cadastrados: " + ingredients.getContent().stream()
                .map(br.unioeste.padaria.ingredient.model.entity.Ingrediente::getName)
                .collect(Collectors.joining(", ")) + ".";
    }

    private String formatRecipe(Receita receita) {
        String ingredients = receita.getIngredientList().isEmpty()
                ? "Nenhum ingrediente cadastrado."
                : receita.getIngredientList().stream()
                .map(this::formatRecipeIngredient)
                .collect(Collectors.joining(", "));

        return "A receita " + receita.getName() + " custa R$ " + receita.getSellingPrice()
                + " e possui os ingredientes: " + ingredients;
    }

    private String formatRecipeIngredient(ReceitaIngrediente item) {
        String abbreviation = item.getIngrediente().getIngredientUnit().getAbbreviation();
        return item.getIngrediente().getName() + ": " + item.getQuantity()
                + (abbreviation == null || abbreviation.isBlank() ? "" : " " + abbreviation);
    }

    private String formatIngredient(br.unioeste.padaria.ingredient.model.entity.Ingrediente ingrediente) {
        return ingrediente.getName() + ", categoria " + ingrediente.getIngredientCategory().getName()
                + ", preço por unidade R$ " + ingrediente.getPricePerUnit() + ".";
    }

    private String extractOutputText(JsonNode response) {
        String text = response.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();
        if (text.isBlank()) {

            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "The AI did not return a text response");
        }
        return text;
    }

    private String toJson(String value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not create AI request", exception);
        }
    }
}

