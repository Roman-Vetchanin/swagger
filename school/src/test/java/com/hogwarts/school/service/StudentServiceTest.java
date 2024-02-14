package com.hogwarts.school.service;

import com.hogwarts.school.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


class StudentServiceTest {
    private final StudentService studentService = new StudentService();

    @BeforeEach
    public void beforeEach() {
        studentService.addStudent(new Student(null,"Гарри", 16));
        studentService.addStudent(new Student(null,"Рон", 15));
        studentService.addStudent(new Student(null,"Гермиона", 17));
        studentService.addStudent(new Student(null,"Драко", 11));
        studentService.addStudent(new Student(null,"Крэб", 16));
        studentService.addStudent(new Student(null,"Гоил", 15));
    }

    @AfterEach
    public void afterEach() {
        Collection<Student> students = new ArrayList<>(studentService.findAll());
        for (Student student : students) {
            studentService.removeStudent(student.getId());
        }
    }

    @Test
    void addStudentPositiveTest() {
        Student expected = new Student(7L,"Полумна", 14);
        studentService.addStudent(expected);
        assertThat(studentService.findAll()).contains(expected);
    }

    @Test
    void getStudentTest() {
        Student expected = new Student(7L,"Седрик", 22);
        studentService.addStudent(expected);
        assertThat(studentService.findAll()).contains(expected);
        assertThat(studentService.getStudent(7L)).isEqualTo(expected);
    }

    @Test
    void updateStudentPositiveTest() {
        Student expected = new Student(7L,"Полумна", 14);
        Student actual = new Student(7L,"Полумна", 18);
        studentService.addStudent(expected);
        assertThat(studentService.findAll()).contains(expected);
        assertThat(studentService.updateStudent(actual)).isEqualTo(actual);

    }

    @Test
    void updateStudentNegativeTest() {
        assertThat(studentService.findAll()).doesNotContain(new Student(7L, "Хагрид", 30));
        assertThat(studentService.updateStudent(new Student(7L, "Хагрид", 30))).isEqualTo(null);
    }

    @Test
    void removeStudentTest() {
        Student expected = new Student(7L,"Седрик", 22);
        studentService.addStudent(expected);
        assertThat(studentService.findAll()).contains(expected);
        studentService.removeStudent(expected.getId());
        assertThat(studentService.findAll()).doesNotContain(expected);
    }

    @Test
    void filteringStudentsByAgeTest() {
        assertThat(studentService.filteringStudentsByAge(15)).containsExactlyInAnyOrder(
                new Student(2L, "Рон", 15),
                new Student(6L, "Гоил", 15)
        );
    }

    @Test
    void findAllTest() {
        assertThat(studentService.findAll()).hasSize(6)
                .containsExactlyInAnyOrder(
                        new Student(1L, "Гарри", 16),
                        new Student(2L, "Рон", 15),
                        new Student(3L, "Гермиона", 17),
                        new Student(4L, "Драко", 11),
                        new Student(5L, "Крэб", 16),
                        new Student(6L, "Гоил", 15));
    }
}