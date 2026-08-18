package br.unioeste.padaria.assistant.service;

import br.unioeste.padaria.assistant.dto.IntencaoAssistenteDTO;
import br.unioeste.padaria.assistant.dto.RespostaChatDTO;
import br.unioeste.padaria.ingrediente.model.entity.Ingrediente;
import br.unioeste.padaria.ingrediente.service.IngredienteService;
import br.unioeste.padaria.receita.model.dto.ReceitaDTO;
import br.unioeste.padaria.receita.model.dto.ReceitaIngredienteDTO;
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
public class AssistenteService {

    private static final Logger logger = LoggerFactory.getLogger(AssistenteService.class);

    private static final String PROMPT_BASE = """
            You are an intent classifier for a bakery system. Reply ONLY with valid JSON, without markdown.
            Use exactly these keys: acao, nomeReceita, idReceita, nomeIngrediente.
            The acao must be one of: ENCONTRAR_RECEITA_PELO_NOME, ENCONTRAR_RECEITA_PELO_ID, LISTAR_RECEITAS,
            ENCONTRAR_INGREDIENTE_PELO_NOME, LISTAR_INGREDIENTES, DESCONHECIDO.
            Use null for data that is not present. Interpret Portuguese user messages.
            """;
    private static final String URL_IA = "https://generativelanguage.googleapis.com";

    private final ReceitaService receitaService;
    private final IngredienteService ingredienteService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestClient restClient = RestClient.builder().baseUrl(URL_IA).build();

    @Value("${gemini.api-key}")
    private String chaveDaApiIa;

    @Value("${gemini.model:gemini-3.5-flash}")
    private String modeloIa;

    public RespostaChatDTO chat(String mensagemUsuario) {
        IntencaoAssistenteDTO intent = interpretar(mensagemUsuario);
        String response = switch (intent.acao()) {
            case "ENCONTRAR_RECEITA_PELO_NOME" -> encontrarReceitaPeloNome(intent.nomeReceita());
            case "ENCONTRAR_RECEITA_PELO_ID" -> encontrarReceitaPeloId(intent.idReceita());
            case "LISTAR_RECEITAS" -> listarReceitas();
            case "ENCONTRAR_INGREDIENTE_PELO_NOME" -> encontrarIngredientePeloNome(intent.nomeIngrediente());
            case "LISTAR_INGREDIENTES" -> listarIngredientes();
            default -> "Não entendi o pedido. Você pode pedir uma receita ou um ingrediente pelo nome.";
        };
        return new RespostaChatDTO(intent.acao(), response);
    }

    private IntencaoAssistenteDTO interpretar(String mensagemUsuario) {
        if (chaveDaApiIa.isBlank()) {
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Configure a chave da API usando variáveis de ambiente para usar a assistente");
        }

        String corpo;
        try {
            corpo = restClient.post()
                    .uri("/v1beta/models/" + modeloIa + ":generateContent")
                    .header("x-goog-api-key", chaveDaApiIa)
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
                                    "acao": {
                                      "type": "STRING",
                                      "enum": ["ENCONTRAR_RECEITA_PELO_NOME", "ENCONTRAR_RECEITA_PELO_ID", "LISTAR_RECEITAS", "ENCONTRAR_INGREDIENTE_PELO_NOME", "LISTAR_INGREDIENTES", "DESCONHECIDO"]
                                    },
                                    "nomeReceita": {"type": "STRING", "nullable": true},
                                    "idReceita": {"type": "INTEGER", "nullable": true},
                                    "nomeIngrediente": {"type": "STRING", "nullable": true}
                                  },
                                  "required": ["acao", "nomeReceita", "idReceita", "nomeIngrediente"],
                                  "propertyOrdering": ["acao", "nomeReceita", "idReceita", "nomeIngrediente"]
                                },
                                "max_output_tokens": 1024
                              }
                            }
                            """.formatted(
                            converterParaJson(PROMPT_BASE),
                            converterParaJson(mensagemUsuario)
                    ))
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException exception) {
            logger.error("Gemini retornou HTTP {}: {}", exception.getStatusCode(), exception.getResponseBodyAsString());
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Gemini API está inalcançavel. Olhe o console da aplicação para detalhes.", exception);
        } catch (RestClientException exception) {
            logger.error("Não foi possível chamar o Gemini", exception);
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "Gemini API está inalcançavel. Olhe o console da aplicação para detalhes.", exception);
        }

        logger.info("Reposta do Gemini: {}", corpo);
        try {
            JsonNode resposta = objectMapper.readTree(corpo);
            String texto = extrairTexto(resposta);
            return objectMapper.readValue(texto, IntencaoAssistenteDTO.class);
        } catch (JsonProcessingException exception) {
            logger.error("A resposta do Gemini não pode ser interpretada: {}", corpo, exception);
            throw new ResponseStatusException(SERVICE_UNAVAILABLE, "A Resposta do Gemini não pode ser interpretada. Olhe o console da aplicação para detalhes.", exception);
        }
    }

    private String encontrarReceitaPeloNome(String nomeReceita) {
        if (nomeReceita == null || nomeReceita.isBlank()) return "Informe o nome da receita que deseja consultar.";

        Page<ReceitaDTO> recipes = receitaService.listarTodasReceitas(nomeReceita, PageRequest.of(0, 10));

        if (recipes.isEmpty()) return "Não encontrei uma receita com o nome " + nomeReceita + ".";

        return recipes.getContent().stream().map(this::formatarReceitaDTO).collect(Collectors.joining("\n"));
    }

    private String encontrarReceitaPeloId(Long idReceita) {
        if (idReceita == null) return "Informe o código da receita que deseja consultar.";

        return formatarReceitaDTO(receitaService.formatarReceita(receitaService.buscarReceitaPorId(idReceita)));
    }

    private String listarReceitas() {
        Page<ReceitaDTO> recipes = receitaService.listarTodasReceitas(null, PageRequest.of(0, 10));

        if (recipes.isEmpty()) return "Não há receitas cadastradas.";

        return "Receitas cadastradas: " + recipes.getContent().stream().map(ReceitaDTO::nomeReceita).collect(Collectors.joining(", ")) + ".";
    }

    private String encontrarIngredientePeloNome(String nomeIngrediente) {
        if (nomeIngrediente == null || nomeIngrediente.isBlank()) return "Informe o nome do ingrediente que deseja consultar.";

        Page<Ingrediente> ingredients = ingredienteService.listarTodosIngredientes(nomeIngrediente, null, PageRequest.of(0, 10));

        if (ingredients.isEmpty()) return "Não encontrei um ingrediente com o nome " + nomeIngrediente + ".";

        return ingredients.getContent().stream().map(this::formatarIngrediente).collect(Collectors.joining("\n"));
    }

    private String listarIngredientes() {
        Page<Ingrediente> ingredients = ingredienteService.listarTodosIngredientes(null, null, PageRequest.of(0, 10));

        if (ingredients.isEmpty()) return "Não há ingredientes cadastrados.";

        return "Ingredientes cadastrados: " + ingredients.getContent().stream()
                .map(Ingrediente::getNomeIngrediente)
                .collect(Collectors.joining(", ")) + ".";
    }

    private String formatarReceitaDTO(ReceitaDTO receitaDTO) {
        String ingredients = receitaDTO.ingredientes().isEmpty()
                ? "Nenhum ingrediente cadastrado."
                : receitaDTO.ingredientes().stream()
                .map(this::formatarReceitaIngrediente)
                .collect(Collectors.joining(", "));

        return "A receita " + receitaDTO.nomeReceita() + " custa R$ " + receitaDTO.custoPorReceita()
                + " e possui os ingredientes: " + ingredients;
    }

    private String formatarReceitaIngrediente(ReceitaIngrediente receitaIngredienteDTO) {
        String abbreviation = receitaIngredienteDTO.getIngrediente().getUnidadeMedida().getAbreviacao();
        return receitaIngredienteDTO.getIngrediente().getNomeIngrediente() + ": " + receitaIngredienteDTO.getQuantidade()
                + (abbreviation == null || abbreviation.isBlank() ? "" : " " + abbreviation);
    }

    private String formatarReceitaIngrediente(ReceitaIngredienteDTO receitaIngredienteDTO) {
        String abbreviation = receitaIngredienteDTO.unidadeMedida().getAbreviacao();
        return receitaIngredienteDTO.nomeIngrediente() + ": " + receitaIngredienteDTO.quantidade()
                + (abbreviation == null || abbreviation.isBlank() ? "" : " " + abbreviation);
    }

    private String formatarIngrediente(Ingrediente ingrediente) {
        return ingrediente.getNomeIngrediente() + ", categoria " + ingrediente.getCategoriaIngrediente().getNomeCategoria()
                + ", preço por unidade R$ " + ingrediente.getCustoPorUnidade() + ".";
    }

    private String extrairTexto(JsonNode response) {
        String text = response.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();

        if (text.isBlank()) throw new ResponseStatusException(SERVICE_UNAVAILABLE, "O Gemini não retornou um texto de resposta");

        return text;
    }

    private String converterParaJson(String valor) {
        try {
            return objectMapper.writeValueAsString(valor);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Não foi possível fazer a requisição para a Ia", exception);
        }
    }
}

