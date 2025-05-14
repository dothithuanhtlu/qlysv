package com.restful.quanlysinhvien.services;

import com.restful.quanlysinhvien.domain.dto.ResultPaginationDTO;
import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.util.error.BadRequestExceptionCustom;
import com.restful.quanlysinhvien.util.error.DuplicateResourceException;
import com.restful.quanlysinhvien.util.error.ResourceNotFoundException;
import com.restful.quanlysinhvien.util.error.StoredProcedureFailedException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

public interface IStudentService {

        /**
         * Lấy danh sách sinh viên theo phân trang (nếu có) hoặc toàn bộ nếu không có
         * phân trang.
         *
         * @param currentOptional  Optional chứa số trang hiện tại (current)
         * @param pageSizeOptional Optional chứa kích thước mỗi trang (pageSize)
         * @return Object - có thể là danh sách sinh viên hoặc đối tượng phân trang tùy
         *         theo tham số
         * @throws BadRequestExceptionCustom nếu tham số không hợp lệ
         */
        public Object getAll(Optional<String> currentOptional, Optional<String> pageSizeOptional);

        /**
         * Lấy danh sách tất cả sinh viên trong hệ thống.
         *
         * @return Danh sách StudentDTO chứa thông tin tất cả sinh viên
         */
        public List<StudentDTO> getAllStu();

        /**
         * Lấy danh sách sinh viên có phân trang.
         *
         * @param pageable Đối tượng Pageable chứa thông tin trang hiện tại và kích
         *                 thước trang
         * @return Đối tượng ResultPaginationDTO chứa danh sách sinh viên và thông tin
         *         phân trang
         */
        public ResultPaginationDTO getAllStuPag(Pageable pageable);

        /**
         * Lấy thông tin sinh viên theo mã sinh viên.
         *
         * @param stuCode Mã sinh viên cần tìm (không được để trống)
         * @return Đối tượng StudentDTO tương ứng với sinh viên tìm được
         * @throws ResourceNotFoundException Nếu không tìm thấy sinh viên với mã đã cung
         *                                   cấp
         */
        public StudentDTO getStuByStuCode(String stuCode) throws ResourceNotFoundException;

        /**
         * Xóa sinh viên khỏi hệ thống theo mã sinh viên.
         *
         * @param stuCode Mã sinh viên cần xóa (không được để trống)
         * @throws ResourceNotFoundException Nếu không tìm thấy sinh viên với mã đã cung
         *                                   cấp
         */
        public void deleteStuByStuCode(String stuCode) throws ResourceNotFoundException;

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
         * @return đối tượng {@link StudentDTO} chứa thông tin tương ứng
         */
        public StudentDTO updateStu(StudentUpdateDTO studentUpdateDTO, String stuCode)
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
         * @return đối tượng {@link StudentDTO} chứa thông tin tương ứng
         */
        public StudentDTO createStu(StudentDTO studentDTO)
                        throws ResourceNotFoundException, StoredProcedureFailedException, DuplicateResourceException;
}
