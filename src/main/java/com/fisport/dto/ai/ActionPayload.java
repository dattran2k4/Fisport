package com.fisport.dto.ai;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActionPayload {

    private String actionName;
    private Object data;
}
