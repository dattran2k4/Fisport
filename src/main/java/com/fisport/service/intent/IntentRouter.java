package com.fisport.service.intent;

import com.fisport.common.EIntentType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IntentRouter {

    private final Map<EIntentType, IntentHandler> handlerMap;

    /**
     * Tự động tiêm (inject) TẤT CẢ các beans
     * triển khai IntentHandler
     * và tạo một Map để tra cứu nhanh.
     */
    public IntentRouter(List<IntentHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IntentHandler::getIntentType,
                        Function.identity()
                ));
    }

    /**
     * Tìm handler phù hợp
     */
    public IntentHandler getHandler(EIntentType intentType) {
        IntentHandler handler = handlerMap.get(intentType);
        if (handler == null) {
            // Luôn trả về GeneralChat nếu không tìm thấy
            return handlerMap.get(EIntentType.GENERAL_CHAT);
        }
        return handler;
    }
}
