package com.hogwarts.school.service;

import com.hogwarts.school.exception.StudentNotFoundException;

import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(Long studentId) {
        Student findStudent = studentRepository.findById(studentId).orElse(null);
        if (findStudent == null) {
            throw new StudentNotFoundException();
        }
        return findStudent;
    }


    public Student updateStudent(Long id, Student student) {
        Student old = findStudent(id);
        old.setAge(student.getAge());
        old.setName(student.getName());
        return studentRepository.save(old);
    }

    public Student removeStudent(Long studentId) {
        Student deleteStudent = findStudent(studentId);
        studentRepository.delete(deleteStudent);
        return deleteStudent;
    }

    public Collection<Student> findByAgeStudent(int studentAge) {
        return studentRepository.findByAge(studentAge);
    }

    public Collection<Student> findAllStudent() {
        return studentRepository.findAll();
    }

}
