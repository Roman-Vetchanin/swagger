-- liquibase formatted sql

-- changeset rvetchanin:1
CREATE INDEX student_name_index ON students (name);