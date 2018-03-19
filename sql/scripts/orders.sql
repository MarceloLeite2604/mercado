CREATE TABLE orders (
    acco_owner               VARCHAR2(64 CHAR) NOT NULL,
    id                       NUMBER(30) NOT NULL,
    first_currency           VARCHAR2(4 CHAR) NOT NULL,
    second_currency          VARCHAR2(4 CHAR) NOT NULL,
    type                     NUMBER(1) NOT NULL,
    status                   NUMBER(1) NOT NULL,
    has_fills                NUMBER(1),
    quantity                 NUMBER(20,8) NOT NULL,
    limit_price              NUMBER(20,8) NOT NULL,
    executed_quantity        NUMBER(20,8),
    executed_price_average   NUMBER(20,8),
    fee                      NUMBER(20,8),
    created                  TIMESTAMP(0) WITH LOCAL TIME ZONE,
    updated                  TIMESTAMP(0) WITH LOCAL TIME ZONE,
    intended                 TIMESTAMP(0) WITH LOCAL TIME ZONE
);

CREATE UNIQUE INDEX orde_id_idx ON
    orders ( id ASC );

CREATE INDEX orde_acco_owner_idx ON
    orders ( acco_owner ASC );

ALTER TABLE orders ADD CONSTRAINT orde_pk PRIMARY KEY ( id );

ALTER TABLE orders
    ADD CONSTRAINT orde_acco_fk FOREIGN KEY ( acco_owner )
        REFERENCES accounts ( owner );
