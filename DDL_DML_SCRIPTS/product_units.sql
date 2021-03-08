CREATE TABLE public.product_units
(
    id bigint NOT NULL,
    conversion_factor double precision,
    uom character varying(255) COLLATE pg_catalog."default",
    uom_alt character varying(255) COLLATE pg_catalog."default",
    product_id bigint NOT NULL,
    CONSTRAINT product_units_pkey PRIMARY KEY (id),
    CONSTRAINT fk5kqda142dwa9lo7vigynbg4sb FOREIGN KEY (product_id)
        REFERENCES public.products (product_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.product_units
    OWNER to postgres;

    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (1,1,'szt','kg',0.2);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (2,2,'szt','kg',0.6);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (3,3,'kg','szt',2);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (4,4,'kg','szt',1);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (5,5,'szt','l',1.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (6,5,'szt','kg',1.6);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (7,6,'szt','l',0.2);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (8,7,'szt','l',1.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (9,8,'szt','l',1.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (10,9,'szt','l',2);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (11,10,'szt','kg',0.05);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (12,11,'szt','kg',0.08);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (13,12,'szt','l',0.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (14,12,'szt','kg',0.6);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (15,13,'szt','kg',0.3);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (16,13,'szt','l',0.25);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (17,14,'szt','l',0.07);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (18,15,'szt','l',0.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (19,16,'szt','l',0.5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (20,17,'szt','l',0.7);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (21,18,'szt','kg',0.35);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (22,19,'szt','kg',1);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (23,20,'szt','kg',1);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (24,21,'kg','szt',5);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (25,22,'kg','szt',4);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (26,23,'kg','szt',6);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (27,24,'kg','szt',8);
    Insert into PRODUCT_UNITS (ID,PRODUCT_ID,UOM,UOM_ALT,CONVERSION_FACTOR) values (28,25,'kg','szt',7);
