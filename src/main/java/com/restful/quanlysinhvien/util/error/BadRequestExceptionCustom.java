package com.restful.quanlysinhvien.util.error;

/**
 * Ngoại lệ được ném ra khi yêu cầu từ client không hợp lệ (bad request-400).
 */
public class BadRequestExceptionCustom extends RuntimeException {

    /**
     * Khởi tạo ngoại lệ với thông điệp lỗi cụ thể.
     *
     * @param message thông điệp mô tả lỗi yêu cầu không hợp lệ
     */
    public BadRequestExceptionCustom(String message) {
        super(message);
    }
}
