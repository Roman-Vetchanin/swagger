package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.exception.StudentNotFoundException;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
       fillFaculty(student.getFaculty(),student);
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
        fillFaculty(old.getFaculty(),student);
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

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty(long id) {
        return findStudent(id).getFaculty();
    }

    private void fillFaculty(Faculty faculty, Student student) {
        if (faculty != null && faculty.getId() == null) {
            Faculty fcl = facultyRepository.findById(faculty.getId())
                    .orElseThrow(FacultyNotFoundException::new);
            student.setFaculty(fcl);
        }
    }
}
