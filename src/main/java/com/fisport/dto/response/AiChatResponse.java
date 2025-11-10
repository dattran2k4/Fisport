package com.fisport.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class AiChatResponse {
    private String intent;
    private String message;
    private Object data;
}
