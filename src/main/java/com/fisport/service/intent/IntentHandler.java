package com.fisport.service.intent;

import com.fisport.common.EIntentType;
import com.fisport.dto.ai.ConversationContext;
import com.fisport.dto.ai.StreamResponse;
import reactor.core.publisher.Flux;

import java.util.Optional;

public interface IntentHandler {
    /**
     * Xử lý tin nhắn của người dùng dựa trên ý định này.
     * @param userId ID người dùng
     * @param userMessage Tin nhắn gốc
     * @param contextOpt Trạng thái hội thoại (nếu có)
     * @return Một Flux<String> chứa phản hồi (streaming)
     */
    Flux<StreamResponse> handle(String userId, String userMessage, Optional<ConversationContext> contextOpt);

    /**
     * Trả về loại ý định mà Handler này xử lý.
     */
    EIntentType getIntentType();
}
