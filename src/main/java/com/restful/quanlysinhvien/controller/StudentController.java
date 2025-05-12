package com.restful.quanlysinhvien.controller;

import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.services.StudentService;
import com.restful.quanlysinhvien.util.error.BadRequestExceptionCustom;
import com.restful.quanlysinhvien.util.error.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    @GetMapping(value = "/students")
    public ResponseEntity<Object> getAllStudentsPagination(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        if (sCurrent.isEmpty() && sPageSize.isEmpty()) {
            return ResponseEntity.ok(this.studentService.getAllStu());
        }
        try {
            int current = Integer.parseInt(sCurrent);
            int pageSize = Integer.parseInt(sPageSize);
            if (current <= 0 || pageSize <= 0) {
                throw new BadRequestExceptionCustom("Page and size must be positive");
            }
            Pageable pageable = PageRequest.of(current - 1, pageSize);
            return ResponseEntity.ok(this.studentService.getAllStuPag(pageable));
        } catch (NumberFormatException e) {
            throw new BadRequestExceptionCustom("Invalid current or pageSize");
        }
    }

    /**
     * Lấy thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode Mã sinh viên (không được để trống)
     * @return ResponseEntity chứa StudentDTO tương ứng
     * @throws ResourceNotFoundException    nếu không tìm thấy sinh viên
     * @throws ConstraintViolationException nếu @PathVariable có lỗi
     */
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
    @DeleteMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> deleteStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode) {
        this.studentService.deleteStuByStuCode(stuCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cập nhật thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode          Mã sinh viên cần cập nhật
     * @param studentUpdateDTO Thông tin cập nhật (phải hợp lệ)
     * @return ResponseEntity với mã trạng thái 204 No Content nếu cập nhật thành
     *         công
     * @throws MethodArgumentNotValidException nếu @Valid có lỗi
     * @throws ConstraintViolationException    nếu @PathVariable có lỗi
     */
    @PutMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> updateStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode,
            @Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        this.studentService.updateStu(studentUpdateDTO, stuCode);
        return ResponseEntity.noContent().build();
    }

    /**
     * Tạo mới một sinh viên.
     *
     * @param studentDTO Thông tin sinh viên mới (phải hợp lệ)
     * @return ResponseEntity chứa StudentDTO đã tạo và mã trạng thái 201 Created
     *         procedure
     * @throws MethodArgumentNotValidException nếu @Valid có lỗi
     */
    @PostMapping(value = "/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        this.studentService.createStu(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }
}
