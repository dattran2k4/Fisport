package com.fisport.service;

import com.fisport.dto.ai.ConversationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationContextService {

    private final Map<String, ConversationContext> contextMap = new ConcurrentHashMap<>();

    public Optional<ConversationContext> getConversationContext(String userId) {
        return Optional.ofNullable(contextMap.get(userId));
    }

    public void setContext(String userId, ConversationContext conversationContext) {
        contextMap.put(userId, conversationContext);
    }

    public void clearContext(String userId) {
        contextMap.remove(userId);
    }
}
