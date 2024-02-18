package com.hogwarts.school.controller;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.findStudent(studentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id,student));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Student> removeStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.removeStudent(studentId));
    }
    @GetMapping("/{studentAge}")
    public ResponseEntity<Collection<Student>> getStudentByAge(@PathVariable int studentAge) {
        Collection<Student> std = studentService.findByAgeStudent(studentAge);
        return ResponseEntity.ok(std);
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> getAll() {
        return ResponseEntity.ok(studentService.findAllStudent());
    }
}
