package com.fisport.service.impl;

import com.fisport.dto.request.ChatRequest;
import com.fisport.service.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "GEMINI-SERVICE")
public class GeminiServiceImpl implements GeminiService {

    private final ChatClient chatClient;

    public  GeminiServiceImpl(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public String generation(ChatRequest request) {
        log.info("Chat generation");

        return chatClient.prompt()
                .user(request.getMessage())
                .call()
                .content();
    }
}
