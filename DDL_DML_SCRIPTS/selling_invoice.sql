CREATE TABLE public.selling_invoice
(
    invoice_id bigint NOT NULL,
    currency character varying(255) COLLATE pg_catalog."default" NOT NULL,
    gross_value double precision NOT NULL,
    invoice_date timestamp without time zone NOT NULL,
    net_worth double precision NOT NULL,
    tax_rate double precision NOT NULL,
    customer_id bigint NOT NULL,
    CONSTRAINT selling_invoice_pkey PRIMARY KEY (invoice_id),
    CONSTRAINT fkbcmnap6gemq6t69c1e3pc7ot9 FOREIGN KEY (customer_id)
        REFERENCES public.customers (customer_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.selling_invoice
    OWNER to postgres;

Insert into SELLING_INVOICE (INVOICE_ID,INVOICE_DATE,CUSTOMER_ID,NET_WORTH,GROSS_VALUE,TAX_RATE,CURRENCY)
values (1,to_date('25-NOV-18','DD-MON-RR'),5,25,30.75,23,'PLN');
