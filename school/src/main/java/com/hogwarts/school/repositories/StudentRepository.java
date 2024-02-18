package com.hogwarts.school.repositories;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Collection<Student> findByAge(int age);
}
