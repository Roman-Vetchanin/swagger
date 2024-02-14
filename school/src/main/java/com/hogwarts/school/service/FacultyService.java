package com.hogwarts.school.service;

import com.hogwarts.school.model.Faculty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();

    private Long generateId = 1L;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(generateId);
        facultyMap.put(faculty.getId(), faculty);
        generateId++;
        return faculty;
    }

    public Faculty getFaculty(Long studentId) {
        return facultyMap.get(studentId);
    }


    public Faculty updateFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty removeFaculty(Long facultyId) {
        return facultyMap.remove(facultyId);
    }
    public List<Faculty> filteringFacultyByColor(String color) {
        return findAllFaculty().stream().filter(faculty -> faculty.getColor().equals(color)).collect(Collectors.toList());
    }

    public Collection<Faculty> findAllFaculty() {
        return Collections.unmodifiableCollection(facultyMap.values());
    }
}
