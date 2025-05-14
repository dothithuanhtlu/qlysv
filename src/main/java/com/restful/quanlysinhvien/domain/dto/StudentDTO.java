package com.restful.quanlysinhvien.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema
public class StudentDTO {
    @NotBlank(message = "StudentCode cannot be blank")
    private String studentCode;

    @NotBlank(message = "FullName cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "dateOfBirth cannot be blank")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be either MALE or FEMALE")
    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @NotBlank(message = "ClassName cannot be blank")
    private String className;
}
