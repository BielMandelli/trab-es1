package br.unioeste.padaria.assistant.controller;

import br.unioeste.padaria.assistant.dto.ChatRequestDTO;
import br.unioeste.padaria.assistant.dto.ChatResponseDTO;
import br.unioeste.padaria.assistant.service.AssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private final AssistantService assistantService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(
            @Valid @RequestBody ChatRequestDTO dto) {
        return ResponseEntity.ok(assistantService.chat(dto.message()));
    }
}

