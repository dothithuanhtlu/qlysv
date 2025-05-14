package com.restful.quanlysinhvien.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Cấu hình OpenAPI (Swagger) cho hệ thống quản lý sinh viên.
 * 
 * <p>
 * Lớp này thiết lập các thông tin metadata như tiêu đề API, liên hệ hỗ trợ,
 * license,
 * các server dùng cho môi trường phát triển và production.
 * </p>
 */
@Configuration
public class OpenAPIConfig {

        /**
         * Tạo đối tượng Server đại diện cho một môi trường (dev hoặc production).
         *
         * @param url         URL của server
         * @param description Mô tả cho server
         * @return Đối tượng Server
         */
        private Server createServer(String url, String description) {
                return new Server()
                                .url(url)
                                .description(description);
        }

        /**
         * Tạo thông tin liên hệ người phát triển/hỗ trợ API.
         *
         * @return Đối tượng Contact chứa thông tin liên hệ
         */
        private Contact createContact() {
                return new Contact()
                                .name("Student Management Team")
                                .email("student-management@university.edu")
                                .url("https://university.edu/support");
        }

        /**
         * Tạo thông tin về license sử dụng cho API.
         * License (giấy phép phần mềm) trong context của API hoặc phần mềm nói chung
         * là một tập hợp các điều kiện pháp lý xác định người khác được phép làm gì với
         * phần mềm/API của bạn
         * 
         * @return Đối tượng License
         */
        private License createLicense() {
                return new License()
                                .name("Academic License")
                                .url("https://university.edu/license");
        }

        /**
         * Tạo thông tin mô tả tổng quan về API (tiêu đề, mô tả, liên hệ, license).
         *
         * @return Đối tượng Info chứa metadata cho API
         */
        private Info createApiInfo() {
                return new Info()
                                .title("Student Management API")// Thường là license riêng được dùng trong môi trường
                                                                // học thuật, nghiên cứu. Điều khoản cụ thể do đơn vị
                                                                // cấp phép quy định.
                                .version("1.0")
                                .description("""
                                                API for managing student information with features:
                                                - CRUD operations for students
                                                - Advanced search with pagination
                                                - Data validation and error handling
                                                - Secure endpoints
                                                """)
                                .contact(createContact())
                                .license(createLicense());
        }

        /**
         * Khởi tạo cấu hình OpenAPI với thông tin mô tả và danh sách server.
         *
         * @return Đối tượng OpenAPI dùng cho Swagger UI
         */
        @Bean
        public OpenAPI studentManagementAPI() {
                return new OpenAPI()
                                .info(createApiInfo())
                                .servers(List.of(
                                                createServer("http://localhost:8080", "Development Server"),
                                                createServer("https://api.university.edu", "Production Server")));
        }
}
