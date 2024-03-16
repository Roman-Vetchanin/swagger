package com.hogwarts.school.repositories;

import com.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByFaculty_Id(long id);

    @Query(value = "SELECT COUNT(*)FROM students", nativeQuery = true)
    Integer getAmountStudent();

    @Query(value = "select AVG(age) from students", nativeQuery = true)
    Double getAverageAge();

    @Query(value = "select * from students order by id desc limit 5", nativeQuery = true)
    List<Student> getLastFiveRecords();
}
