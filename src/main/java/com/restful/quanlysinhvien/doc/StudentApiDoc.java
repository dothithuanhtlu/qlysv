package com.restful.quanlysinhvien.doc;

public class StudentApiDoc {
  // Các constants cho mã response code
  public static final String STATUS_CODE_OK = "200";
  public static final String STATUS_CODE_NOT_FOUND = "404";
  public static final String STATUS_CODE_BAD_REQUEST = "400";
  public static final String STATUS_CODE_CONFLICT = "409";
  public static final String STATUS_CODE_CREATED = "201";
  public static final String STATUS_CODE_INTERNAL_SERVER_ERROR = "500";
  public static final String SUMMARY_GET_STUDENTS = "Lấy danh sách sinh viên";
  public static final String DESC_GET_STUDENTS = "API hỗ trợ phân trang. Nếu không truyền tham số, trả về toàn bộ danh sách.";

  public static final String EXAMPLE_NO_PAGINATION = """
      [
        {
          "id": 1,
          "studentCode": "SV001",
          "fullName": "Nguyễn Văn A"
        }
      ]
      """;

  public static final String EXAMPLE_PAGINATION = """
      {
        "meta": {
          "page": 0,
          "pageSize": 10,
          "pages": 5,
          "total": 50
        },
        "result": [
          {
            "id": 1,
            "studentCode": "SV001",
            "fullName": "Nguyễn Văn A"
          }
        ]
      }
      """;

  public static final String EXAMPLE_400 = """
      {
        "statusCode": 400,
        "error": "Bad Request",
        "message": "Invalid current or pageSize"
      }
      """;

  public static final String GET_STUDENT_BY_CODE_SUMMARY = "Lấy sinh viên theo mã";
  public static final String GET_STUDENT_BY_CODE_DESC = """
      Trả về thông tin sinh viên theo mã được cung cấp.
      Nếu mã không tồn tại trong hệ thống, trả về lỗi 404.
      """;

  public static final String EXAMPLE_FOUND = """
      {
        "id": 1,
        "studentCode": "SV001",
        "fullName": "Nguyễn Văn A",
        "email": "nva@example.com"
      }
      """;

  public static final String EXAMPLE_NOT_FOUND = """
      {
        "statusCode": 404,
        "error": "Resource Not Found",
        "message": "Student code not found"
      }
      """;

  public static final String DELETE_STUDENT_SUMMARY = "Xóa sinh viên theo mã";
  public static final String DELETE_STUDENT_DESC = """
      Xóa sinh viên khỏi hệ thống dựa trên mã sinh viên.
      Nếu mã không tồn tại hoặc stored procedure thất bại, trả về lỗi 400.
      """;

  public static final String EXAMPLE_DELETE_SUCCESS = """
      {
        "statusCode": 200
      }
      """;

  public static final String EXAMPLE_DELETE_FAILED = """
      {
        "statusCode": 400,
        "error": "Operation Failed",
        "message": "Failed to delete student"
      }
      """;

  public static final String UPDATE_STUDENT_SUMMARY = "Cập nhật sinh viên theo mã";
  public static final String UPDATE_STUDENT_DESC = """
      API này dùng để cập nhật thông tin sinh viên dựa theo mã sinh viên.
      - Sử dụng stored procedure `update_stu`.
      - Nếu lớp không tồn tại hoặc sinh viên không tồn tại: trả về lỗi 404.
      - Nếu email bị trùng với sinh viên khác: trả về lỗi 409.
      - Nếu lớp đã đầy: trả về lỗi 400.
      """;

  public static final String EXAMPLE_UPDATE_SUCCESS = """
      {
        "statusCode": 200,
        "error": null,
        "message": "Cập nhật thành công",
        "data": {
          "studentCode": "STU001",
          "fullName": "Nguyễn Văn A",
          "email": "nguyenvana@example.com",
          "dateOfBirth": "2001-10-10",
          "gender": "MALE",
          "address": "Hà Nội",
          "className": "CS101"
        }
      }
      """;

  public static final String EXAMPLE_VALIDATION_ERROR = """
      {
        "statusCode": 400,
        "error": "Validation Error",
        "message": [
          "fullName không được để trống",
          "Email không được để trống"
        ]
      }
      """;

  public static final String EXAMPLE_DUPLICATE = """
      {
        "statusCode": 409,
        "error": "Conflict",
        "message": "Email belongs to another student"
      }
      """;

  public static final String EXAMPLE_UNEXPECTED_ERROR = """
      {
        "statusCode": 500,
        "error": "Unexpected Error",
        "message": "NullPointerException: Cannot invoke ..."
      }
      """;
  public static final String CREATE_STUDENT_SUMMARY = "Tạo sinh viên mới";
  public static final String CREATE_STUDENT_DESC = """
      API dùng để thêm mới một sinh viên vào hệ thống.
      Sử dụng stored procedure `create_stu`. Nếu lớp đã đầy, sẽ trả về lỗi.
      """;

  public static final String EXAMPLE_CREATE_SUCCESS = """
      {
        "studentCode": "SV123",
        "fullName": "Trần Thị B",
        "email": "tranb@example.com",
        "dateOfBirth": "2002-05-10",
        "address": "Hà Nội",
        "gender": "FEMALE",
        "className": "CS202"
      }
      """;

  public static final String EXAMPLE_CREATE_VALIDATION_ERROR = """
      {
        "statusCode": 400,
        "error": "Validation Error",
        "message": [
          "Email không được để trống",
          "Date of birth phải ở quá khứ"
        ]
      }
      """;

  public static final String EXAMPLE_CREATE_DUPLICATE = """
      {
        "statusCode": 409,
        "error": "Conflict",
        "message": "Email already exists: tranb@example.com"
      }
      """;

  public static final String EXAMPLE_CREATE_CLASS_NOT_FOUND = """
      {
        "statusCode": 404,
        "error": "Resource Not Found",
        "message": "ClassName not found"
      }
      """;

  public static final String EXAMPLE_CREATE_STORED_PROCEDURE_FAILED = """
      {
        "statusCode": 400,
        "error": "Operation Failed",
        "message": "Failed to create student - possibly class is full"
      }
      """;

}
