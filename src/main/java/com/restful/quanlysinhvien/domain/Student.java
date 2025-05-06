package com.restful.quanlysinhvien.domain;

import com.restful.quanlysinhvien.domain.domain_enum.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;s

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String studentCode;

    @NotBlank(message = "fullName mustn't be empty")
    private String fullName;

    @Column(unique = true)
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email mustn't be empty")
    private String email;

    private LocalDate dateOfBirth;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;
}
