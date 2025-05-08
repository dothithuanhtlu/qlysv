package com.restful.quanlysinhvien.util;

import com.restful.quanlysinhvien.domain.CustomResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    // Danh sách các path không áp dụng format
    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui",
            "/swagger-ui.html",
            "/swagger-ui/",
            "/swagger-ui/**",
            "/webjars/**",
            "/favicon.ico");

    /**
     * Xác định xem lớp này có hỗ trợ định dạng phản hồi cho phương thức và bộ
     * chuyển đổi được chọn hay không.
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Áp dụng cho tất cả các phản hồi, bao gồm cả ResponseEntity
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        // Không format cho String vì có thể gây lỗi khi chuyển đổi
        if (body instanceof String) {
            return body;
        }

        // Kiểm tra path loại trừ
        String path = request.getURI().getPath();
        for (String excludedPath : EXCLUDED_PATHS) {
            if (path.startsWith(excludedPath)) {
                return body;
            }
        }

        // Nếu body đã là CustomResponse hoặc ResponseEntity, không cần format lại
        if (body instanceof CustomResponse) {
            return body;
        }

        // Kiểm tra nếu body là ResponseEntity
        if (body instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) body;
            Object responseBody = responseEntity.getBody();

            // Nếu body trong ResponseEntity đã là CustomResponse, trả về nguyên vẹn
            if (responseBody instanceof CustomResponse) {
                return body;
            }

            // Format body trong ResponseEntity
            CustomResponse<Object> formattedResponse = new CustomResponse<>();
            formattedResponse.setStatusCode(responseEntity.getStatusCodeValue());
            formattedResponse.setData(responseBody);
            formattedResponse.setMessage("Call api success");

            // Tạo ResponseEntity mới với body đã format
            return ResponseEntity.status(responseEntity.getStatusCode()).body(formattedResponse);
        }

        // Lấy status code từ response
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // Format response thông thường
        CustomResponse<Object> formattedResponse = new CustomResponse<>();
        formattedResponse.setStatusCode(status);

        if (status < 400) {
            // Response thành công
            formattedResponse.setData(body);
            formattedResponse.setMessage("Call api success");
        } else {
            // Response lỗi (nếu chưa được format)
            formattedResponse.setError("Error occurred");
            formattedResponse.setMessage(body != null ? body.toString() : "Unknown error");
        }

        return formattedResponse;
    }
}