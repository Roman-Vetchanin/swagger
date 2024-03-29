package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;


@Service
public class FacultyService {

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(null);
        logger.info("Was invoked method for create faculty {}, {}",faculty.getId(),faculty.getName());
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long facultyId) {
        Faculty findFaculty = facultyRepository.findById(facultyId).orElse(null);
        logger.debug("Faculty id {} entered",facultyId);
        if (findFaculty == null) {
            logger.error("Failed to update student with id {}",facultyId);
            throw new FacultyNotFoundException();
        }
        logger.info("Was invoked method for find faculty {}, {}",findFaculty.getId(),findFaculty.getName());
        return findFaculty;
    }


    public Faculty updateFaculty(Long id, Faculty faculty) {
        Faculty old = findFaculty(id);
        logger.debug("Faculty id {} entered",id);
        old.setColor(faculty.getColor());
        old.setName(faculty.getName());
        logger.info("Was invoked method for update faculty {}, {}",old.getId(),old.getName());
        return facultyRepository.save(old);
    }

    public Faculty removeFaculty(Long facultyId) {
        Faculty deleteFaculty = findFaculty(facultyId);
        logger.debug("Faculty id {} entered",facultyId);
        facultyRepository.delete(deleteFaculty);
        logger.info("Was invoked method for delete faculty {}, {}",deleteFaculty.getId(),deleteFaculty.getName());
        return deleteFaculty;
    }

    public List<Faculty> filteringFacultyByColor(String color) {
        logger.debug("Faculty color {} entered",color);
        logger.info("Was invoked method for find by color faculty");
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> findAllFaculty() {
        logger.info("Was invoked method for find all faculty");
        return facultyRepository.findAll();
    }

    public List<Faculty> findByColorOrName(String colorOrName) {
        List<Faculty> findByColorOrName = facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName,colorOrName);
        logger.debug("Faculty colorOrName {} entered",colorOrName);
        logger.info("Was invoked method for find By Color Or Name faculty");
        return findByColorOrName;
    }

    public List<Student> findStudents(long id) {
        Faculty faculty = findFaculty(id);
        logger.debug("Students id {} entered",id);
        logger.info("Was invoked method for find Students");
        return studentRepository.findByFaculty_Id(faculty.getId());
    }

    public Faculty longestFacultyName() {
        return facultyRepository.findAll().stream()
                .max(Comparator.comparing(faculty -> faculty.getName().length()))
                .orElseThrow(FacultyNotFoundException::new);
    }

}
