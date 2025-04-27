-- liquibase formatted sql

-- changeset liquibase:2
INSERT INTO criteria_type (id, name) VALUES (DEFAULT, 'Amount');
INSERT INTO criteria_type (id, name) VALUES (DEFAULT, 'Title');
INSERT INTO criteria_type (id, name) VALUES (DEFAULT, 'Date');

INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'More');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'Less');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'Equals');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'Starts with');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'Ends with');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'From');
INSERT INTO comparing_condition (id, name) VALUES (DEFAULT, 'To');

INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 1, 1);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 1,2);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 1,3);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 2, 4);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 2,5);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 2,3);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 3, 6);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 3, 7);
INSERT INTO criteria_type_cc (id, criteria_type_id, comparing_condition_id)VALUES (DEFAULT, 3, 3);

INSERT INTO filter (id, name) VALUES (DEFAULT, 'Colaqueen filter');
INSERT INTO filter (id, name) VALUES (DEFAULT, 'Markminyster filter');

INSERT INTO criteria (id, criteria_type_cc_id, amount) VALUES (DEFAULT, 1, 500);
INSERT INTO criteria (id, criteria_type_cc_id, title) VALUES (DEFAULT, 4, 'Alksitaksi');
INSERT INTO criteria (id, criteria_type_cc_id, date) VALUES (DEFAULT, 8, CURRENT_DATE);
INSERT INTO criteria (id, criteria_type_cc_id, amount) VALUES (DEFAULT, 3, 9002006);
INSERT INTO criteria (id, criteria_type_cc_id, date) VALUES (DEFAULT, 7, DATE '2001-09-11');

INSERT INTO filter_criteria (id, criteria_id, filter_id) VALUES (DEFAULT, 1, 1);
INSERT INTO filter_criteria (id, criteria_id, filter_id) VALUES (DEFAULT, 2, 1);
INSERT INTO filter_criteria (id, criteria_id, filter_id) VALUES (DEFAULT, 3, 1);
INSERT INTO filter_criteria (id, criteria_id, filter_id) VALUES (DEFAULT, 4, 2);
INSERT INTO filter_criteria (id, criteria_id, filter_id) VALUES (DEFAULT, 5, 2);

