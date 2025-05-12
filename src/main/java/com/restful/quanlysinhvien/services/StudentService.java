package com.restful.quanlysinhvien.services;

import com.restful.quanlysinhvien.domain.ClassRoom;
import com.restful.quanlysinhvien.domain.Student;
import com.restful.quanlysinhvien.domain.dto.Meta;
import com.restful.quanlysinhvien.domain.dto.ResultPaginationDTO;
import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.repository.ClassRoomRepository;
import com.restful.quanlysinhvien.repository.StudentRepository;
import com.restful.quanlysinhvien.service_impl.StudentImplService;
import com.restful.quanlysinhvien.util.error.DuplicateResourceException;
import com.restful.quanlysinhvien.util.error.ResourceNotFoundException;
import com.restful.quanlysinhvien.util.error.StoredProcedureFailedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService implements StudentImplService {
    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private EntityManager entityManager;

    /**
     * Lấy danh sách tất cả sinh viên trong hệ thống.
     *
     * <p>
     * Phương thức này truy vấn toàn bộ sinh viên từ cơ sở dữ liệu,
     * sau đó chuyển từng thực thể {@link Student} sang dạng {@link StudentDTO}
     * để phục vụ cho tầng controller hoặc các lớp phía trên.
     * </p>
     *
     * @return Danh sách sinh viên dưới dạng {@link StudentDTO}
     */
    @Override
    public List<StudentDTO> getAllStu() {
        List<Student> students = this.studentRepository.findAll();
        return students.stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    /**
     * Lấy danh sách sinh viên có phân trang.
     *
     * <p>
     * Phương thức này sử dụng đối tượng {@link Pageable} để truy vấn dữ liệu sinh
     * viên theo trang,
     * sau đó chuyển đổi từng entity {@link Student} sang {@link StudentDTO},
     * đồng thời trả về thông tin phân trang dưới dạng {@link ResultPaginationDTO}.
     * </p>
     *
     * @param pageable đối tượng phân trang chứa thông tin số trang và kích thước
     *                 trang
     * @return đối tượng {@link ResultPaginationDTO} chứa danh sách sinh viên và
     *         metadata phân trang
     */
    @Override
    public ResultPaginationDTO getAllStuPag(Pageable pageable) {
        Page<Student> pageStu = this.studentRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageStu.getNumber());
        mt.setPageSize(pageStu.getSize());
        mt.setPages(pageStu.getTotalPages());
        mt.setTotal(pageStu.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageStu.getContent().stream().map(this::convertToStudentDTO).toList());
        return rs;
    }

    /**
     * Tìm kiếm sinh viên theo mã sinh viên.
     * 
     * @param stuCode Mã sinh viên cần tìm
     * @return Thông tin sinh viên dưới dạng StudentDTO
     * @throws ResourceNotFoundException nếu không tìm thấy sinh viên với mã được
     *                                   cung cấp
     */
    @Override
    public StudentDTO getStuByStuCode(String stuCode) {
        validateStudentNotExist(stuCode);
        Student student = this.studentRepository.findOneByStudentCode(stuCode);
        return convertToStudentDTO(student);
    }

    /**
     * Xóa sinh viên khỏi hệ thống.
     * 
     * @param stuCode Mã sinh viên cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy sinh viên
     */
    @Override
    @Transactional
    public void deleteStuByStuCode(String stuCode) {
        validateStudentNotExist(stuCode);
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("delete_stu")
                .registerStoredProcedureParameter("p_stu_code", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_result", Integer.class, ParameterMode.OUT)
                .setParameter("p_stu_code", stuCode);

        query.execute();

        Integer result = (Integer) query.getOutputParameterValue("p_result");

        if (result == 0) {
            throw new StoredProcedureFailedException("Failed to delete student");
        }
    }

    // Khi kiểm tra studentCode không tồn tại (GET/PUT/DELETE)
    private void validateStudentNotExist(String stuCode) {
        if (!studentRepository.existsByStudentCode(stuCode)) {
            throw new ResourceNotFoundException("Student code not found"); // 404
        }
    }

    // Khi kiểm tra studentCode trùng (POST)
    private void validateStudentNotDuplicate(String stuCode) {
        if (studentRepository.existsByStudentCode(stuCode)) {
            throw new DuplicateResourceException("Student code already exists"); // 409
        }
    }

    /**
     * Kiểm tra email khi tạo mới sinh viên.
     * 
     * @param email Email cần kiểm tra
     * @throws DuplicateResourceException nếu email đã tồn tại trong hệ thống
     */
    private void validateEmailForCreate(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }
    }

    /**
     * Kiểm tra email khi cập nhật sinh viên.
     * 
     * @param email              Email cần kiểm tra
     * @param currentStudentCode Mã sinh viên hiện tại
     * @throws DuplicateResourceException nếu email thuộc về sinh viên khác
     */
    private void validateEmailForUpdate(String email, String currentStudentCode) {
        Student studentWithEmail = studentRepository.findOneByEmail(email);
        if (studentWithEmail != null && !studentWithEmail.getStudentCode().equals(currentStudentCode)) {
            throw new DuplicateResourceException("Email belongs to another student");
        }
    }

    /**
     * Kiểm tra tên lớp có tồn tại trong hệ thống hay không.
     *
     * <p>
     * Nếu tên lớp không tồn tại trong cơ sở dữ liệu, phương thức sẽ ném ra ngoại lệ
     * {@link ResourceNotFoundException}.
     * </p>
     *
     * @param className tên lớp cần kiểm tra
     * @return đối tượng {@link ClassRoom} tương ứng nếu tồn tại
     * @throws ResourceNotFoundException nếu không tìm thấy lớp có tên đã cho
     */
    private ClassRoom validateClassNameNotExist(String className) {
        ClassRoom classRoom = this.classRoomRepository.findByClassName(className);
        if (classRoom == null) {
            throw new ResourceNotFoundException("ClassName not found");
        }
        return classRoom;
    }

    /**
     * Chuyển đổi thực thể {@link Student} sang đối tượng {@link StudentDTO}.
     *
     * <p>
     * Phương thức này dùng để tách lớp logic giữa tầng entity và tầng DTO,
     * giúp bảo vệ dữ liệu và chuẩn hóa thông tin trả về cho client.
     * </p>
     *
     * @param student đối tượng {@link Student} cần chuyển đổi
     * @return đối tượng {@link StudentDTO} chứa thông tin tương ứng
     */
    private StudentDTO convertToStudentDTO(Student student) {
        return StudentDTO.builder()
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth())
                .address(student.getAddress())
                .gender(student.getGender())
                .className(student.getClassRoom().getClassName())
                .build();
    }

    /**
     * Cập nhật thông tin sinh viên.
     * 
     * @param studentUpdateDTO DTO chứa thông tin cập nhật
     * @param stuCode          Mã sinh viên cần cập nhật
     * @throws ResourceNotFoundException      nếu không tìm thấy sinh viên hoặc lớp
     *                                        học
     * @throws DuplicateResourceException     nếu email mới đã thuộc về sinh viên
     *                                        khác
     * @throws StoredProcedureFailedException nếu quá trình cập nhật thất bại
     */
    @Override
    @Transactional
    public void updateStu(StudentUpdateDTO studentUpdateDTO, String stuCode) {

        // Validate inputs
        validateStudentNotExist(stuCode);
        validateEmailForUpdate(studentUpdateDTO.getEmail(), stuCode);

        ClassRoom classRoom = validateClassNameNotExist(studentUpdateDTO.getClassName());

        // Using EntityManager for more control
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("update_stu")
                .registerStoredProcedureParameter("p_class_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_full_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_date_of_birth", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_gender", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_stu_code", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", Integer.class, ParameterMode.OUT)

                .setParameter("p_class_id", classRoom.getId())
                .setParameter("p_full_name", studentUpdateDTO.getFullName())
                .setParameter("p_email", studentUpdateDTO.getEmail())
                .setParameter("p_date_of_birth", java.sql.Date.valueOf(studentUpdateDTO.getDateOfBirth()))
                .setParameter("p_address", studentUpdateDTO.getAddress())
                .setParameter("p_gender", studentUpdateDTO.getGender())
                .setParameter("p_stu_code", stuCode);

        query.execute();

        Integer result = (Integer) query.getOutputParameterValue("p_Result");

        if (result == 0) {
            throw new StoredProcedureFailedException("Failed to update student - possibly class is full");
        }
    }

    /**
     * Tạo mới sinh viên.
     * 
     * @param studentDTO DTO chứa thông tin sinh viên mới
     * @throws ResourceNotFoundException      nếu không tìm thấy lớp học
     * @throws DuplicateResourceException     nếu mã sinh viên hoặc email đã tồn tại
     * @throws StoredProcedureFailedException nếu quá trình tạo mới thất bại
     */
    @Override
    @Transactional
    public void createStu(StudentDTO studentDTO)
            throws DuplicateResourceException, ResourceNotFoundException {
        validateStudentNotDuplicate(studentDTO.getStudentCode());
        validateEmailForCreate(studentDTO.getEmail());
        ClassRoom classRoom = validateClassNameNotExist(studentDTO.getClassName());
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("create_stu")
                .registerStoredProcedureParameter("p_class_id", Long.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_full_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_date_of_birth", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_address", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_gender", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_stu_code", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_Result", Integer.class, ParameterMode.OUT)

                .setParameter("p_class_id", classRoom.getId())
                .setParameter("p_full_name", studentDTO.getFullName())
                .setParameter("p_email", studentDTO.getEmail())
                .setParameter("p_date_of_birth", java.sql.Date.valueOf(studentDTO.getDateOfBirth()))
                .setParameter("p_address", studentDTO.getAddress())
                .setParameter("p_gender", studentDTO.getGender())
                .setParameter("p_stu_code", studentDTO.getStudentCode());

        query.execute();

        Integer result = (Integer) query.getOutputParameterValue("p_Result");

        if (result == 0) {
            throw new StoredProcedureFailedException("Failed to create student - possibly class is full");
        }
    }

}
