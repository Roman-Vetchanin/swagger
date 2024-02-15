package com.hogwarts.school.service;

import com.hogwarts.school.model.Faculty;

import com.hogwarts.school.repositories.FacultyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService facultyService;

    private final List<Faculty> facultyList = new ArrayList<>(List.of(
            new Faculty(1L, "Слизерин", "Зеленый"),
            new Faculty(2L, "Грифиндор", "Красный"),
            new Faculty(3L, "Когтевран", "Синий"),
            new Faculty(4L, "Пуффендуй", "Желтый")));


    @Test
    void addFaculty() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Черный");
        when(facultyRepository.save(expected)).thenReturn(expected);
        facultyList.add(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        facultyService.addFaculty(expected);
        assertThat(facultyService.findAllFaculty()).contains(expected);
    }

    @Test
    void findFaculty() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Черный");
        when(facultyRepository.save(expected)).thenReturn(expected);
        facultyList.add(expected);
        facultyService.addFaculty(expected);
        when(facultyRepository.findById(5L)).thenReturn(Optional.of(expected));
        assertThat(facultyService.findFaculty(5L)).isEqualTo(expected);
    }

    @Test
    void updateFacultyPositiveTest() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Черный");
        Faculty actual = new Faculty(5L, "Новый факультет", "Золотой");
        when(facultyRepository.save(expected)).thenReturn(expected);
        facultyList.add(expected);
        facultyService.addFaculty(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        when(facultyRepository.save(actual)).thenReturn(actual);
        assertThat(facultyService.updateFaculty(actual)).isEqualTo(actual);
    }

    @Test
    void updateFacultyNegativeTest() {
        assertThat(facultyService.findAllFaculty()).doesNotContain(new Faculty(5L, "Рандомный факультет", "Фиолетовый"));
        assertThat(facultyService.updateFaculty(new Faculty(5L, "Рандомный факультет", "Фиолетовый"))).isEqualTo(null);
    }

    @Test
    void removeFacultyTest() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Белый");
        facultyList.add(expected);
        facultyService.addFaculty(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        facultyService.removeFaculty(expected.getId());
        facultyList.remove(expected);
        assertThat(facultyService.findAllFaculty()).doesNotContain(expected);

    }


    @Test
    void filteringFacultyByColorTest() {
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.filteringFacultyByColor("Зеленый")).contains(new Faculty(1L, "Слизерин", "Зеленый"));
    }

    @Test
    void findAllFacultyTest() {
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).hasSize(4)
                .containsExactlyInAnyOrder(
                        new Faculty(1L, "Слизерин", "Зеленый"),
                        new Faculty(2L, "Грифиндор", "Красный"),
                        new Faculty(3L, "Когтевран", "Синий"),
                        new Faculty(4L, "Пуффендуй", "Желтый"));
    }
}