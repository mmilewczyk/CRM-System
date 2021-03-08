CREATE TABLE public.types_of_transport
(
    code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    full_name character varying(255) COLLATE pg_catalog."default",
    max_length double precision,
    max_weight double precision,
    min_length double precision,
    min_weight double precision,
    transport_capacity integer,
    CONSTRAINT types_of_transport_pkey PRIMARY KEY (code)
)

TABLESPACE pg_default;

ALTER TABLE public.types_of_transport
    OWNER to postgres;

    Insert into TYPES_OF_TRANSPORT (CODE,FULL_NAME,MIN_LENGTH,MAX_LENGTH,MIN_WEIGHT,MAX_WEIGHT,TRANSPORT_CAPACITY) values ('VAN','van',3.5,4,1800,2400,1000);
    Insert into TYPES_OF_TRANSPORT (CODE,FULL_NAME,MIN_LENGTH,MAX_LENGTH,MIN_WEIGHT,MAX_WEIGHT,TRANSPORT_CAPACITY) values ('BUS','bus',3.8,4.2,1950,2800,1200);
    Insert into TYPES_OF_TRANSPORT (CODE,FULL_NAME,MIN_LENGTH,MAX_LENGTH,MIN_WEIGHT,MAX_WEIGHT,TRANSPORT_CAPACITY) values ('AUT','auto osobowe',2,3.2,800,1400,600);
    Insert into TYPES_OF_TRANSPORT (CODE,FULL_NAME,MIN_LENGTH,MAX_LENGTH,MIN_WEIGHT,MAX_WEIGHT,TRANSPORT_CAPACITY) values ('TIR','tir',12,20,5000,10000,30000);
    Insert into TYPES_OF_TRANSPORT (CODE,FULL_NAME,MIN_LENGTH,MAX_LENGTH,MIN_WEIGHT,MAX_WEIGHT,TRANSPORT_CAPACITY) values ('SKUT','skuter',1.65,2.24,300,500,200);
