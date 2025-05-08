package com.restful.quanlysinhvien.repository;

import com.restful.quanlysinhvien.domain.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link ClassRoom} entities.
 * Provides CRUD operations and custom queries for classroom data using Spring Data JPA.
 */
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {

    /**
     * Retrieves a classroom by its unique class name.
     *
     * @param className the name of the classroom
     * @return the {@link ClassRoom} entity, or null if not found
     */
    ClassRoom findByClassName(String className);
}
