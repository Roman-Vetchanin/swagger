package com.hogwarts.school.repositories;

import com.hogwarts.school.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    List<Faculty> findByColor(String color);

    List<Faculty> findByColorIgnoreCaseOrNameIgnoreCase(String color, String name);
}
