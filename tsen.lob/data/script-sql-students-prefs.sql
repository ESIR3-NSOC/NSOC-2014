USE nsoc;
DROP TABLE students;
DROP TABLE preference_student;
CREATE TABLE students(student_id SMALLINT NOT NULL, class_name varchar(16) NOT NULL);
INSERT INTO students(student_id,class_name) VALUES (1100,"dom");
INSERT INTO students(student_id,class_name) VALUES (1101,"dom");
INSERT INTO students(student_id,class_name) VALUES (1110,"dom");
INSERT INTO students(student_id,class_name) VALUES (1111,"dom");

CREATE TABLE preference_student(student_id SMALLINT NOT NULL,humidity_ext DOUBLE NOT NULL, temp_ext DOUBLE NOT NULL, temp_int_consigne DOUBLE);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,85,17,23);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,43,21,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,62,19,22);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,24,24,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,50,20,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1100,70,12,24);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,85,17,22);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,43,21,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,62,19,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,24,24,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,50,20,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1101,70,12,23);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,85,17,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,43,21,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,62,19,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,24,24,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,50,20,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1110,70,12,23);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,85,17,22);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,43,21,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,62,19,22);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,24,24,21);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,50,20,20);
INSERT INTO preference_student(student_id,humidity_ext,temp_ext,temp_int_consigne) VALUES (1111,70,12,22);