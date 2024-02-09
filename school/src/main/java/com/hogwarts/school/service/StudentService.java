package com.hogwarts.school.service;

import com.hogwarts.school.model.Student;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long generateId = 1L;

    public Student addStudent(Student student) {
        student.setId(generateId);
        studentMap.put(student.getId(), student);
        generateId++;
        return student;
    }

    public Student getStudent(Long studentId) {
        return studentMap.get(studentId);
    }


    public Student updateStudent(Student student) {
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student removeStudent(Long studentId) {
        return studentMap.remove(studentId);
    }

    public List<Student> filteringStudentsByAge(int ageStudent) {
        return findAll().stream().filter(student -> student.getAge()==ageStudent).collect(Collectors.toList());
    }

    public Collection<Student> findAll() {
        return Collections.unmodifiableCollection(studentMap.values());
    }

}
