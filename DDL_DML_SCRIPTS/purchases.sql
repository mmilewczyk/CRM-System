CREATE TABLE public.purchases
(
    purchase_id bigint NOT NULL,
    invoice_exist character(1) COLLATE pg_catalog."default",
    purchase_date timestamp without time zone,
    receipt_exist character(1) COLLATE pg_catalog."default",
    customer_id bigint NOT NULL,
    invoice_id bigint,
    CONSTRAINT purchases_pkey PRIMARY KEY (purchase_id),
    CONSTRAINT fk9q5yt0n9s4ube31bxfamhir44 FOREIGN KEY (customer_id)
        REFERENCES public.customers (customer_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fknvb29p9mxwmhew3gyakuvh5pu FOREIGN KEY (invoice_id)
        REFERENCES public.selling_invoice (invoice_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.purchases
    OWNER to postgres;

Insert into PURCHASES (PURCHASE_ID,CUSTOMER_ID,RECEIPT_EXIST,INVOICE_EXIST,INVOICE_ID,PURCHASE_DATE) values (1,4,'N','T',1,to_date('04-FEB-18','DD-MON-RR'));
Insert into PURCHASES (PURCHASE_ID,CUSTOMER_ID,RECEIPT_EXIST,INVOICE_EXIST,INVOICE_ID,PURCHASE_DATE) values (2,2,'T','N',null,to_date('04-SEP-18','DD-MON-RR'));
Insert into PURCHASES (PURCHASE_ID,CUSTOMER_ID,RECEIPT_EXIST,INVOICE_EXIST,INVOICE_ID,PURCHASE_DATE) values (3,6,'T','N',null,to_date('04-DEC-18','DD-MON-RR'));
