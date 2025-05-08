package com.restful.quanlysinhvien.service_impl;

import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.util.error.ClassNameValidationException;
import com.restful.quanlysinhvien.util.error.EmailValidationException;
import com.restful.quanlysinhvien.util.error.IdValidationException;

import java.util.List;

public interface StudentImplService {
    /**
     * Retrieves all students from the system.
     *
     * @return List of StudentDTO containing all students.
     */
    List<StudentDTO> getAllStu();

    /**
     * Retrieves a student by their student code.
     *
     * @param stuCode The student code to search for.
     * @return The corresponding StudentDTO.
     * @throws IdValidationException If the student code does not exist.
     */
    StudentDTO getStuByStuCode(String stuCode) throws IdValidationException;

    /**
     * Deletes a student from the system by their student code.
     *
     * @param stuCode The student code of the student to be deleted.
     * @throws IdValidationException If the student code does not exist.
     */
    void deleteStuByStuCode(String stuCode) throws IdValidationException;

    /**
     * Updates a student's information based on the provided data and student code.
     *
     * @param studentUpdateDTO Data transfer object containing the updated student info.
     * @param stuCode The code of the student to update.
     * @throws IdValidationException If the student code does not exist.
     * @throws EmailValidationException If the email already exists.
     * @throws ClassNameValidationException If the class name does not exist.
     */
    void updateStu(StudentUpdateDTO studentUpdateDTO, String stuCode)
            throws IdValidationException, EmailValidationException, ClassNameValidationException;

    /**
     * Creates a new student based on the provided StudentDTO.
     *
     * @param studentDTO The DTO containing student details to be created.
     * @throws IdValidationException If the student code already exists.
     * @throws EmailValidationException If the email already exists.
     * @throws ClassNameValidationException If the class name does not exist.
     */
    void createStu(StudentDTO studentDTO)
            throws IdValidationException, EmailValidationException, ClassNameValidationException;

}
