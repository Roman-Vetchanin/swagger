package com.hogwarts.school.controller;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.createFaculty(faculty));
    }

    @GetMapping("/{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.findFaculty(facultyId));
    }

    @PutMapping("/{facultyId}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long facultyId, @RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(facultyId, faculty));
    }

    @DeleteMapping("/{facultyId}")
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.removeFaculty(facultyId));
    }

    @GetMapping(params = "color")
    public ResponseEntity<List<Faculty>> getFacultyByColor(@RequestParam String color) {
        List<Faculty> fcl = facultyService.filteringFacultyByColor(color);
        return ResponseEntity.ok(fcl);
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAll() {
        return ResponseEntity.ok(facultyService.findAllFaculty());
    }

    @GetMapping(params = "colorOrName")
    public List<Faculty> findByColorOrName(@RequestParam String colorOrName) {
        return facultyService.findByColorOrName(colorOrName);
    }

    @GetMapping("/{id}/students")
    public List<Student> findStudents(@PathVariable long id) {
        return facultyService.findStudents(id);
    }

    @GetMapping("/getLongestFacultyName")
    public ResponseEntity<List<Faculty>> longestFacultyName() {
        return ResponseEntity.ok(facultyService.longestFacultyName());
    }
}
