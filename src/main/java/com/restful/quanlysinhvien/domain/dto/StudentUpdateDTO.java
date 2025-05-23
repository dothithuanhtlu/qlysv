package com.restful.quanlysinhvien.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema
@Builder
public class StudentUpdateDTO {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

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
    private long roleId;
}
