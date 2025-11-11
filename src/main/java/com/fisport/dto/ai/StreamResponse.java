package com.fisport.dto.ai;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StreamResponse {
    /**
     * Loại chunk:
     * - "TEXT": Một mẩu văn bản để hiển thị.
     * - "ACTION": Một tín hiệu yêu cầu FE hành động.
     * - "ERROR": Một tin nhắn lỗi.
     */
    private String type;

    /**
     * Nội dung (nếu type="TEXT")
     */
    private String textChunk;

    private ActionPayload action;

    public static StreamResponse text(String chunk) {
        return new StreamResponse("TEXT", chunk, null);
    }

    public static StreamResponse action(String actionName, Object data) {
        return new StreamResponse("ACTION", null, new ActionPayload(actionName, data));
    }

    public static StreamResponse error(String errorMessage) {
        return new StreamResponse("ERROR", errorMessage, null);
    }
}
