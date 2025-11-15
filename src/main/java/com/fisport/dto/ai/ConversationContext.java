package com.fisport.dto.ai;

import com.fisport.common.EIntentType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationContext {

    private EIntentType currentTopic;

    private Map<String, Object> data;
}
