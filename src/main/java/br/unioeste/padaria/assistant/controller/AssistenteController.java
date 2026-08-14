package br.unioeste.padaria.assistant.controller;

import br.unioeste.padaria.assistant.dto.RequisicaoChatDTO;
import br.unioeste.padaria.assistant.dto.RespostaChatDTO;
import br.unioeste.padaria.assistant.service.AssistenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/assistente")
public class AssistenteController {

    private final AssistenteService assistenteService;

    @PostMapping("/chat")
    public ResponseEntity<RespostaChatDTO> chat(
            @Valid @RequestBody RequisicaoChatDTO dto) {
        return ResponseEntity.ok(assistenteService.chat(dto.mensagem()));
    }
}

