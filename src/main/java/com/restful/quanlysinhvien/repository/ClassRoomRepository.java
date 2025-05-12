package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface Repository dùng để quản lý các entity {@link ClassRoom}.
 * Cung cấp các thao tác CRUD và truy vấn tùy chỉnh cho dữ liệu lớp học sử dụng
 * Spring Data JPA.
 */
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    /**
     * Tìm lớp học dựa trên tên lớp duy nhất.
     *
     * @param className tên lớp học cần tìm
     * @return đối tượng {@link ClassRoom}, hoặc null nếu không tìm thấy
     */
    ClassRoom findByClassName(String className);
}
