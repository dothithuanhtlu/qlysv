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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated
public class StudentController {
    private final StudentService studentService;

    @GetMapping(value = "/students")
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(this.studentService.getAllStu());
    }

    @GetMapping(value = "/students/{stuCode}")
    public ResponseEntity<StudentDTO> getStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode) throws IdValidationException {
        StudentDTO studentDTO = this.studentService.getStuByStuCode(stuCode);
        return ResponseEntity.ok(studentDTO);
    }

    @DeleteMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> deleteStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode) throws IdValidationException {
        this.studentService.deleteStuByStuCode(stuCode);
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/students/{stuCode}")
    public ResponseEntity<Void> updateStudentByStuCode(
            @PathVariable("stuCode") @NotBlank(message = "Student code must not be empty") String stuCode,
            @Valid @RequestBody StudentUpdateDTO studentUpdateDTO)
    throws IdValidationException, EmailValidationException, ClassNameValidationException {
        this.studentService.updateStu(studentUpdateDTO, stuCode);
        return ResponseEntity.ok(null);
    }

    @PostMapping(value = "/students")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO)
            throws IdValidationException, EmailValidationException, ClassNameValidationException
    {
        this.studentService.createStu(studentDTO);
        return ResponseEntity.ok(null);
    }
}
