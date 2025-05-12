package com.restful.quanlysinhvien.service_impl;

import com.restful.quanlysinhvien.domain.dto.ResultPaginationDTO;
import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.util.error.DuplicateResourceException;
import com.restful.quanlysinhvien.util.error.ResourceNotFoundException;
import com.restful.quanlysinhvien.util.error.StoredProcedureFailedException;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface StudentImplService {

        /**
         * Lấy danh sách tất cả sinh viên trong hệ thống.
         *
         * @return Danh sách StudentDTO chứa thông tin tất cả sinh viên
         */
        List<StudentDTO> getAllStu();

        /**
         * Lấy danh sách sinh viên có phân trang.
         *
         * @param pageable Đối tượng Pageable chứa thông tin trang hiện tại và kích
         *                 thước trang
         * @return Đối tượng ResultPaginationDTO chứa danh sách sinh viên và thông tin
         *         phân trang
         */
        ResultPaginationDTO getAllStuPag(Pageable pageable);

        /**
         * Lấy thông tin sinh viên theo mã sinh viên.
         *
         * @param stuCode Mã sinh viên cần tìm (không được để trống)
         * @return Đối tượng StudentDTO tương ứng với sinh viên tìm được
         * @throws ResourceNotFoundException Nếu không tìm thấy sinh viên với mã đã cung
         *                                   cấp
         */
        StudentDTO getStuByStuCode(String stuCode) throws ResourceNotFoundException;

        /**
         * Xóa sinh viên khỏi hệ thống theo mã sinh viên.
         *
         * @param stuCode Mã sinh viên cần xóa (không được để trống)
         * @throws ResourceNotFoundException Nếu không tìm thấy sinh viên với mã đã cung
         *                                   cấp
         */
        void deleteStuByStuCode(String stuCode) throws ResourceNotFoundException;

        /**
         * Cập nhật thông tin sinh viên.
         *
         * @param studentUpdateDTO DTO chứa thông tin sinh viên cần cập nhật
         * @param stuCode          Mã sinh viên cần cập nhật (không được để trống)
         * @throws ResourceNotFoundException      Nếu không tìm thấy sinh viên hoặc lớp
         *                                        học
         * @throws DuplicateResourceException     Nếu email đã tồn tại cho sinh viên
         *                                        khác
         * @throws StoredProcedureFailedException Nếu việc cập nhật thất bại (ví dụ lớp
         *                                        đã đầy)
         */
        void updateStu(StudentUpdateDTO studentUpdateDTO, String stuCode)
                        throws ResourceNotFoundException, StoredProcedureFailedException, DuplicateResourceException;

        /**
         * Tạo mới sinh viên trong hệ thống.
         *
         * @param studentDTO DTO chứa thông tin sinh viên cần tạo
         * @throws ResourceNotFoundException      Nếu không tìm thấy lớp học được tham
         *                                        chiếu
         * @throws DuplicateResourceException     Nếu mã sinh viên hoặc email đã tồn tại
         * @throws StoredProcedureFailedException Nếu việc tạo sinh viên thất bại (ví dụ
         *                                        lớp đã đầy)
         */
        void createStu(StudentDTO studentDTO)
                        throws ResourceNotFoundException, StoredProcedureFailedException, DuplicateResourceException;
}
