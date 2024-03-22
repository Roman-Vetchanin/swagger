package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.exception.StudentNotFoundException;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
public class StudentService {

    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        student.setId(null);
       fillFaculty(student.getFaculty(),student);
       logger.warn("Before creating a Student entity, you need a Faculty entity");
       logger.info("Was invoked method for create student {}, {}",student.getId(),student.getName());
        return studentRepository.save(student);
    }

    public Student findStudent(Long studentId) {
        Student findStudent = studentRepository.findById(studentId).orElse(null);
        logger.debug("Student id {} entered",studentId);
        if (findStudent == null) {
            logger.error("Could not find student id {}",studentId);
            throw new StudentNotFoundException();
        }
        logger.info("Was invoked method for find student {}, {}",findStudent.getId(),findStudent.getName());
        return findStudent;
    }


    public Student updateStudent(Long id, Student student) {
        Student old = findStudent(id);
        fillFaculty(student.getFaculty(),old);
        old.setAge(student.getAge());
        old.setName(student.getName());
        logger.debug("Student id {} entered",id);
        logger.info("Was invoked method for update student {}, {}",old.getId(),old.getName());
        return studentRepository.save(old);
    }

    public Student removeStudent(Long studentId) {
        Student deleteStudent = findStudent(studentId);
        studentRepository.delete(deleteStudent);
        logger.debug("Student id {} entered",studentId);
        logger.info("Was invoked method for delete student {}, {}",deleteStudent.getId(),deleteStudent.getName());
        return deleteStudent;
    }

    public Collection<Student> findByAgeStudent(int studentAge) {
        Collection<Student> findByAgeStudent = studentRepository.findByAge(studentAge);
        logger.debug("Student age = {} entered",studentAge);
        logger.info("Was invoked method for find By Age Student student");
        return findByAgeStudent;
    }

    public Collection<Student> findAllStudent() {
        logger.info("Was invoked method for find All student");
        return studentRepository.findAll();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.debug("Student minAge = {} and maxAge = {} entered",minAge,maxAge);
        logger.info("Was invoked method for find By Age Between student");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty(long id) {
        Faculty facultyFind = findStudent(id).getFaculty();
        logger.debug("Student id {} entered",id);
        logger.info("Was invoked method for find Faculty");
        return facultyFind;
    }

    private void fillFaculty(Faculty faculty, Student student) {
        if (faculty != null && faculty.getId() == null) {
            Faculty fcl = facultyRepository.findById(faculty.getId())
                    .orElse(null);
            if (fcl == null) {
                logger.error("Could not find student");
                throw new FacultyNotFoundException();
            }
            student.setFaculty(fcl);
        }
    }

    public Integer getAmountStudents() {
        logger.info("Was invoked method for get Amount Students");
        return studentRepository.getAmountStudent();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get Average Age");
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveRecords() {
        logger.info("Was invoked method for get Last Five Records");
        return studentRepository.getLastFiveRecords();
    }
}
