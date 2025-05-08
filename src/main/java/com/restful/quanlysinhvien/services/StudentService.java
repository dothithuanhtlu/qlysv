package com.restful.quanlysinhvien.services;

import com.restful.quanlysinhvien.domain.ClassRoom;
import com.restful.quanlysinhvien.domain.Student;
import com.restful.quanlysinhvien.domain.dto.StudentDTO;
import com.restful.quanlysinhvien.domain.dto.StudentUpdateDTO;
import com.restful.quanlysinhvien.repository.ClassRoomRepository;
import com.restful.quanlysinhvien.repository.StudentRepository;
import com.restful.quanlysinhvien.service_impl.StudentImplService;
import com.restful.quanlysinhvien.util.error.ClassNameValidationException;
import com.restful.quanlysinhvien.util.error.EmailValidationException;
import com.restful.quanlysinhvien.util.error.IdValidationException;
import com.restful.quanlysinhvien.util.error.StoredProcedureFailedException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
     * Method này lấy toàn bộ dữ liệu sinh viên từ database,
     * sau đó chuyển đổi từng entity sang DTO để phục vụ cho các tầng phía trên
     * (controller, API...)
     * </p>
     *
     * @return Danh sách sinh viên dưới dạng StudentDTO
     */
    @Override
    public List<StudentDTO> getAllStu() {
        List<Student> students = this.studentRepository.findAll();
        return students.stream()
                .map(this::convertToStudentDTO)
                .toList();
    }

    /**
     * Tìm kiếm sinh viên theo mã sinh viên (student code).
     *
     * <p>
     * Method sẽ kiểm tra sự tồn tại của mã sinh viên trước,
     * sau đó truy xuất thông tin và chuyển đổi sang DTO.
     * </p>
     *
     * @param stuCode Mã sinh viên cần tìm
     * @return Thông tin sinh viên tương ứng với mã đã cung cấp
     * @throws IdValidationException nếu mã sinh viên không tồn tại
     */
    @Override
    public StudentDTO getStuByStuCode(String stuCode) throws IdValidationException {
        isExistStuForUpdate(stuCode);
        Student student = this.studentRepository.findOneByStudentCode(stuCode);
        return convertToStudentDTO(student);
    }

    /**
     * Xóa sinh viên khỏi hệ thống dựa trên mã sinh viên.
     *
     * <p>
     * Method sẽ kiểm tra xem sinh viên có tồn tại hay không,
     * sau đó thực hiện thao tác xóa khỏi database.
     * </p>
     *
     * @param stuCode Mã sinh viên cần xóa
     * @throws IdValidationException nếu mã sinh viên không tồn tại
     */
    @Override
    @Transactional
    public void deleteStuByStuCode(String stuCode) throws IdValidationException {
        isExistStuForUpdate(stuCode);
        this.studentRepository.deleteByStudentCode(stuCode);
    }

    private void isExistStuForUpdate(String stuCode) throws IdValidationException {
        if (!this.studentRepository.existsByStudentCode(stuCode)) {
            throw new IdValidationException("Student code is not exist");
        }
    }

    private void isExistStuForCreate(String stuCode) throws IdValidationException {
        if (this.studentRepository.existsByStudentCode(stuCode)) {
            throw new IdValidationException("Student code already exist");
        }
    }

    private void isExistEmail(String email) throws EmailValidationException {
        if (this.studentRepository.existsByEmail(email)) {
            throw new EmailValidationException("Email is already exist");
        }
    }

    private ClassRoom isExistClassName(String className) throws ClassNameValidationException {
        ClassRoom classRoom = this.classRoomRepository.findByClassName(className);
        if (classRoom == null) {
            throw new ClassNameValidationException("ClassName is not exist");
        }
        return classRoom;
    }

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
     * Cập nhật thông tin sinh viên đã tồn tại trong hệ thống.
     *
     * <p>
     * Method này sẽ thực hiện các bước sau:
     * - Kiểm tra email có tồn tại trong hệ thống không
     * - Kiểm tra mã sinh viên đã tồn tại để đảm bảo đang cập nhật đúng người
     * - Kiểm tra tên lớp có tồn tại trong hệ thống
     * - Gọi stored procedure trong database để thực hiện cập nhật thông tin sinh
     * viên
     * </p>
     *
     * @param studentUpdateDTO Dữ liệu sinh viên cần cập nhật
     * @param stuCode          Mã sinh viên dùng để xác định sinh viên cần cập nhật
     * @throws IdValidationException        nếu mã sinh viên không tồn tại
     * @throws EmailValidationException     nếu email đã được sử dụng bởi sinh viên
     *                                      khác
     * @throws ClassNameValidationException nếu lớp không tồn tại
     */
    @Override
    @Transactional
    public void updateStu(StudentUpdateDTO studentUpdateDTO, String stuCode)
            throws IdValidationException, EmailValidationException, ClassNameValidationException {

        // Validate inputs
        isExistStuForUpdate(stuCode);
        isExistEmail(studentUpdateDTO.getEmail());

        ClassRoom classRoom = isExistClassName(studentUpdateDTO.getClassName());

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
     * Tạo mới một sinh viên trong hệ thống.
     *
     * <p>
     * Method này thực hiện các bước:
     * - Kiểm tra xem email đã được sử dụng chưa
     * - Kiểm tra mã sinh viên đã tồn tại chưa (phải là mới)
     * - Kiểm tra tên lớp có hợp lệ hay không
     * - Gọi stored procedure trong cơ sở dữ liệu để tạo sinh viên mới
     * </p>
     *
     * @param studentDTO Thông tin sinh viên cần tạo mới
     * @throws IdValidationException        nếu mã sinh viên đã tồn tại
     * @throws EmailValidationException     nếu email đã được sử dụng
     * @throws ClassNameValidationException nếu lớp không tồn tại
     */
    @Override
    @Transactional
    public void createStu(StudentDTO studentDTO)
            throws IdValidationException, EmailValidationException, ClassNameValidationException {
        isExistStuForCreate(studentDTO.getStudentCode());
        isExistEmail(studentDTO.getEmail());
        ClassRoom classRoom = isExistClassName(studentDTO.getClassName());
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
