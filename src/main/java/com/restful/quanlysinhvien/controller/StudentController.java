package com.restful.quanlysinhvien.controller;

import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.services.StudentService;
import com.restful.quanlysinhvien.util.error.ClassNameValidationException;
import com.restful.quanlysinhvien.util.error.EmailValidationException;
import com.restful.quanlysinhvien.util.error.IdValidationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
public class StudentController {
    private final StudentService studentService;

    /**
     * Lấy danh sách tất cả sinh viên.
     *
     * @return {@link ResponseEntity} chứa danh sách các đối tượng
     *         {@link StudentDTO}
     */
    @GetMapping(value = "/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(this.studentService.getAllStu());
    }

    /**
     * Lấy thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode mã sinh viên duy nhất
     * @return {@link ResponseEntity} chứa đối tượng {@link StudentDTO}
     * @throws IdValidationException nếu mã sinh viên không hợp lệ hoặc không tìm
     *                               thấy
     */
    @GetMapping(value = "/students/{stuCode}")
    public ResponseEntity<StudentDTO> getStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode)
            throws IdValidationException {
        StudentDTO studentDTO = this.studentService.getStuByStuCode(stuCode);
        return ResponseEntity.ok(studentDTO);
    }

    /**
     * Xóa sinh viên theo mã sinh viên.
     *
     * @param stuCode mã sinh viên duy nhất
     * @return {@link ResponseEntity} rỗng với mã trạng thái HTTP 200
     * @throws IdValidationException nếu mã sinh viên không hợp lệ hoặc không tìm
     *                               thấy
     */
    @DeleteMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> deleteStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode)
            throws IdValidationException {
        this.studentService.deleteStuByStuCode(stuCode);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
    }

    /**
     * Cập nhật thông tin sinh viên theo mã sinh viên.
     *
     * @param stuCode          mã sinh viên duy nhất
     * @param studentUpdateDTO DTO chứa thông tin cập nhật của sinh viên
     * @return {@link ResponseEntity} rỗng với mã trạng thái HTTP 200
     * @throws IdValidationException        nếu mã sinh viên không hợp lệ hoặc không
     *                                      tìm thấy
     * @throws EmailValidationException     nếu email không hợp lệ hoặc đã tồn tại
     * @throws ClassNameValidationException nếu tên lớp không hợp lệ
     */
    @PutMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> updateStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode,
            @Valid @RequestBody StudentUpdateDTO studentUpdateDTO)
            throws IdValidationException, EmailValidationException, ClassNameValidationException {
        this.studentService.updateStu(studentUpdateDTO, stuCode);
        // return ResponseEntity.noContent().build();
        return ResponseEntity.ok().build();
    }

    /**
     * Tạo mới một sinh viên.
     * ném ra ConstraintViolationException
     * 
     * @param studentDTO DTO chứa thông tin sinh viên mới
     * @return {@link ResponseEntity} chứa đối tượng {@link StudentDTO} đã tạo
     * @throws IdValidationException        nếu mã sinh viên không hợp lệ hoặc đã
     *                                      tồn tại
     * @throws EmailValidationException     nếu email không hợp lệ hoặc đã tồn tại
     * @throws ClassNameValidationException nếu tên lớp không hợp lệ
     */
    @PostMapping(value = "/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO)
            throws IdValidationException, EmailValidationException, ClassNameValidationException {
        this.studentService.createStu(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }
}
