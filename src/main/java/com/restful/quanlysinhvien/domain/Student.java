package com.restful.quanlysinhvien.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // khóa setter của id khong cho phep truy cap
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(unique = true, nullable = false)
    private String studentCode;

    @NotBlank(message = "fullName mustn't be empty")
    private String fullName;

    @Column(unique = true)
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email mustn't be empty")
    private String email;

    @Past(message = "Birth date must be in the past")
    @NotNull(message = "dateOfBirth cannot be blank")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @Pattern(regexp = "MALE|FEMALE", message = "Gender must be either MALE or FEMALE")
    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;
}
