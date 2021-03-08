CREATE TABLE public.departments
(
    department_code bigint NOT NULL,
    city character varying(255) COLLATE pg_catalog."default",
    department_name character varying(255) COLLATE pg_catalog."default",
    manager_id bigint,
    CONSTRAINT departments_pkey PRIMARY KEY (department_code),
    CONSTRAINT fk6sfjbduxlfax3rii5l9i1tse8 FOREIGN KEY (manager_id)
        REFERENCES public.sys_user (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.departments
    OWNER to postgres;

INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(1, 'Warszawa Śródmieście', 'Księgowość', 18);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(2, 'Piaseczno', 'Produkcja', 20);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(3, 'Warszawa Centrum', 'Administracja', 17);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(4, 'Warszawa Centrum', 'IT', 1);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(5, 'Warszawa Śródmieście', 'Sprzedaż', 16);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(6, 'Warszawa Ochota', 'Planowanie i zakupy', 14);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(7, 'Kraków', 'HR', 19);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(8, 'Łódź', 'Badanie i rozwój', 7);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(9, 'Szczecin', 'Dział prawny', 3);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(10, 'Gdańsk Oliwa', 'Public relations', 2);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(11, 'Poznań', 'Transport', 6);
INSERT INTO departments(department_code, city, department_name, manager_id)
VALUES(12, 'Zakopane', 'Logistyka', 16);
