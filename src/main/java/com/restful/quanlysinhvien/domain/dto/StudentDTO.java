package com.restful.quanlysinhvien.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentDTO {
    @NotBlank(message = "StudentCode cannot be blank")
    private String studentCode;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Past(message = "Birth date must be in the past")
    private LocalDate dateOfBirth;

    private String address;

    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be either MALE or FEMALE")
    private String gender;

    @NotBlank(message = "Email cannot be blank")
    private String className;
}
