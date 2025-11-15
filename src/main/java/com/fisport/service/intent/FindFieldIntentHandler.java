package com.fisport.service.intent;

import com.fisport.common.EIntentType;
import com.fisport.dto.ai.*;
import com.fisport.service.ConversationContextService;
import com.fisport.service.FieldService;
import com.fisport.service.SubFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "FIND-FIELD-INTENT-HANDLER")
public class FindFieldIntentHandler implements IntentHandler {

    private final ChatClient chatClient;

    private final SubFieldService subFieldService;

    private final Resource findFieldPrompt;

    private final Resource contextualPrompt;

    private final ConversationContextService contextService;

    private final Map<String, SearchCriteria> state = new ConcurrentHashMap<>();

    public FindFieldIntentHandler(ChatClient.Builder chatClientBuilder, SubFieldService subFieldService,
                                  @Value("classpath:prompts/find-field.st") Resource findFieldPrompt,
                                  @Value("classpath:prompts/contextual-chat.st") Resource contextualPrompt,
                                  ConversationContextService contextService) {
        this.chatClient = chatClientBuilder.build();
        this.subFieldService = subFieldService;
        this.findFieldPrompt = findFieldPrompt;
        this.contextualPrompt = contextualPrompt;
        this.contextService = contextService;
    }

    @Override
    public Flux<StreamResponse> handle(String userId, String userMessage, Optional<ConversationContext> contextOpt) {
        log.info("Finding Field AI Intent");

        log.info("[USER: {}] Loaded history state: {}", userId, state);
        SearchCriteria history = state.getOrDefault(userId, new SearchCriteria(null, null, null, null, null));
        var converter = new BeanOutputConverter<>(SearchCriteria.class);

        PromptTemplate temp = new PromptTemplate(findFieldPrompt);

        Prompt prompt = temp.create(Map.of(
                "user_message", userMessage,
                "history", history.toString(),
                "current_date", LocalDate.now().toString(),
                "format", converter.getFormat()
        ));

        SearchCriteria searchCriteria = chatClient.prompt(prompt)
                .call()
                .entity(converter);

        if (searchCriteria.isReadyForSearch()) {

            state.remove(userId);

            List<SubFieldForAIResponse> subFieldForAIResponses = subFieldService.findAvailableSubFields(searchCriteria);

            String context = formatSubFieldsAsContext(subFieldForAIResponses);

            PromptTemplate genTpl = new PromptTemplate(contextualPrompt);
            Prompt finalPrompt = genTpl.create(Map.of(
                    "context", context,
                    "question", userMessage
            ));



            Flux<String> textStream = chatClient.prompt(finalPrompt)
                    .stream()
                    .content();

            Flux<StreamResponse> textResponseStream = textStream
                    .map(StreamResponse::text);

            ActionPayload action = new ActionPayload("OPEN_BOOKING_MODAL",
                    Map.of("foundFields", subFieldForAIResponses,
                            "criteria", searchCriteria));

            StreamResponse actionResponse = StreamResponse.action("OPEN_BOOKING_MODAL", action);
            log.info("[USER: {}] Found AI Intent: {}", userId, actionResponse);

            return Flux.concat(textResponseStream, Flux.just(actionResponse));

        } else {
            state.put(userId, searchCriteria); // Cập nhật trạng thái
            return Flux.just(StreamResponse.text(searchCriteria.followUpQuestion()));
        }
    }

    @Override
    public EIntentType getIntentType() {
        return EIntentType.FIND_FIELD;
    }

    private String formatSubFieldsAsContext(List<SubFieldForAIResponse> subFields) {
        if (subFields == null || subFields.isEmpty()) {
            return "Rất tiếc, tôi không tìm thấy sân con nào trống theo yêu cầu.";
        }

        return subFields.stream()
                .limit(2)
                .map(SubFieldForAIResponse::toString)
                .collect(Collectors.joining("\n"));
    }
}


