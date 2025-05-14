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
import java.util.stream.Collectors;
import org.slf4j.Logger;

/**
 * Lớp xử lý ngoại lệ toàn cục cho các controller REST.
 * Mỗi phương thức trong lớp này tương ứng với một loại ngoại lệ cụ thể để trả
 * về thông báo lỗi phù hợp.
 */
@RestControllerAdvice
public class GlobalException {

    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    /**
     * Xử lý ngoại lệ khi tài nguyên không được tìm thấy.
     *
     * @param e Ngoại lệ ResourceNotFoundException
     * @return ResponseEntity chứa CustomResponse với mã trạng thái 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleResourceNotFound(ResourceNotFoundException e) {
        logger.error("Data invalid(not found): {}", e.getMessage(), e);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("Resource Not Found");
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    /**
     * Xử lý các exception kiểu BadRequestExceptionCustom khi dữ liệu yêu cầu không
     * hợp lệ.
     *
     * @param e Ngoại lệ được ném ra từ controller khi dữ liệu không hợp lệ
     * @return ResponseEntity chứa CustomResponse với mã lỗi 400 và thông báo chi
     *         tiết
     */
    @ExceptionHandler(BadRequestExceptionCustom.class)
    public ResponseEntity<CustomResponse<Object>> handleBadRequest(BadRequestExceptionCustom e) {
        logger.error("Data invalid(bad request): {}", e.getMessage(), e);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Data invalid!");
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Xử lý ngoại lệ khi tài nguyên đã tồn tại (xung đột).
     *
     * @param e Ngoại lệ DuplicateResourceException
     * @return ResponseEntity chứa CustomResponse với mã trạng thái 409
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CustomResponse<Object>> handleDuplicateResource(DuplicateResourceException e) {
        logger.error("Data invalid(already exists): {}", e.getMessage(), e);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setError("Conflict");
        res.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
    }

    /**
     * Xử lý lỗi validate dữ liệu khi sử dụng annotation @Valid trên DTO.
     *
     * @param ex MethodArgumentNotValidException chứa thông tin lỗi validate
     * @return ResponseEntity chứa thông báo lỗi dạng CustomResponse và mã trạng
     *         thái 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Object>> validateError(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage(), ex);
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Xử lý ngoại lệ khi vi phạm các ràng buộc trong entity (@NotNull, @Size, ...)
     *
     * @param e ConstraintViolationException
     * @return ResponseEntity chứa CustomResponse và mã trạng thái 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse<Object>> handleConstraintViolation(ConstraintViolationException e) {
        logger.error("Validation error: {}", e.getMessage(), e);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Validation Error");

        List<String> messages = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        res.setMessage(messages.size() > 1 ? messages : messages.get(0));
        return ResponseEntity.badRequest().body(res);
    }

    /**
     * Xử lý lỗi khi gọi stored procedure thất bại.
     *
     * @param ex RuntimeException (được ném từ stored procedure wrapper)
     * @return ResponseEntity chứa CustomResponse và mã trạng thái 400
     */
    @ExceptionHandler(StoredProcedureFailedException.class)
    public ResponseEntity<CustomResponse<Object>> handleStoredProcedureException(RuntimeException ex) {
        logger.error("Stored procedure operation failed: {}", ex.getMessage(), ex);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("Operation Failed");
        res.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(res);
    }

    /**
     * Xử lý lỗi khi xảy ra sự cố trong giao dịch hoặc truy cập cơ sở dữ liệu.
     *
     * @param ex Ngoại lệ liên quan đến database như DataAccessException,
     *           TransactionException
     * @return ResponseEntity chứa thông tin lỗi và mã trạng thái 500
     */
    @ExceptionHandler({ DataAccessException.class, TransactionException.class })
    public ResponseEntity<CustomResponse<Object>> handleDatabaseException(Exception ex) {
        logger.error("Database operation failed: {}", ex.getMessage(), ex);
        CustomResponse<Object> res = new CustomResponse<>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setError("Database Operation Failed");
        res.setMessage("An error occurred while accessing the database");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    /**
     * Xử lý các lỗi không xác định trong hệ thống (RuntimeException chưa được
     * catch).
     *
     * @param ex      Ngoại lệ Runtime
     * @param request Thông tin về HTTP request
     * @return ResponseEntity chứa thông tin lỗi và mã trạng thái 500
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
