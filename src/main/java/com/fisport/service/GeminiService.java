package com.fisport.service;

import com.fisport.dto.request.ChatRequest;

public interface GeminiService {
    String generation(ChatRequest request);
}
