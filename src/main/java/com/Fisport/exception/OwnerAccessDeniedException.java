package com.fisport.exception;

/**
 * Exception khi Owner không có quyền truy cập tài nguyên
 * Sử dụng riêng cho Owner service management
 */
public class OwnerAccessDeniedException extends RuntimeException {
    public OwnerAccessDeniedException(String message) {
        super(message);
    }
}

