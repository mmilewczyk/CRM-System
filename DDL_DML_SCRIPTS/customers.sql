CREATE TABLE public.customers
(
    customer_id bigint NOT NULL,
    city character varying(255) COLLATE pg_catalog."default",
    firstname character varying(255) COLLATE pg_catalog."default" NOT NULL,
    lastname character varying(255) COLLATE pg_catalog."default" NOT NULL,
    pesel character varying(11) COLLATE pg_catalog."default",
    zip_code character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT customers_pkey PRIMARY KEY (customer_id)
)

TABLESPACE pg_default;

ALTER TABLE public.customers
    OWNER to postgres;

INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(1, 'Płock', 'Bronisław', 'Foka', '72120573948','09-400');
INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(2, 'Płock', 'Joanna', 'Frączak', '80041146587','09-400');
INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(3, 'Gdańsk', 'Kazimiera', 'Wecław', '65091047612','22-100');
INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(4, 'Warszawa', 'Bartosz', 'Przybylski', '01101442863','02-241');
INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(5, 'Kraków', 'Katarzyna', 'Fackiewicz', '85031856317','30-006');
INSERT INTO customers(customer_id, city, firstname, lastname, pesel, zip_code)
VALUES(6, 'Szczecin', 'Krzysztof', 'Jarzyna', '75110621863','70-003');
