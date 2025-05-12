package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.Student;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Interface Repository cho việc quản lý các entity {@link Student}.
 * Cung cấp các thao tác CRUD và các truy vấn tuỳ chỉnh cho dữ liệu sinh viên sử
 * dụng Spring Data JPA.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

        /**
         * Tìm sinh viên theo mã sinh viên duy nhất.
         *
         * @param studentCode mã sinh viên cần tìm
         * @return đối tượng {@link Student}, hoặc null nếu không tìm thấy
         */
        Student findOneByStudentCode(String studentCode);

        /**
         * Kiểm tra sự tồn tại của sinh viên theo mã sinh viên.
         *
         * @param studentCode mã sinh viên cần kiểm tra
         * @return true nếu tồn tại sinh viên với mã tương ứng, false nếu không
         */
        boolean existsByStudentCode(String studentCode);

        /**
         * Xoá sinh viên khỏi hệ thống dựa trên mã sinh viên.
         *
         * @param studentCode mã sinh viên cần xoá
         */
        void deleteByStudentCode(String studentCode);

        /**
         * Kiểm tra sự tồn tại của sinh viên theo email.
         *
         * @param email địa chỉ email cần kiểm tra
         * @return true nếu tồn tại sinh viên với email tương ứng, false nếu không
         */
        boolean existsByEmail(String email);

        /**
         * Tìm sinh viên theo email.
         *
         * @param email địa chỉ email cần tìm (không được null hoặc rỗng)
         * @return đối tượng {@link Student} nếu tìm thấy, hoặc null nếu không có sinh
         *         viên nào với email đã cho
         * @throws DataAccessException nếu có lỗi truy xuất cơ sở dữ liệu
         */
        Student findOneByEmail(String email);

        /**
         * Cập nhật thông tin sinh viên thông qua stored procedure.
         *
         * @param classId     ID lớp học mà sinh viên thuộc về
         * @param fullName    họ tên sinh viên
         * @param email       email sinh viên
         * @param dateOfBirth ngày sinh của sinh viên
         * @param address     địa chỉ của sinh viên
         * @param gender      giới tính của sinh viên
         * @param stuCode     mã sinh viên
         * @param result      kết quả học tập của sinh viên
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
         * Tạo mới sinh viên bằng stored procedure.
         *
         * @param classId     ID lớp học mà sinh viên thuộc về
         * @param fullName    họ tên sinh viên
         * @param email       email sinh viên
         * @param dateOfBirth ngày sinh của sinh viên
         * @param address     địa chỉ của sinh viên
         * @param gender      giới tính của sinh viên
         * @param stuCode     mã sinh viên
         * @param result      kết quả học tập của sinh viên
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

        /**
         * Gọi stored procedure để xóa sinh viên theo mã sinh viên.
         *
         * @param stuCode mã sinh viên cần xóa
         */
        @Query(value = "CALL delete_stu(:stuCode, 1)", nativeQuery = true)
        void deleteByStudentCodeProcedure(@Param("stuCode") String stuCode);
}
