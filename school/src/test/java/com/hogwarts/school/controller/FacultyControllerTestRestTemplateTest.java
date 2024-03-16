package com.hogwarts.school.controller;


import com.hogwarts.school.model.Faculty;
import com.hogwarts.school.model.Student;
import com.hogwarts.school.repositories.FacultyRepository;
import com.hogwarts.school.repositories.StudentRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;
    private final Faker faker = new Faker();

    private final List<Faculty> facultyList = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();

    @AfterEach
    public void afterEach() {
        students.clear();
        facultyList.clear();
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @BeforeEach
    public void beforeEach() {
        for (int i = 0; i < 4; i++) {
            Faculty faculty = new Faculty();
            faculty.setColor(faker.color().name());
            faculty.setName(faker.harryPotter().house());
            facultyList.add(faculty);
            facultyRepository.save(faculty);
        }
        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.setFaculty(facultyList.get(faker.random().nextInt(facultyList.size())));
            student.setAge(faker.random().nextInt(11, 18));
            student.setName(faker.harryPotter().character());
            students.add(student);
            studentRepository.save(student);
        }
    }


    private String buildUrl(String uri) {
        return "http://localhost:%d%s".formatted(port, uri);
    }


    @Test
    void addFacultyPositiveTest() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().character());
        faculty.setColor(faker.color().name());

        ResponseEntity<Faculty> responseEntity = testRestTemplate.postForEntity(buildUrl("/faculty"),
                faculty
                , Faculty.class);

        Faculty created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(created.getId()).isNotNull();

        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);

    }

    @Test
    void getFacultyPositiveTest() {
        Faculty faculty = facultyList.get(faker.random().nextInt(facultyList.size()));

        ResponseEntity<Faculty> getFacultyFromDb = testRestTemplate.getForEntity(buildUrl("/faculty/" + faculty.getId()),
                Faculty.class);

        Faculty get = getFacultyFromDb.getBody();

        assertThat(getFacultyFromDb.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get).isNotNull();
        assertThat(get).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        assertThat(get.getId()).isNotNull();
    }

    @Test
    void getFacultyNegativeTest() {
        ResponseEntity<Faculty> getFacultyFromDb = testRestTemplate.getForEntity(buildUrl("/faculty/-1"),
                Faculty.class);
        assertThat(getFacultyFromDb.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateFacultyPositiveTest() {
        Faculty faculty = facultyList.get(faker.random().nextInt(facultyList.size()));
        Faculty newFaculty = new Faculty(null, faculty.getName() + 1213, faculty.getColor() + 123123);

        HttpEntity<Faculty> entity = new HttpEntity<>(newFaculty);
        ResponseEntity<Faculty> updateFaculty = testRestTemplate.exchange(buildUrl("/faculty/" + faculty.getId()),
                HttpMethod.PUT,
                entity,
                Faculty.class);
        assertThat(updateFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateFaculty.getBody()).isNotNull();
        assertThat(updateFaculty.getBody().getId()).isNotNull();
        assertThat(updateFaculty.getBody()).usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(newFaculty);

    }

    @Test
    void updateFacultyNegativeTest() {
        Faculty newFaculty = new Faculty(null, "Грифиндор1", "Красный1");
        HttpEntity<Faculty> entity = new HttpEntity<>(newFaculty);
        ResponseEntity<Faculty> updateFaculty = testRestTemplate.exchange(buildUrl("/faculty/-1"),
                HttpMethod.PUT,
                entity,
                Faculty.class);
        assertThat(updateFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void removeFacultyPositiveTest() {
        Faculty faculty1 = new Faculty(5L, faker.harryPotter().house(), faker.color().name());
        facultyList.add(faculty1);
        facultyRepository.save(faculty1);
        Faculty faculty = facultyList.get(4);
        ResponseEntity<Faculty> deleteFaculty = testRestTemplate.exchange(buildUrl("/faculty/" + faculty.getId()),
                HttpMethod.DELETE,
                null,
                Faculty.class);
        assertThat(deleteFaculty.getBody()).isNotNull();
        assertThat(deleteFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);


        ResponseEntity<Faculty> getFacultyFromDb = testRestTemplate.getForEntity(buildUrl("/faculty/" + faculty.getId()),
                Faculty.class);


        assertThat(getFacultyFromDb.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        facultyList.remove(4);
    }

    @Test
    void deleteNegativeTest() {
        ResponseEntity<Faculty> deleteFaculty = testRestTemplate.exchange(buildUrl("/faculty/-1"),
                HttpMethod.DELETE,
                null,
                Faculty.class);
        assertThat(deleteFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFacultyByColorTest() {
        //тут можно переписать на получение рандомного факультета и передать цвет факультета как критерий поиска.
        //аналогично можно поступить и с поиском по имени.
        Faculty redFaculty = new Faculty(null, "Красный факультет", "Красный");
        facultyList.add(redFaculty);
        List<Faculty> expected = facultyList.stream().filter(faculty -> faculty.getColor().equals("Красный")).toList();
        testRestTemplate.postForEntity(buildUrl("/faculty"),
                redFaculty
                , Faculty.class);

        ResponseEntity<List<Faculty>> findByColorFaculty = testRestTemplate.exchange(buildUrl("/faculty?color=Красный")
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<>() {
                });

        List<Faculty> actual = findByColorFaculty.getBody();

        assertThat(findByColorFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void getAllTest() {
        List<Faculty> expected = facultyList.stream().toList();
        ResponseEntity<List<Faculty>> getAllFaculty = testRestTemplate.exchange(buildUrl("/faculty")
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<>() {
                });
        List<Faculty> actual = getAllFaculty.getBody();

        assertThat(getAllFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }


    @Test
    void findByNameTest() {
        Faculty redFaculty = new Faculty(null, "Грифиндор", "Красный");
        facultyList.add(redFaculty);
        List<Faculty> expected = facultyList.stream().filter(faculty -> faculty.getName().equals("Грифиндор")).toList();
        ResponseEntity<Faculty> responseEntityRedFaculty = testRestTemplate.postForEntity(buildUrl("/faculty"),
                redFaculty
                , Faculty.class);

        Faculty red = responseEntityRedFaculty.getBody();
        assertThat(red).isNotNull();
        Optional<Faculty> fromDb = facultyRepository.findById(red.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison().ignoringFields("id")
                .isEqualTo(redFaculty);

        ResponseEntity<List<Faculty>> findByColorFaculty = testRestTemplate.exchange(buildUrl("/faculty?colorOrName=Грифиндор")
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<>() {
                });

        List<Faculty> actual = findByColorFaculty.getBody();

        assertThat(findByColorFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual)
                .hasSize(1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void findStudentsPositiveTest() {
        Faculty faculty = facultyList.get(faker.random().nextInt(facultyList.size()));
        List<Student> studentList = students.stream().filter(student -> student.getFaculty().getName().equals(faculty.getName())).toList();
        ResponseEntity<List<Student>> responseEntity = testRestTemplate.exchange(buildUrl("/faculty/{id}/students"), HttpMethod.GET
                , null,
                new ParameterizedTypeReference<>() {
                }, Map.of("id", faculty.getId()));
        List<Student> actual = responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder().isEqualTo(studentList);
    }

    @Test
    void findStudentsNegativeTest() {
        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity(buildUrl("/faculty/{id}/students")
                , Student.class, Map.of("id", -1));
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}