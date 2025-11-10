package com.fisport.service.impl;

import com.fisport.dto.ai.SearchCriteria;
import com.fisport.dto.ai.SubFieldForAIResponse;
import com.fisport.service.AIChatService;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "AI-CHAT-SERVICE")
public class AIChatServiceImpl implements AIChatService {

    private final ChatClient chatClient;
    private final SubFieldService subFieldService;

    private final Resource extractionPromptResource;
    private final Resource generationPromptResource;
    private final Resource generalPromptResource;

    private final Map<String, SearchCriteria> conversationState = new ConcurrentHashMap<>();

    public AIChatServiceImpl(ChatClient.Builder builder, SubFieldService subFieldService,
                             @Value("classpath:prompts/extract-criteria.st") Resource extractionPromptResource,
                             @Value("classpath:prompts/contextual-chat.st") Resource generationPromptResource,
                             @Value("classpath:prompts/general-chat.st") Resource generalPromptResource) {
        this.chatClient = builder.build();
        this.subFieldService = subFieldService;

        this.extractionPromptResource = extractionPromptResource;
        this.generationPromptResource = generationPromptResource;
        this.generalPromptResource = generalPromptResource;
    }

    @Override
    public Flux<String> streamConversationalResponse(String userId, String userMessage) {
        log.info("Stream conversation");

        // --- BƯỚC 1: TRÍCH XUẤT THÔNG TIN (BLOCKING CALL) ---
        SearchCriteria history = conversationState.getOrDefault(userId,
                new SearchCriteria(null, null, null, null, null));

        log.info("[USER: {}] Loaded history state: {}", userId, history);

        var outputConverter = new BeanOutputConverter<>(SearchCriteria.class);

        PromptTemplate extractionTpl = new PromptTemplate(extractionPromptResource);
        Prompt extractionPrompt = extractionTpl.create(Map.of(
                "user_message", userMessage,
                "history", history.toString(),
                "current_date", LocalDate.now().toString(),
                "format", outputConverter.getFormat()
        ));

        // Gọi AI lần 1 (blocking) để lấy SearchCriteria
        log.info("[USER: {}] Calling AI for criteria extraction...", userId);
        String rawOutput = chatClient.prompt(extractionPrompt).call().content();
        log.info("[USER: {}] AI Raw Output (Extraction): {}", userId, rawOutput);

        SearchCriteria newCriteria = outputConverter.convert(rawOutput);
        log.info("[USER: {}] AI Parsed Criteria: {}", userId, newCriteria);

        // --- BƯỚC 2: PHÂN LOẠI VÀ HÀNH ĐỘNG ---

        if (newCriteria.isGeneralChat()) {
            // Trường hợp 1: Nói chuyện phiếm
            log.info("[USER: {}] CLASSIFICATION: General Chat. Removing state.", userId);
            conversationState.remove(userId);
            return callStreamingGeneration(generalPromptResource, Map.of("user_message", userMessage));

        } else if (newCriteria.isReadyForSearch()) {
            // Trường hợp 2: Đủ thông tin, tìm sân và trả lời (streaming)
            log.info("[USER: {}] CLASSIFICATION: Ready for Search. Removing state.", userId);
            conversationState.remove(userId);

            List<SubFieldForAIResponse> subFields = subFieldService.findAvailableSubFields(newCriteria);
            log.info("[USER: {}] Found {} available subfields.", userId, subFields.size());

            String context = formatSubFieldsAsContext(subFields);

            log.info("[USER: {}] Calling AI for streaming generation (with context)...", userId);
            return callStreamingGeneration(generationPromptResource, Map.of(
                    "context", context,
                    "question", userMessage
            ));


        } else {
            // Trường hợp 3: Thiếu thông tin, hỏi lại
            log.info("[USER: {}] CLASSIFICATION: Needs Follow-up. Saving state.", userId);
            conversationState.put(userId, newCriteria);

            String followUp = newCriteria.followUpQuestion();
            log.info("[USER: {}] Returning follow-up question: \"{}\"", userId, followUp);

            return Flux.just(newCriteria.followUpQuestion());
        }
    }

    private Flux<String> callStreamingGeneration(Resource promptResource, Map<String, Object> variables) {
        PromptTemplate promptTemplate = new PromptTemplate(promptResource);
        Prompt prompt = promptTemplate.create(variables);

        return chatClient.prompt(prompt)
                .stream()
                .content();
    }

    private String formatSubFieldsAsContext(List<SubFieldForAIResponse> subFields) {
        if (subFields == null || subFields.isEmpty()) {
            return "Rất tiếc, không tìm thấy sân con nào trống theo yêu cầu.";
        }

        // Giới hạn 7 sân để tránh context quá dài (tiết kiệm token)
        return subFields.stream()
                .limit(2)
                .map(SubFieldForAIResponse::toString)
                .collect(Collectors.joining("\n"));
    }
}
