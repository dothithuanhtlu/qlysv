package com.restful.quanlysinhvien.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // khóa setter của id khong cho phep truy cap
    @Setter(AccessLevel.NONE)
    private long id;

    @NotBlank(message = "Role name cannot be blank")
    @Column(unique = true, nullable = false) // Đảm bảo nameRole là duy nhất
    private String nameRole;

    private String description;
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY) // One-to-Many: 1 Role có nhiều Student
    @JsonIgnore // Thêm vào để tránh vòng lặp khi serialize
    private List<Student> students;
}