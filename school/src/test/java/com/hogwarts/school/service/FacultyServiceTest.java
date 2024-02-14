package com.hogwarts.school.service;

import com.hogwarts.school.model.Faculty;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


class FacultyServiceTest {

    private final FacultyService facultyService = new FacultyService();

    @BeforeEach
    public void beforeEach() {
        facultyService.addFaculty(new Faculty(null, "Слизерин", "Зеленый"));
        facultyService.addFaculty(new Faculty(null, "Грифиндор", "Красный"));
        facultyService.addFaculty(new Faculty(null, "Когтевран", "Синий"));
        facultyService.addFaculty(new Faculty(null, "Пуффендуй", "Желтый"));
    }

    @AfterEach
    public void afterEach() {
        Collection<Faculty> faculties = new ArrayList<>(facultyService.findAllFaculty());
        for (Faculty faculty : faculties) {
            facultyService.removeFaculty(faculty.getId());
        }
    }

    @Test
    void addFaculty() {
        Faculty expected = new Faculty(5L,"Новый факультет", "Черный");
        facultyService.addFaculty(expected);
        assertThat(facultyService.findAllFaculty()).contains(expected);
    }

    @Test
    void getFaculty() {
        Faculty expected = new Faculty(5L,"Новый факультет", "Черный");
        facultyService.addFaculty(expected);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        assertThat(facultyService.getFaculty(5L)).isEqualTo(expected);
    }

    @Test
    void updateFacultyPositiveTest() {
        Faculty expected = new Faculty(5L,"Новый факультет", "Черный");
        Faculty actual = new Faculty(5L,"Новый факультет", "Золотой");
        facultyService.addFaculty(expected);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        assertThat(facultyService.updateFaculty(actual)).isEqualTo(actual);
    }

    @Test
    void updateFacultyNegativeTest() {
        assertThat(facultyService.findAllFaculty()).doesNotContain(new Faculty(5L, "Рандомный факультет", "Фиолетовый"));
        assertThat(facultyService.updateFaculty(new Faculty(5L, "Рандомный факультет", "Фиолетовый"))).isEqualTo(null);
    }
    @Test
    void removeFacultyTest() {
        Faculty expected = new Faculty(5L,"Новый факультет", "Белый");
        facultyService.addFaculty(expected);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        facultyService.removeFaculty(expected.getId());
        assertThat(facultyService.findAllFaculty()).doesNotContain(expected);

    }


    @Test
    void filteringFacultyByColorTest() {
        assertThat(facultyService.filteringFacultyByColor("Зеленый")).contains(new Faculty(1L, "Слизерин", "Зеленый"));
    }

    @Test
    void findAllFacultyTest() {
        assertThat(facultyService.findAllFaculty()).hasSize(4)
                .containsExactlyInAnyOrder(
                        new Faculty(1L, "Слизерин", "Зеленый"),
                        new Faculty(2L, "Грифиндор", "Красный"),
                        new Faculty(3L, "Когтевран", "Синий"),
                        new Faculty(4L, "Пуффендуй", "Желтый"));
    }
}