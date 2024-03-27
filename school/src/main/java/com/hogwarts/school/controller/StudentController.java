package com.hogwarts.school.controller;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
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
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Student> removeStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.removeStudent(studentId));
    }

    @GetMapping(params = "studentAge")
    public ResponseEntity<Collection<Student>> getStudentByAge(@RequestParam int studentAge) {
        Collection<Student> std = studentService.findByAgeStudent(studentAge);
        return ResponseEntity.ok(std);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAll() {
        return ResponseEntity.ok(studentService.findAllStudent());
    }

    @GetMapping(params = {"minAge", "maxAge"})
    public List<Student> findByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findFaculty(@PathVariable long id) {
        return studentService.findFaculty(id);
    }

    @GetMapping("/getAmountStudents")
    public ResponseEntity<Integer> getAmountStudent() {
        return ResponseEntity.ok(studentService.getAmountStudents());
    }

    @GetMapping("/getAverageAge")
    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.getAverageAge());
    }

    @GetMapping("/lastFiveRecords")
    public ResponseEntity<List<Student>> getLastThreeRecords() {
        return ResponseEntity.ok(studentService.getLastFiveRecords());
    }

    @GetMapping("/findStudentNameByFirstLetter/{latter}")
    public ResponseEntity<List<Student>> findStudentNameByFirstLetter(String latter) {
        return ResponseEntity.ok(studentService.findStudentNameByFirstLetter(latter));
    }

    @GetMapping("/getAvgAge")
    public ResponseEntity<Double> avgAge() {
        return ResponseEntity.ok(studentService.averageAge());
    }

    @GetMapping("/calc")
    public ResponseEntity<String> calculate() {
        return ResponseEntity.ok(studentService.calculate());
    }
}
