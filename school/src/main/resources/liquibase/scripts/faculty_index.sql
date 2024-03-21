-- liquibase formatted sql

-- changeset rvetchanin:1
CREATE INDEX faculty_index ON faculties (name,color);