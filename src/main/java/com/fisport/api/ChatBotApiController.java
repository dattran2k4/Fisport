package com.fisport.api;

import com.fisport.dto.request.ChatRequest;
import com.fisport.service.AIChatService;
import com.fisport.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j(topic = "CHATBOT-API-CONTROLLER")
@RequestMapping("/api/ai")
public class ChatBotApiController {

    private final GeminiService geminiService;
    private final AIChatService aiChatService;

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody ChatRequest request) {
        log.info("ChatBotApiController request: {}", request);
        return aiChatService.streamConversationalResponse("1L", request.getMessage());
    }
}
