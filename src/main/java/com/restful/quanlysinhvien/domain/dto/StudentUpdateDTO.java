package com.restful.quanlysinhvien.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO {
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Past(message = "Birth date must be in the past")
    private LocalDate dateOfBirth;

    private String address;
    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be either MALE or FEMALE")
    private String gender;
    @NotBlank(message = "ClassName cannot be blank")
    private String className;
}
