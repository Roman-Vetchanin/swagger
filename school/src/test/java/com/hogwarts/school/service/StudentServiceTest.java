package com.hogwarts.school.service;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;


    private final List<Student> studentList = new ArrayList<>(List.of(
            new Student(1L, "Гарри", 16),
            new Student(2L, "Рон", 15),
            new Student(3L, "Гермиона", 17),
            new Student(4L, "Драко", 11),
            new Student(5L, "Крэб", 16),
            new Student(6L, "Гоил", 15)
    ));


    @Test
    void addStudentPositiveTest() {
        Student expected = new Student(7L, "Полумна", 14);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        studentService.addStudent(expected);
        assertThat(studentService.findAll()).contains(expected);
    }

    @Test
    void getStudentTest() {
        Student expected = new Student(7L, "Седрик", 22);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        studentService.addStudent(expected);
        when(studentRepository.findById(5L)).thenReturn(Optional.of(expected));
        assertThat(studentService.findStudent(5L)).isEqualTo(expected);
    }


    @Test
    void updateStudentPositiveTest() {
        Student expected = new Student(7L, "Полумна", 14);
        Student actual = new Student(7L, "Полумна", 18);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        studentService.addStudent(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.findAll()).contains(expected);
        when(studentRepository.save(actual)).thenReturn(actual);
        assertThat(studentService.updateStudent(actual)).isEqualTo(actual);

    }

    @Test
    void updateStudentNegativeTest() {
        assertThat(studentService.findAll()).doesNotContain(new Student(7L, "Хагрид", 30));
        assertThat(studentService.updateStudent(new Student(7L, "Хагрид", 30))).isEqualTo(null);
    }

    @Test
    void removeStudentTest() {
        Student expected = new Student(7L, "Седрик", 22);
        studentList.add(expected);
        studentService.addStudent(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.findAll()).contains(expected);
        studentService.removeStudent(expected.getId());
        studentList.remove(expected);
        assertThat(studentService.findAll()).doesNotContain(expected);
    }

    @Test
    void filteringStudentsByAgeTest() {
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.filteringStudentsByAge(15)).containsExactlyInAnyOrder(
                new Student(2L, "Рон", 15),
                new Student(6L, "Гоил", 15)
        );
    }

    @Test
    void findAllTest() {
        when(studentRepository.findAll()).thenReturn(studentList);
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