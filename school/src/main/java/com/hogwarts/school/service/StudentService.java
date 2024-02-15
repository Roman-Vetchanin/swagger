package com.hogwarts.school.service;

import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long studentId) {
        return studentRepository.findById(studentId).get();
    }


    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public void removeStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    public List<Student> filteringStudentsByAge(int ageStudent) {
        return findAll().stream().filter(student -> student.getAge() == ageStudent).collect(Collectors.toList());
    }

    public Collection<Student> findAll() {
        return studentRepository.findAll();
    }

}
