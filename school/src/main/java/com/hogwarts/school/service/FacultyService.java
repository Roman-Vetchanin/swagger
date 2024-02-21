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
        Faculty findFaculty = facultyRepository.findById(facultyId).orElse(null);
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

    public List<Faculty> findByColorOrName(String colorOrName) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName,colorOrName);
    }

    public List<Student> findStudents(long id) {
        Faculty faculty = findFaculty(id);
        return studentRepository.findByFaculty_Id(faculty.getId());
    }

}
