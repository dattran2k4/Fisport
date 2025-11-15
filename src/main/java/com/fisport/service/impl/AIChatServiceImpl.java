package com.fisport.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisport.common.EIntentType;
import com.fisport.dto.ai.ActionPayload;
import com.fisport.dto.ai.AnalyzedIntent;
import com.fisport.dto.ai.ConversationContext;
import com.fisport.dto.ai.StreamResponse;
import com.fisport.service.AIChatService;
import com.fisport.service.ConversationContextService;
import com.fisport.service.SubFieldService;
import com.fisport.service.intent.IntentHandler;
import com.fisport.service.intent.IntentRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j(topic = "AI-CHAT-SERVICE")
public class AIChatServiceImpl implements AIChatService {

    private final ChatClient chatClient;

    private final Resource classifyIntentPrompt;

    private final IntentRouter router;

    private final Map<String, String> chatHistory = new ConcurrentHashMap<>();

    private final ConversationContextService contextService;

    private final ObjectMapper objectMapper;

    public AIChatServiceImpl(ChatClient.Builder builder,
                             @Value("classpath:prompts/classify-intent.st") Resource classifyIntentPrompt,
                             IntentRouter router,
                             ConversationContextService contextService,
                             ObjectMapper objectMapper) {
        this.chatClient = builder.build();
        this.classifyIntentPrompt = classifyIntentPrompt;
        this.router = router;
        this.contextService = contextService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<String> streamConversationalResponse(String userId, String userMessage) {
        log.info("Stream conversation for userId {}", userId);

        Optional<ConversationContext> contextOpt = contextService.getConversationContext(userId);
        EIntentType intent;

        if (contextOpt.isPresent()) {
            intent = contextOpt.get().getCurrentTopic();
            log.info("User [{}]: Continuing conversation with topic [{}]", userId, intent);

        } else {
            var outputConverter = new BeanOutputConverter<>(AnalyzedIntent.class);
            String history = chatHistory.getOrDefault(userId, "");

            PromptTemplate classifyTempt = new PromptTemplate(classifyIntentPrompt);
            Prompt classifyPrompt = classifyTempt.create(Map.of(
                    "user_message", userMessage,
                    "history", history.toString(),
                    "format", outputConverter.getFormat()
            ));

            log.debug("User [{}]: Calling classification AI...", userId);

            //String response = chatClient.prompt(classifyPrompt).call().content();
            //AnalyzedIntent analyzedIntent = outputConverter.convert(response);

            AnalyzedIntent analyzedIntent = chatClient.prompt(classifyPrompt).call().entity(AnalyzedIntent.class);

            intent = analyzedIntent.intent();

            log.info("User [{}]: Classified intent as [{}]", userId, intent);

            chatHistory.put(userId, history + "\nUser: " + userMessage);

        }

        IntentHandler handler = router.getHandler(intent);
        log.info("User [{}]: Routing to handler [{}]", userId, handler.getClass().getSimpleName());

        Flux<StreamResponse> responseStream =  handler.handle(userId, userMessage, contextOpt);

        ActionPayload endTurnAction = new ActionPayload("END_TURN", null);
        StreamResponse endTurnResponse = new StreamResponse("ACTION", null, endTurnAction);

        return responseStream
                .concatWith(Flux.just(endTurnResponse))
                .map(this::serializeResponse)
                .doOnError(e -> log.error("Error serializing StreamResponse", e));
    }

    private String serializeResponse(StreamResponse response) {
        try {
            log.info("Serializing StreamResponse");
            // Biến Object thành chuỗi JSON
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize response: {}", response, e);
            // Trả về lỗi dưới dạng JSON
            return "{\"type\": \"ERROR\", \"textChunk\": \"Lỗi xử lý máy chủ\"}";
        }
    }
}


