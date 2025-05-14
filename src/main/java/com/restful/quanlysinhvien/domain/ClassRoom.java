package com.restful.quanlysinhvien.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "class_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // khóa setter của id khong cho phep truy cap
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(unique = true, nullable = false)
    private String className;

    private String description;
    private Integer maxStudents;
    private Integer currentStudents;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Student> students;
}
