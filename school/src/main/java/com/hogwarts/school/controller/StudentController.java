package com.hogwarts.school.controller;

import com.hogwarts.school.model.Student;
import com.hogwarts.school.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student std = studentService.addStudent(student);
        return ResponseEntity.ok(std);
    }

    @GetMapping("/getStudent/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable Long studentId) {
        Student std = studentService.getStudent(studentId);
        if (studentId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(std);
    }

    @PutMapping()
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        Student std = studentService.updateStudent(student);
        if (std == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(std);
    }

    @DeleteMapping("/removeStudent/{studentId}")
    public ResponseEntity<Void> removeStudent(@PathVariable Long studentId) {
        studentService.removeStudent(studentId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("getStudentByAge/{studentId}")
    public ResponseEntity<List<Student>> getStudentByAge(@PathVariable int studentAge) {
        List<Student> std = studentService.filteringStudentsByAge(studentAge);
        return ResponseEntity.ok(std);
    }
}
