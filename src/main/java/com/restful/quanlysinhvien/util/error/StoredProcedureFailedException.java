package com.restful.quanlysinhvien.util.error;

/**
 * Ngoại lệ được ném ra khi xảy ra lỗi trong quá trình thực thi stored
 * procedure.
 */
public class StoredProcedureFailedException extends RuntimeException {

    /**
     * Khởi tạo ngoại lệ với thông điệp lỗi cụ thể.
     *
     * @param message thông điệp mô tả lỗi khi thực thi stored procedure
     */
    public StoredProcedureFailedException(String message) {
        super(message);
    }
}