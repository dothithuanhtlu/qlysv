package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    ClassRoom findByClassName(String className);
}
