package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;
import com.hogwarts.school.exception.StudentNotFoundException;

import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private StudentService studentService;


    private final List<Student> studentList = new ArrayList<>(List.of(
            new Student(1L, "Гарри", 16),
            new Student(2L, "Рон", 15),
            new Student(3L, "Гермиона", 17),
            new Student(4L, "Драко", 11),
            new Student(5L, "Крэб", 16),
            new Student(6L, "Гоил", 15),
            new Student(7L, "Тонкс", 22),
            new Student(8L,"Чжоу",16)
    ));


    @Test
    void addStudentPositiveTest() {
        Student expected = new Student(7L, "Люпин", 22);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.createStudent(expected)).isEqualTo(expected);
        assertThat(studentService.findAllStudent()).contains(expected);
    }

    @Test
    void addStudentNegativeTest() {
        Student expected = new Student(7L, "Люпин", 22);
        when(facultyRepository.findById(expected.getFaculty().getId()))
                .thenThrow(FacultyNotFoundException.class);
        assertThatThrownBy(()->studentService.createStudent(expected)).isInstanceOf(FacultyNotFoundException.class);
    }

    @Test
    void getStudentTest() {
        Student expected = new Student(7L, "Седрик", 22);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        studentService.createStudent(expected);
        when(studentRepository.findById(7L)).thenReturn(Optional.of(expected));
        assertThat(studentService.findStudent(7L)).isEqualTo(expected);
    }

    @Test
    void getStudentNegativeTest() {
        when(studentRepository.findById(7L)).thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(() -> studentService.findStudent(7L)).isInstanceOf(StudentNotFoundException.class);
    }


    @Test
    void updateStudentPositiveTest() {
        Student expected = new Student(7L, "Полумна", 14);
        Student actual = new Student(7L, "Полумна", 18);
        when(studentRepository.save(expected)).thenReturn(expected);
        assertThat(studentService.createStudent(expected)).isEqualTo(expected);
        studentList.add(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.findAllStudent()).contains(expected);
        when(studentRepository.findById(7L)).thenReturn(Optional.of(actual));
        when(studentRepository.save(actual)).thenReturn(actual);
        assertThat(studentService.updateStudent(7L, actual)).isEqualTo(actual);
    }

    @Test
    void updateStudentNegativeTest() {
        when(studentRepository.findById(7L)).thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(
                () -> studentService.updateStudent(7L, new Student(7L, "Полумна", 18)))
                .isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void removeStudentTest() {
        Student expected = new Student(7L, "Седрик", 22);
        studentList.add(expected);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentService.createStudent(expected);
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.findAllStudent()).contains(expected);
        when(studentRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        assertThat(studentService.removeStudent(expected.getId())).isEqualTo(expected);
        studentList.remove(expected);
        assertThat(studentService.findAllStudent()).doesNotContain(expected);
    }

    @Test
    void removeStudentNegativeTest() {
        when(studentRepository.findById(7L)).thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(() -> studentService.removeStudent(7L)).isInstanceOf(StudentNotFoundException.class);
    }

    @Test
    void findByAgeStudent() {
        when(studentRepository.findByAge(15)).thenReturn(studentList);
        assertThat(studentService.findByAgeStudent(15)).contains(
                new Student(6L, "Гоил", 15)
        );
    }

    @Test
    void findAllTest() {
        when(studentRepository.findAll()).thenReturn(studentList);
        assertThat(studentService.findAllStudent()).hasSize(8)
                .containsExactlyInAnyOrder(
                        new Student(1L, "Гарри", 16),
                        new Student(2L, "Рон", 15),
                        new Student(3L, "Гермиона", 17),
                        new Student(4L, "Драко", 11),
                        new Student(5L, "Крэб", 16),
                        new Student(6L, "Гоил", 15),
                        new Student(7L, "Тонкс", 22),
                        new Student(8L, "Чжоу", 16));
    }

    @Test
    void findByAgeBetween() {
        when(studentRepository.findByAgeBetween(11, 15)).thenReturn(
                List.of(new Student(4L, "Драко", 11),
                        new Student(2L, "Рон", 15),
                        new Student(6L, "Гоил", 15)));
        assertThat(studentService.findByAgeBetween(11, 15)).hasSize(3).containsExactlyInAnyOrder(
                new Student(4L, "Драко", 11),
                new Student(2L, "Рон", 15),
                new Student(6L, "Гоил", 15)
        );
    }

    @Test
    void findFaculty() {
        Student expected = new Student(7L, "Седрик", 22);
        when(studentRepository.save(expected)).thenReturn(expected);
        studentList.add(expected);
        studentService.createStudent(expected);
        when(studentRepository.findById(7L)).thenReturn(Optional.of(expected));
        assertThat(studentService.findFaculty(7L)).isEqualTo(expected.getFaculty());
    }

    @Test
    void findFacultyNegativeTest() {
        when(studentRepository.findById(7L)).thenThrow(StudentNotFoundException.class);
        assertThatThrownBy(() -> studentService.findFaculty(7L)).isInstanceOf(StudentNotFoundException.class);
    }
}