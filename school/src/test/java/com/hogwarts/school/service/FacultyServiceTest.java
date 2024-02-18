package com.hogwarts.school.service;

import com.hogwarts.school.exception.FacultyNotFoundException;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        when(facultyRepository.save(expected)).thenReturn(expected);
        assertThat(facultyService.createFaculty(expected)).isEqualTo(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).contains(expected);
    }

    @Test
    void findFacultyPositiveTest() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Черный");
        when(facultyRepository.save(expected)).thenReturn(expected);
        facultyList.add(expected);
        when(facultyRepository.save(expected)).thenReturn(expected);
        assertThat(facultyService.createFaculty(expected)).isEqualTo(expected);
        when(facultyRepository.findById(5L)).thenReturn(Optional.of(expected));
        assertThat(facultyService.findFaculty(5L)).isEqualTo(expected);
    }

    @Test
    void findFacultyNegativeTest() {
        when(facultyRepository.findById(7L)).thenThrow(FacultyNotFoundException.class);
        assertThatThrownBy(() -> facultyService.findFaculty(7L)).isInstanceOf(FacultyNotFoundException.class);

    }

    @Test
    void updateFacultyPositiveTest() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Черный");
        Faculty actual = new Faculty(5L, "Новый факультет", "Золотой");
        when(facultyRepository.save(expected)).thenReturn(expected);
        assertThat(facultyService.createFaculty(expected)).isEqualTo(expected);
        facultyList.add(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        when(facultyRepository.findById(7L)).thenReturn(Optional.of(actual));
        when(facultyRepository.save(actual)).thenReturn(actual);
        assertThat(facultyService.updateFaculty(7L, actual)).isEqualTo(actual);
    }

    @Test
    void updateFacultyNegativeTest() {
        when(facultyRepository.findById(7L)).thenThrow(FacultyNotFoundException.class);
        assertThatThrownBy(() -> facultyService.updateFaculty(7L, new Faculty(7L, "Рандомный факультет", "Белый"))).isInstanceOf(FacultyNotFoundException.class);
    }

    @Test
    void removeFacultyTest() {
        Faculty expected = new Faculty(5L, "Новый факультет", "Белый");
        facultyList.add(expected);
        when(facultyRepository.save(expected)).thenReturn(expected);
        facultyService.createFaculty(expected);
        when(facultyRepository.findAll()).thenReturn(facultyList);
        assertThat(facultyService.findAllFaculty()).contains(expected);
        when(facultyRepository.findById(5L)).thenReturn(Optional.of(expected));
        assertThat(facultyService.removeFaculty(expected.getId())).isEqualTo(expected);
        facultyList.remove(expected);
        assertThat(facultyService.findAllFaculty()).doesNotContain(expected);

    }

    @Test
    void removeNegativeTest() {
        when(facultyRepository.findById(7L)).thenThrow(FacultyNotFoundException.class);
        assertThatThrownBy(() -> facultyService.removeFaculty(7L))
                .isInstanceOf(FacultyNotFoundException.class);
    }


    @Test
    void filteringFacultyByColorTest() {
        when(facultyRepository.findByColor("Зеленый")).thenReturn(facultyList);
        assertThat(facultyService.filteringFacultyByColor("Зеленый"))
                .contains(new Faculty(1L, "Слизерин", "Зеленый"));
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