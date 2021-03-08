CREATE TABLE public.absenteeism
(
    id bigint NOT NULL,
    date_from timestamp without time zone,
    date_to timestamp without time zone,
    employee_id bigint NOT NULL,
    roa_code bigint NOT NULL,
    CONSTRAINT absenteeism_pkey PRIMARY KEY (id),
    CONSTRAINT fkl6k7j7sug12nj7n4qi364h2uj FOREIGN KEY (roa_code)
        REFERENCES public.reasons_of_absenteeism (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknbtc1eev8hql0g4ejl1j1nt80 FOREIGN KEY (employee_id)
        REFERENCES public.sys_user (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.absenteeism
    OWNER to postgres;

INSERT INTO absenteeism(id, date_from, date_to, employee_id, roa_code)
VALUES(1, '15-JAN-18', '27-JAN-18', 4, 4);
INSERT INTO absenteeism(id, date_from, date_to, employee_id, roa_code)
VALUES(2, '27-FEB-18', '05-MAR-18', 2, 4);
INSERT INTO absenteeism(id, date_from, date_to, employee_id, roa_code)
VALUES(3, '04-SEP-18', '04-MAR-19', 20, 2);
INSERT INTO absenteeism(id, date_from, date_to, employee_id, roa_code)
VALUES(4, '12-AUG-17', '04-MAY-18', 14, 1);
INSERT INTO absenteeism(id, date_from, date_to, employee_id, roa_code)
VALUES(5, '05-OCT-18', '05-OCT-18', 9, 9);
