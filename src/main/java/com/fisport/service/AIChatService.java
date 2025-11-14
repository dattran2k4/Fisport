package com.fisport.service;

import reactor.core.publisher.Flux;

public interface AIChatService {
    Flux<String> streamConversationalResponse(String userId, String userMessage);
}
