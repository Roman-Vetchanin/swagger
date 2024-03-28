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
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
        fillFaculty(student.getFaculty(), student);
        logger.warn("Before creating a Student entity, you need a Faculty entity");
        logger.info("Was invoked method for create student {}, {}", student.getId(), student.getName());
        return studentRepository.save(student);
    }

    public Student findStudent(Long studentId) {
        Student findStudent = studentRepository.findById(studentId).orElse(null);
        logger.debug("Student id {} entered", studentId);
        if (findStudent == null) {
            logger.error("Could not find student id {}", studentId);
            throw new StudentNotFoundException();
        }
        logger.info("Was invoked method for find student {}, {}", findStudent.getId(), findStudent.getName());
        return findStudent;
    }


    public Student updateStudent(Long id, Student student) {
        Student old = findStudent(id);
        fillFaculty(student.getFaculty(), old);
        old.setAge(student.getAge());
        old.setName(student.getName());
        logger.debug("Student id {} entered", id);
        logger.info("Was invoked method for update student {}, {}", old.getId(), old.getName());
        return studentRepository.save(old);
    }

    public Student removeStudent(Long studentId) {
        Student deleteStudent = findStudent(studentId);
        studentRepository.delete(deleteStudent);
        logger.debug("Student id {} entered", studentId);
        logger.info("Was invoked method for delete student {}, {}", deleteStudent.getId(), deleteStudent.getName());
        return deleteStudent;
    }

    public Collection<Student> findByAgeStudent(int studentAge) {
        Collection<Student> findByAgeStudent = studentRepository.findByAge(studentAge);
        logger.debug("Student age = {} entered", studentAge);
        logger.info("Was invoked method for find By Age Student student");
        return findByAgeStudent;
    }

    public Collection<Student> findAllStudent() {
        logger.info("Was invoked method for find All student");
        return studentRepository.findAll();
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.debug("Student minAge = {} and maxAge = {} entered", minAge, maxAge);
        logger.info("Was invoked method for find By Age Between student");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFaculty(long id) {
        Faculty facultyFind = findStudent(id).getFaculty();
        logger.debug("Student id {} entered", id);
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

    public List<Student> findStudentNameByFirstLetter(String letter) {
        return studentRepository.findAll().stream().peek(student -> student.setName(student.getName().toUpperCase()))
                .filter(student -> student.getName().startsWith(letter))
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    public Double averageAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElseThrow(StudentNotFoundException::new);
    }

    public String calculate() {
        Long start = System.currentTimeMillis();
        int sum = IntStream.iterate(1, a -> a + 1).limit(1_000_000).reduce(0, Integer::sum);
        Long stop = System.currentTimeMillis();
        long time = stop - start;
        return "Решение " + sum + ", время = " + time;
    }
    //не исключаю что я не верно понял ДЗ и нагородил какую то чушь...

    public List<Student> printParallel() {
        List<Student> students = studentRepository.findAll();
        List<Student> studentInThread = new ArrayList<>();
        System.out.println(students.get(0));
        studentInThread.add(students.get(0));
        System.out.println(students.get(1));
        studentInThread.add(students.get(1));
        new Thread(()->{
            System.out.println(students.get(2));
            studentInThread.add(students.get(2));
            System.out.println(students.get(3));
            studentInThread.add(students.get(3));
        }).start();
        new Thread(()->{
            System.out.println(students.get(4));
            studentInThread.add(students.get(4));
            System.out.println(students.get(5));
            studentInThread.add(students.get(5));
        }).start();
        return studentInThread;
    }

    final Object flag = new Object();

    private Student getNameStudents(int index) {
        List<Student> studentList = studentRepository.findAll().stream().toList();
        synchronized (flag) {
            System.out.println(studentList.get(index));
            return studentList.get(index);
       }
    }

    public List<Student> printSynchronized() {
        List<Student> studentName = new ArrayList<>();
        getNameStudents(1);
        studentName.add(getNameStudents(1));
        getNameStudents(2);
        studentName.add(getNameStudents(2));
        new Thread(()->{
            getNameStudents(3);
            studentName.add(getNameStudents(3));
            getNameStudents(4);
            studentName.add(getNameStudents(4));
        }).start();
        new Thread(()->{
            getNameStudents(5);
            studentName.add(getNameStudents(5));
            getNameStudents(6);
            studentName.add(getNameStudents(6));
        }).start();
        return studentName;
    }

}
