-- liquibase formatted sql

-- changeset liquibase:1
CREATE TABLE comparing_condition (
                                     id serial  NOT NULL,
                                     name varchar(255)  NOT NULL,
                                     CONSTRAINT comparing_condition_pk PRIMARY KEY (id)
);

CREATE TABLE criteria (
                          id serial  NOT NULL,
                          criteria_type_cc_id int  NOT NULL,
                          amount int  NULL,
                          title varchar(255)  NULL,
                          date date  NULL,
                          CONSTRAINT criteria_pk PRIMARY KEY (id)
);

CREATE TABLE criteria_type (
                               id serial  NOT NULL,
                               name varchar(255)  NOT NULL,
                               CONSTRAINT criteria_type_pk PRIMARY KEY (id)
);

CREATE TABLE criteria_type_cc (
                                  id serial  NOT NULL,
                                  criteria_type_id int  NOT NULL,
                                  comparing_condition_id int  NOT NULL,
                                  CONSTRAINT criteria_type_cc_pk PRIMARY KEY (id)
);

CREATE TABLE filter (
                        id serial  NOT NULL,
                        name varchar(255)  NOT NULL,
                        CONSTRAINT filter_pk PRIMARY KEY (id)
);

CREATE TABLE filter_criteria (
                                 id serial  NOT NULL,
                                 filter_id int  NOT NULL,
                                 criteria_id int  NOT NULL,
                                 CONSTRAINT filter_criteria_pk PRIMARY KEY (id)
);

ALTER TABLE criteria ADD CONSTRAINT criteria_criteria_type_cc
    FOREIGN KEY (criteria_type_cc_id)
        REFERENCES criteria_type_cc (id)
        NOT DEFERRABLE
;

ALTER TABLE criteria_type_cc ADD CONSTRAINT criteria_type_cc_comparing_condition
    FOREIGN KEY (comparing_condition_id)
        REFERENCES comparing_condition (id)
        NOT DEFERRABLE
;

ALTER TABLE criteria_type_cc ADD CONSTRAINT criteria_type_cc_criteria_type
    FOREIGN KEY (criteria_type_id)
        REFERENCES criteria_type (id)
        NOT DEFERRABLE
;

ALTER TABLE filter_criteria ADD CONSTRAINT filter_criteria_criteria
    FOREIGN KEY (criteria_id)
        REFERENCES criteria (id)
        NOT DEFERRABLE
;

ALTER TABLE filter_criteria ADD CONSTRAINT filter_criteria_filter
    FOREIGN KEY (filter_id)
        REFERENCES filter (id)
        NOT DEFERRABLE
;