package com.restful.quanlysinhvien.util.error;

/**
 * Ngoại lệ được ném ra khi tài nguyên được yêu cầu không tồn tại.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Khởi tạo ngoại lệ với thông điệp lỗi cụ thể.
     *
     * @param message thông điệp mô tả lỗi khi không tìm thấy tài nguyên
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
