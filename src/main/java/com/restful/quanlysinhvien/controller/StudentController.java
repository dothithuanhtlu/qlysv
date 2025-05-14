package com.restful.quanlysinhvien.controller;

import com.restful.quanlysinhvien.doc.StudentApiDoc;
import com.restful.quanlysinhvien.domain.CustomResponse;
import com.restful.quanlysinhvien.domain.dto.ResultPaginationDTO;
import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.services.service_impl.StudentService;
import com.restful.quanlysinhvien.util.error.BadRequestExceptionCustom;
import com.restful.quanlysinhvien.util.error.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @Operation: mô tả tổng quan API cho Swagger UI.
 * @ApiResponses: liệt kê các mã phản hồi có thể xảy ra.
 * @ApiResponse: định nghĩa chi tiết từng loại mã phản hồi.
 * @ExampleObject: ví dụ cụ thể trả về JSON tương ứng với từng response.
 * @Schema: mô tả kiểu dữ liệu trả về hoặc nhận vào.
 */

@AllArgsConstructor
@RestController
// kích hoạt validation cho parameter
@Validated
public class StudentController {
    private final StudentService studentService;

    /**
     * API lấy danh sách sinh viên có hỗ trợ phân trang.
     * Nếu không truyền current và pageSize thì trả toàn bộ danh sách.
     *
     * @param currentOptional  chỉ số trang hiện tại (bắt đầu từ 1). Optional.
     * @param pageSizeOptional số lượng phần tử mỗi trang. Optional.
     * @return ResponseEntity chứa danh sách sinh viên (phân trang hoặc toàn bộ)
     * @throws BadRequestExceptionCustom nếu current/pageSize không hợp lệ (không
     *                                   phải số, hoặc <= 0)
     */
    @Operation(summary = StudentApiDoc.SUMMARY_GET_STUDENTS, description = StudentApiDoc.DESC_GET_STUDENTS)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công", content = {
                    @Content(mediaType = "application/json", schema = @Schema(oneOf = {
                            ResultPaginationDTO.class,
                            List.class
                    }), examples = {
                            @ExampleObject(name = "Không phân trang", value = StudentApiDoc.EXAMPLE_NO_PAGINATION),
                            @ExampleObject(name = "Có phân trang", value = StudentApiDoc.EXAMPLE_PAGINATION)
                    })
            }),
            @ApiResponse(responseCode = "400", description = "Tham số không hợp lệ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_400)))
    })
    @GetMapping(value = "/students")
    public ResponseEntity<Object> getAllStudentsPagination(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        return ResponseEntity.ok(this.studentService.getAll(currentOptional, pageSizeOptional));
    }

    /**
     * Lấy thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode Mã sinh viên (không được để trống)
     * @return ResponseEntity chứa StudentDTO tương ứng
     * @throws ResourceNotFoundException    nếu không tìm thấy sinh viên
     * @throws ConstraintViolationException nếu @PathVariable có lỗi
     */
    @Operation(summary = StudentApiDoc.GET_STUDENT_BY_CODE_SUMMARY, description = StudentApiDoc.GET_STUDENT_BY_CODE_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tìm thấy sinh viên", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class), examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_FOUND))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy sinh viên", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class), examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_NOT_FOUND)))
    })
    @GetMapping(value = "/students/{stuCode}")
    public ResponseEntity<StudentDTO> getStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode) {
        StudentDTO studentDTO = this.studentService.getStuByStuCode(stuCode);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Xóa sinh viên theo mã sinh viên.
     *
     * @param stuCode Mã sinh viên cần xóa
     * @return ResponseEntity với mã trạng thái 204 No Content nếu xóa thành công
     * @throws ConstraintViolationException nếu @PathVariable có lỗi
     */
    @Operation(summary = StudentApiDoc.DELETE_STUDENT_SUMMARY, description = StudentApiDoc.DELETE_STUDENT_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa sinh viên thành công", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_DELETE_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Xóa thất bại (Stored Procedure lỗi hoặc mã không tồn tại)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomResponse.class), examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_DELETE_FAILED)))
    })
    @DeleteMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> deleteStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode) {
        this.studentService.deleteStuByStuCode(stuCode);
        return ResponseEntity.ok().build();
    }

    /**
     * Cập nhật thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode          Mã sinh viên cần cập nhật
     * @param studentUpdateDTO Thông tin cập nhật (phải hợp lệ)
     * @return ResponseEntity với mã trạng thái 200 nếu cập nhật thành
     *         công với data là studentDTO đã update
     * @throws MethodArgumentNotValidException nếu @Valid có lỗi
     * @throws ConstraintViolationException    nếu @PathVariable có lỗi
     */
    @Operation(summary = StudentApiDoc.UPDATE_STUDENT_SUMMARY, description = StudentApiDoc.UPDATE_STUDENT_DESC, responses = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_UPDATE_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Lỗi dữ liệu gửi lên không hợp lệ hoặc stored procedure thất bại", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy mã sinh viên hoặc lớp học", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_NOT_FOUND))),
            @ApiResponse(responseCode = "409", description = "Email đã tồn tại và thuộc về sinh viên khác", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_DUPLICATE))),
            @ApiResponse(responseCode = "500", description = "Lỗi hệ thống không mong muốn", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_UNEXPECTED_ERROR)))
    })
    @PutMapping(value = "/students/{stuCode}")
    public ResponseEntity<StudentDTO> updateStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode,
            @Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        return ResponseEntity.ok(this.studentService.updateStu(studentUpdateDTO, stuCode));
    }

    /**
     * Tạo mới một sinh viên.
     *
     * @param studentDTO Thông tin sinh viên mới (phải hợp lệ)
     * @return ResponseEntity chứa StudentDTO đã tạo và mã trạng thái 201 Created
     *         procedure
     * @throws MethodArgumentNotValidException nếu @Valid có lỗi
     */
    @Operation(summary = StudentApiDoc.CREATE_STUDENT_SUMMARY, description = StudentApiDoc.CREATE_STUDENT_DESC)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo sinh viên thành công", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_CREATE_SUCCESS))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc stored procedure thất bại", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "Validation Error", value = StudentApiDoc.EXAMPLE_CREATE_VALIDATION_ERROR),
                    @ExampleObject(name = "Stored Procedure Failed", value = StudentApiDoc.EXAMPLE_CREATE_STORED_PROCEDURE_FAILED)
            })),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy lớp học", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_CREATE_CLASS_NOT_FOUND))),
            @ApiResponse(responseCode = "409", description = "Email hoặc mã sinh viên bị trùng", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = StudentApiDoc.EXAMPLE_CREATE_DUPLICATE)))
    })
    @PostMapping(value = "/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        this.studentService.createStu(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }
}
