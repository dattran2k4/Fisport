package com.fisport.service.intent;

import com.fisport.common.EIntentType;
import com.fisport.dto.ai.ConversationContext;
import com.fisport.dto.ai.StreamResponse;
import com.fisport.service.ConversationContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j(topic = "GENERAL-INTENT-HANDLER")
public class GeneralIntentHandler implements IntentHandler{

    private final ChatClient chatClient;
    private final Resource generalChatResource;
    private final ConversationContextService contextService;

    public GeneralIntentHandler(ChatClient.Builder builder,
                                @Value("classpath:prompts/general-chat.st") Resource generalChatResource,
                                ConversationContextService contextService) {
        this.chatClient = builder.build();
        this.generalChatResource = generalChatResource;
        this.contextService = contextService;
    }

    @Override
    public Flux<StreamResponse> handle(String userId, String userMessage, Optional<ConversationContext> contextOpt) {
        log.info("General chat handler for userId [{}]", userId);

        contextService.clearContext(userId);

        // Cách viết clean sử dụng param và text (String) template
        return chatClient.prompt()
                .user(spec -> spec
                        .text(generalChatResource)
                        .param("user_message", userMessage))
                .stream()
                .content()
                .map(StreamResponse::text);
    }

    @Override
    public EIntentType getIntentType() {
        return EIntentType.GENERAL_CHAT;
    }
}
