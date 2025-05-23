package com.restful.quanlysinhvien.util;

import com.restful.quanlysinhvien.domain.CustomResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Set;

/**
 * Lớp {@code FormatRestResponse} là một global response handler dùng để định
 * dạng
 * tất cả các phản hồi trả về từ controller dưới dạng {@link CustomResponse},
 * giúp đảm bảo tính nhất quán trong định dạng API.
 *
 * Lớp này tự động được áp dụng cho tất cả các controller nhờ annotation
 * {@link ControllerAdvice}.
 */
@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    /**
     * Danh sách các path sẽ được loại trừ khỏi việc tự động định dạng (ví dụ như
     * Swagger).
     */
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
     * Xác định liệu phản hồi từ controller có nên được xử lý bởi lớp này không.
     *
     * @param returnType    kiểu dữ liệu trả về từ controller
     * @param converterType converter được sử dụng để chuyển đổi phản hồi
     * @return true nếu phản hồi nên được xử lý; false nếu không
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true; // Áp dụng cho tất cả phản hồi
    }

    /**
     * Xử lý và định dạng lại phản hồi trước khi gửi về client.
     *
     * @param body                  nội dung phản hồi từ controller
     * @param returnType            kiểu dữ liệu trả về
     * @param selectedContentType   loại media được chọn (ví dụ: application/json)
     * @param selectedConverterType converter đang được sử dụng
     * @param request               request gốc
     * @param response              response gốc
     * @return đối tượng đã được định dạng hoặc giữ nguyên nếu không cần format
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {

        // lay statusCode
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        CustomResponse<Object> res = new CustomResponse<Object>();
        res.setStatusCode(status);

        // nếu data thuộc dạng String thì k thể chuyển sang json được, phải trả về data
        // luôn.
        if (body instanceof String) {
            return body;
        }

        // Bỏ qua định dạng cho các đường dẫn được loại trừ
        String path = request.getURI().getPath();
        for (String excludedPath : EXCLUDED_PATHS) {
            if (path.startsWith(excludedPath)) {
                return body;
            }
        }
        if (status >= 400) {
            return body;
        } else {
            res.setData(body);
            res.setMessage("Call api success");
        }
        return res;
    }
}
