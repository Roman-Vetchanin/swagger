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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTestRestTemplateTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private final Faker faker = new Faker();
    private final List<Student> students = new ArrayList<>(11);


    @BeforeEach
    public void beforeEach() {
        Faculty faculty = createFaculty();
        Faculty faculty1 = createFaculty();
        createStudents(faculty, faculty1);
    }

    @AfterEach
    void afterEach() {
        studentRepository.deleteAll();
        studentRepository.deleteAll();
    }

    private Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }

    private void createStudents(Faculty... faculties) {
        students.clear();
        Stream.of(faculties)
                .forEach(faculty ->
                        students.addAll(studentRepository.saveAll(Stream.generate(() -> {
                                    Student student = new Student();
                                    student.setFaculty(faculty);
                                    student.setName(faker.harryPotter().character());
                                    student.setAge(faker.random().nextInt(11, 18));
                                    return student;
                                }).limit(5)
                                .collect(Collectors.toList())))
                );


    }

    private String buildUrl(String uri) {
        return "http://localhost:%d%s".formatted(port, uri);
    }


    @Test
    void addStudentTest() {
        Student student = new Student();
        student.setFaculty(createFaculty());
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(11, 18));

        ResponseEntity<Student> responseEntity = testRestTemplate.postForEntity(buildUrl("/student"),
                student
                , Student.class);

        Student created = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(created.getId()).isNotNull();

        Optional<Student> fromDb = studentRepository.findById(created.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .isEqualTo(created);
    }
    @Test
    void getStudentTest() {
        Student student = students.get(3);

        ResponseEntity<Student> getStudentFromDb = testRestTemplate.getForEntity(buildUrl("/student/"+student.getId()),
                Student.class);

        Student get = getStudentFromDb.getBody();

        assertThat(getStudentFromDb.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get).isNotNull();
        assertThat(get).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(get.getId()).isNotNull();
    }
    @Test
    void updateStudentPositiveTest() {
        Student oldStudent = students.get(faker.random().nextInt(students.size()));
        Long id = oldStudent.getId();
        Student newStudent = new Student();
        newStudent.setId(id);
        newStudent.setName("newName");
        newStudent.setAge(oldStudent.getAge()+5);
        HttpEntity<Student> entity = new HttpEntity<>(newStudent);
        ResponseEntity<Student> updateFaculty = testRestTemplate.exchange(buildUrl("/student/"+id),
                HttpMethod.PUT,
                entity,
                Student.class);
        assertThat(oldStudent).isNotEqualTo(newStudent);
        assertThat(updateFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateFaculty.getBody()).isNotNull();
        assertThat(updateFaculty.getBody().getId()).isNotNull();
        assertThat(updateFaculty.getBody())
                .isEqualTo(newStudent);

    }

    @Test
    void updateStudentNegativeTest() {
        Student student = new Student(null, faker.harryPotter().character(), faker.random().nextInt(11, 18));
        testRestTemplate.postForEntity(buildUrl("/student"),
                student
                , Student.class);
        Student newStudent = new Student(null, student.getName() + 123, student.getAge() + 5);

        HttpEntity<Student> entity = new HttpEntity<>(newStudent);
        ResponseEntity<Student> updateFaculty = testRestTemplate.exchange(buildUrl("/student/-1"),
                HttpMethod.PUT,
                entity,
                Student.class);
        assertThat(updateFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
    @Test
    void removeStudentPositiveTest() {
        Student student = students.get(9);
        ResponseEntity<Student> deleteFaculty = testRestTemplate.exchange(buildUrl("/student/"+student.getId()),
                HttpMethod.DELETE,
                null,
                Student.class);
        assertThat(deleteFaculty.getBody()).isNotNull();
        assertThat(deleteFaculty.getStatusCode()).isEqualTo(HttpStatus.OK);


        ResponseEntity<Faculty> getFacultyFromDb = testRestTemplate.getForEntity(buildUrl("/faculty/"+student.getId()),
                Faculty.class);


        assertThat(getFacultyFromDb.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void removeStudentNegativeTest() {
        ResponseEntity<Student> deleteFaculty = testRestTemplate.exchange(buildUrl("/student/-1"),
                HttpMethod.DELETE,
                null,
                Student.class);
        assertThat(deleteFaculty.getBody()).isNotNull();
        assertThat(deleteFaculty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getStudentByAgePositiveTest() {
        Student student = students.get(faker.random().nextInt(students.size()));

        ResponseEntity<Student> getStudentFromDb = testRestTemplate.getForEntity(buildUrl("/student/"+student.getId()),
                Student.class);

        Student get = getStudentFromDb.getBody();

        assertThat(getStudentFromDb.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get).isNotNull();
        assertThat(get).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student);
        assertThat(get.getId()).isNotNull();
    }
    @Test
    void getStudentByAgeNegativeTest() {
        ResponseEntity<Student> getStudentFromDb = testRestTemplate.getForEntity(buildUrl("/student/-1"),
                Student.class);

        assertThat(getStudentFromDb.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    void getAll() {
        List<Student> expected = students.stream().toList();
        ResponseEntity<List<Student>> getAllStudent = testRestTemplate.exchange(buildUrl("/student")
                , HttpMethod.GET
                , null,
                new ParameterizedTypeReference<>() {
                });
        List<Student> actual = getAllStudent.getBody();

        assertThat(getAllStudent.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual)
                .hasSize(10)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void findByAgeBetween() {
        int minAge = faker.random().nextInt(11, 18);
        int maxAge = faker.random().nextInt(minAge, 18);
        List<Student> expected = students.stream()
                .filter(student -> student.getAge() >= minAge && student.getAge() <= maxAge).toList();
        ResponseEntity<List<Student>> findBetweenAge = testRestTemplate.exchange(buildUrl("/student?minAge={minAge}&maxAge={maxAge}")
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<>() {
                }, Map.of("minAge", minAge, "maxAge", maxAge));
        List<Student> actual = findBetweenAge.getBody();

        assertThat(findBetweenAge.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expected);
    }

    @Test
    void findFacultyPositiveTest() {
        Student student = students.get(faker.random().nextInt(students.size()));
        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(buildUrl("/student/{id}/faculty")
                , Faculty.class
                , Map.of("id", student.getId()));
        Faculty actual = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(student.getFaculty());
    }

    @Test
    void findFacultyNegativeTest() {
        ResponseEntity<Faculty> responseEntity = testRestTemplate.getForEntity(buildUrl("/student/{id}/faculty")
                , Faculty.class
                , Map.of("id", -1));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}