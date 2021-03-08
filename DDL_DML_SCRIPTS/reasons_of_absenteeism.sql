CREATE TABLE public.reasons_of_absenteeism
(
    id bigint NOT NULL,
    absenteeism_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    comments character varying(255) COLLATE pg_catalog."default",
    consent character(1) COLLATE pg_catalog."default",
    CONSTRAINT reasons_of_absenteeism_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.reasons_of_absenteeism
    OWNER to postgres;

INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(1, 'urlop macierzyński', 'N', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(2, 'urlop ojcowski', 'N', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(3, 'urlop rodzicielski', 'N', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(4, 'urlop wypoczynkowy', 'T', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(5, 'urlop wychowawczy', 'N', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(6, 'urlop szkoleniowy', 'T', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(7, 'urlop okolicznościowy', 'T', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(8, 'urlop bezpłatny', 'T', null);
INSERT INTO reasons_of_absenteeism(id, absenteeism_name, comments, consent)
VALUES(9, 'na żądanie', 'N', null);
