package com.restful.quanlysinhvien.util.error;

import com.restful.quanlysinhvien.domain.CustomResponse;
import jakarta.validation.ConstraintViolationException;

import org.hibernate.TransactionException;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException {
    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    /**
     * Xử lý các ngoại lệ liên quan đến xác thực ID, email và tên lớp.
     *
     * @param e ngoại lệ được ném (IdValidationException, EmailValidationException,
     *          hoặc ClassNameValidationException)
     * @return {@link ResponseEntity} chứa {@link CustomResponse} với mã trạng thái
     *         404 và thông tin lỗi
     */
    @ExceptionHandler(value = { IdValidationException.class, EmailValidationException.class,
            ClassNameValidationException.class })
    public ResponseEntity<CustomResponse<Object>> handleIdException(Exception e) {
        logger.error("Validation error: {}", e.getMessage(), e); // Ghi log lỗi
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(e.getMessage());
        res.setMessage("Data invalid!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Xử lý ngoại lệ khi tham số đầu vào không hợp lệ (validation error).
     * Các đối tượng DTO được gửi từ request body (thường là @RequestBody)
     * 
     * @param ex ngoại lệ MethodArgumentNotValidException chứa thông tin lỗi xác
     *           thực
     * @return {@link ResponseEntity} chứa {@link CustomResponse} với mã trạng thái
     *         400 và danh sách lỗi
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Object>> validateError(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage(), ex);
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Xử lý ngoại lệ khi vi phạm ràng buộc xác thực (ConstraintViolationException).
     * trước khi lưu vào db
     * 
     * @param e ngoại lệ ConstraintViolationException chứa thông tin lỗi xác thực
     * @return {@link ResponseEntity} chứa {@link CustomResponse} với mã trạng thái
     *         400 và danh sách lỗi
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse<Object>> handleConstraintViolation(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage(), e);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Validation Error");

        // Lấy toàn bộ lỗi và gộp lại
        List<String> messages = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        res.setMessage(messages.size() > 1 ? messages : messages.get(0));
        return ResponseEntity.badRequest().body(res);
    }

    /**
     * Xử lý ngoại lệ khi lớp học đầy hoặc không thể tạo/cập nhật sinh viên
     * 
     * @param ex ngoại lệ RuntimeException từ stored procedure
     * @return {@link ResponseEntity} chứa {@link CustomResponse} với mã trạng thái
     *         400
     */
    @ExceptionHandler(value = { StoredProcedureFailedException.class })
    public ResponseEntity<CustomResponse<Object>> handleStoredProcedureException(RuntimeException ex) {
        logger.error("Stored procedure operation failed: {}", ex.getMessage(), ex);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Operation Failed");
        res.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    /**
     * Xử lý các lỗi liên quan đến giao dịch cơ sở dữ liệu
     * 
     * @param ex ngoại lệ liên quan đến giao dịch cơ sở dữ liệu
     * @return {@link ResponseEntity} chứa {@link CustomResponse} với mã trạng thái
     *         500
     */
    @ExceptionHandler(value = { DataAccessException.class, TransactionException.class })
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(Exception ex) {
        logger.error("Database operation failed: {}", ex.getMessage(), ex);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setError("Database Operation Failed");
        res.setMessage("An error occurred while accessing the database");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * Xử lý các ngoại lệ RuntimeException chung.
     * Bắt những ngoại lệ không được xử lý bởi các handler cụ thể khác
     *
     * @param ex      ngoại lệ RuntimeException được ném
     * @param request thông tin yêu cầu HTTP
     * @return {@link ResponseEntity} chứa thông tin lỗi định dạng theo
     *         CustomResponse
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomResponse<Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error("Unexpected runtime error: {}", ex.getMessage(), ex);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setError("Unexpected Error");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
}
