package com.hogwarts.school.service;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.repositories.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty crateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long studentId) {
        return facultyRepository.findById(studentId).get();
    }


    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void removeFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }

    public List<Faculty> filteringFacultyByColor(String color) {
        return findAllFaculty().stream().filter(faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
    }

    public Collection<Faculty> findAllFaculty() {
        return facultyRepository.findAll();
    }
}
