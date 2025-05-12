package com.restful.quanlysinhvien.util.error;

/**
 * Ngoại lệ được ném ra khi phát hiện tài nguyên bị trùng lặp.(409 Conflict)
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Khởi tạo ngoại lệ với thông điệp lỗi cụ thể.
     *
     * @param message thông điệp mô tả lỗi khi tài nguyên bị trùng
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
