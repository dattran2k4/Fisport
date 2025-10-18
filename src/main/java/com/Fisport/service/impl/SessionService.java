package com.Fisport.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final HttpSession session;

    public void set(String key, Object value) {
        session.setAttribute(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = session.getAttribute(key);
        if (value == null) return null;
        return type.cast(value);
    }

    public void remove(String key) {
        session.removeAttribute(key);
    }

    public void clear() {
        session.invalidate();
    }

    public boolean has(String key) {
        return session.getAttribute(key) != null;
    }

    public String getSessionId() {
        return session.getId();
    }
}
