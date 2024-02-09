package com.hogwarts.school.controller;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/addFaculty")
    public ResponseEntity<Faculty> addFaculty(@RequestBody Faculty faculty) {
        Faculty fcl = facultyService.addFaculty(faculty);
        return ResponseEntity.ok(fcl);
    }

    @GetMapping("/getFaculty/{facultyId}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long facultyId) {
        Faculty fcl = facultyService.getFaculty(facultyId);
        if (facultyId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fcl);
    }

    @PutMapping()
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty fcl = facultyService.updateFaculty(faculty);
        if (fcl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fcl);
    }
    @DeleteMapping("/removeFaculty/{facultyId}")
    public ResponseEntity<Faculty> removeStudent(@PathVariable Long facultyId) {
        Faculty fcl = facultyService.removeFaculty(facultyId);
        if (fcl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fcl);
    }
    @GetMapping("/getFacultyByColor/{color}")
    public ResponseEntity<List<Faculty>> getFacultyByColor(@PathVariable String color) {
        List<Faculty> fcl = facultyService.filteringFacultyByColor(color);
        return ResponseEntity.ok(fcl);
    }
}
