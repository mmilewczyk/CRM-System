CREATE TABLE public.product_type
(
    product_type_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    discount double precision,
    full_name character varying(255) COLLATE pg_catalog."default",
    period_of_availability character(1) COLLATE pg_catalog."default",
    CONSTRAINT product_type_pkey PRIMARY KEY (product_type_id)
)

TABLESPACE pg_default;

ALTER TABLE public.product_type
    OWNER to postgres;

    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('ALK', 'alkohol', null, 'D');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('KOS', 'kosmetyki', null, 'D');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('NAB', 'nabiał', null, 'K');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('NAP', 'napoje', null, 'D');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('NAS', 'nasiona', null, 'D');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('OWO', 'owoc', null, 'K');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('PIE', 'pieczywo', null, 'K');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('SLO', 'słodycz', null, 'D');
    INSERT INTO product_type(product_type_id, full_name, discount, period_of_availability)
    VALUES('WAR', 'warzywo', null, 'K');
