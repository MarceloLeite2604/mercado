CREATE TABLE operations (
    orde_acco_owner   VARCHAR2(64 CHAR) NOT NULL,
    orde_id           NUMBER(30) NOT NULL,
    id                NUMBER(30) NOT NULL,
    quantity          NUMBER(20,8) NOT NULL,
    price             NUMBER(20,8) NOT NULL,
    fee_rate          NUMBER(20,8) NOT NULL,
    executed          TIMESTAMP(0) WITH LOCAL TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX oper_orde_acco_owner_orde_id_id_idx ON
    operations (
        orde_acco_owner
    ASC,
        orde_id
    ASC,
        id
    ASC );

CREATE INDEX oper_orde_id_idx ON
    operations ( orde_id ASC );

ALTER TABLE operations
    ADD CONSTRAINT oper_pk PRIMARY KEY ( orde_acco_owner,
    orde_id,
    id );

ALTER TABLE operations
    ADD CONSTRAINT oper_orde_fk FOREIGN KEY ( orde_id )
        REFERENCES orders ( id );
