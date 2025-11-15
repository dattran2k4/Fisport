package com.fisport.dto.ai;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fisport.common.EIntentType;

public record AnalyzedIntent(
        @JsonPropertyDescription("Ý định chính của người dùng, phải là một trong các giá trị: " +
                "'FIND_FIELD', 'GENERAL_CHAT'")
        EIntentType intent
) {}
