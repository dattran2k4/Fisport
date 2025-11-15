package com.fisport.api;

import com.fisport.dto.ai.StreamResponse;
import com.fisport.dto.request.ChatRequest;
import com.fisport.service.AIChatService;
import com.fisport.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
@Validated
@Slf4j(topic = "CHATBOT-API-CONTROLLER")
@RequestMapping("/api/ai")
public class ChatBotApiController {

    private final AIChatService aiChatService;

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam("message") String message) {
        log.info("ChatBotApiController request: ");

        return aiChatService.streamConversationalResponse("1L", message);
    }
}
