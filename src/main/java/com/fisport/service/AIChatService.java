package com.fisport.service;

import com.fisport.dto.ai.StreamResponse;
import reactor.core.publisher.Flux;

public interface AIChatService {
    Flux<String> streamConversationalResponse(String userId, String userMessage);
}
