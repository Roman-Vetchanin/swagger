package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId).orElseThrow(FacultyNotFoundException::new);
    }


    public Faculty updateFaculty(Long id, Faculty faculty) {
        Faculty old = findFaculty(id);
        old.setColor(faculty.getColor());
        old.setName(faculty.getName());
        return facultyRepository.save(old);
    }

    public Faculty removeFaculty(Long facultyId) {
        Faculty deleteFaculty = findFaculty(facultyId);
        facultyRepository.delete(deleteFaculty);
        return deleteFaculty;
    }

    public List<Faculty> filteringFacultyByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> findAllFaculty() {
        return facultyRepository.findAll();
    }

    public List<Faculty> findByColorOrName(String colorOrName) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName,colorOrName);
    }

    public List<Student> findStudents(long id) {
        Faculty faculty = findFaculty(id);
        return studentRepository.findByFaculty_Id(faculty.getId());
    }

}
