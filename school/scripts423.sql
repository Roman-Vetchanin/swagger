--Составить первый JOIN-запрос, чтобы получить информацию обо всех студентах
--(достаточно получить только имя и возраст студента) школы Хогвартс вместе с названиями факультетов.
select students.name, students.age, faculties.name as "Факультет" from students left join faculties on students.faculty_id = faculties.id;
--Составить второй JOIN-запрос, чтобы получить только тех студентов, у которых есть аватарки.
select students.name from students inner join avatars on avatars.student_id = students.id;