package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.repositories.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long studentId) {
        Faculty findFaculty = facultyRepository.findById(studentId).orElse(null);
        if (findFaculty == null) {
            throw new FacultyNotFoundException();
        }
        return findFaculty;
    }


    public Faculty updateFaculty(Long id, Faculty faculty) {
        Faculty old = facultyRepository.findById(id).orElse(null);
        if (old == null) {
            throw new FacultyNotFoundException();
        }
        old.setColor(faculty.getColor());
        old.setName(faculty.getName());
        return facultyRepository.save(old);
    }

    public Faculty removeFaculty(Long facultyId) {
        Faculty deleteFaculty = facultyRepository.findById(facultyId).orElse(null);
        if (deleteFaculty == null) {
            throw new FacultyNotFoundException();
        }
        facultyRepository.delete(deleteFaculty);
        return deleteFaculty;
    }

    public Collection<Faculty> filteringFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findAllFaculty() {
        return facultyRepository.findAll();
    }
}
