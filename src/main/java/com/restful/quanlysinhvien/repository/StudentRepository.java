package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Repository interface for managing {@link Student} entities.
 * Provides CRUD operations and custom queries for student data using Spring
 * Data JPA.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

        /**
         * Retrieves a student by their unique student code.
         *
         * @param studentCode the unique code of the student
         * @return the {@link Student} entity, or null if not found
         */
        Student findOneByStudentCode(String studentCode);

        /**
         * Checks if a student exists with the given student code.
         *
         * @param studentCode the student code to check
         * @return true if a student with the specified code exists, false otherwise
         */
        boolean existsByStudentCode(String studentCode);

        /**
         * Deletes a student by their student code.
         *
         * @param studentCode the student code of the student to delete
         */
        void deleteByStudentCode(String studentCode);

        /**
         * Checks if a student exists with the given email.
         *
         * @param email the email to check
         * @return true if a student with the specified email exists, false otherwise
         */
        boolean existsByEmail(String email);

        /**
         * Updates a student's information using a stored procedure.
         *
         * @param classId     the ID of the class the student belongs to
         * @param fullName    the full name of the student
         * @param email       the email address of the student
         * @param dateOfBirth the date of birth of the student
         * @param address     the address of the student
         * @param gender      the gender of the student
         * @param stuCode     the unique student code
         * @param result      the academic result of the student
         */
        @Query(value = "CALL update_stu(:p_class_id, :p_full_name, :p_email, :p_date_of_birth, :p_address, :p_gender, :p_stu_code, :p_result)", nativeQuery = true)
        void updateStudent(
                        @Param("p_class_id") Long classId,
                        @Param("p_full_name") String fullName,
                        @Param("p_email") String email,
                        @Param("p_date_of_birth") Date dateOfBirth,
                        @Param("p_address") String address,
                        @Param("p_gender") String gender,
                        @Param("p_stu_code") String stuCode,
                        @Param("p_result") Integer result);

        /**
         * Creates a new student using a stored procedure.
         *
         * @param classId     the ID of the class the student belongs to
         * @param fullName    the full name of the student
         * @param email       the email address of the student
         * @param dateOfBirth the date of birth of the student
         * @param address     the address of the student
         * @param gender      the gender of the student
         * @param stuCode     the unique student code
         * @param result      the academic result of the student
         */
        @Query(value = "CALL create_stu(:p_class_id, :p_full_name, :p_email, :p_date_of_birth, :p_address, :p_gender, :p_stu_code, :p_result)", nativeQuery = true)
        void createStudent(
                        @Param("p_class_id") Long classId,
                        @Param("p_full_name") String fullName,
                        @Param("p_email") String email,
                        @Param("p_date_of_birth") Date dateOfBirth,
                        @Param("p_address") String address,
                        @Param("p_gender") String gender,
                        @Param("p_stu_code") String stuCode,
                        @Param("p_result") Integer result);
}
