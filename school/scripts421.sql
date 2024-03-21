-- Возраст студента не может быть меньше 16 лет.
-- Имена студентов должны быть уникальными и не равны нулю.
-- При создании студента без возраста ему автоматически должно присваиваться 20 лет.
ALTER  TABLE students ALTER COLUMN name SET NOT NULL, ADD CONSTRAINT name_unique UNIQUE (name),ADD CONSTRAINT age_check CHECK (age >= 16),ALTER COLUMN age SET DEFAULT 20;
-- Пара “значение названия” - “цвет факультета” должна быть уникальной.
ALTER TABLE faculties ADD CONSTRAINT name_color_unique UNIQUE(name,color);
