package com.restful.quanlysinhvien.util.error;

/**
 * Ngoại lệ được ném khi mã định danh (ID) không hợp lệ hoặc không tồn tại.
 * Sử dụng để xử lý các lỗi liên quan đến xác thực mã sinh viên trong các thao
 * tác.
 */
public class IdValidationException extends Exception {

    /**
     * Khởi tạo ngoại lệ với thông điệp lỗi cụ thể.
     *
     * @param message thông điệp mô tả lỗi liên quan đến mã định danh
     */
    public IdValidationException(String message) {
        super(message);
    }
}