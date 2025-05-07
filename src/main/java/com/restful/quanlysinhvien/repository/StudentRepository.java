package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findOneByStudentCode(String studentCode);
    boolean existsByStudentCode(String studentCode);
    void deleteByStudentCode(String studentCode);
    boolean existsByEmail(String email);
    @Query(value = "CALL update_stu(:p_class_id, :p_full_name, :p_email, :p_date_of_birth, :p_address, :p_gender, :p_stu_code, :p_result)",
            nativeQuery = true)
    void updateStudent(
            @Param("p_class_id") Long classId,
            @Param("p_full_name") String fullName,
            @Param("p_email") String email,
            @Param("p_date_of_birth") Date dateOfBirth,
            @Param("p_address") String address,
            @Param("p_gender") String gender,
            @Param("p_stu_code") String stuCode,
            @Param("p_result") Integer result
    );
    @Query(value = "CALL create_stu(:p_class_id, :p_full_name, :p_email, :p_date_of_birth, :p_address, :p_gender, :p_stu_code, :p_result)",
            nativeQuery = true)
    void createStudent(
            @Param("p_class_id") Long classId,
            @Param("p_full_name") String fullName,
            @Param("p_email") String email,
            @Param("p_date_of_birth") Date dateOfBirth,
            @Param("p_address") String address,
            @Param("p_gender") String gender,
            @Param("p_stu_code") String stuCode,
            @Param("p_result") Integer result
    );
}



