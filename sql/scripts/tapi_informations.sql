CREATE TABLE tapi_informations (
    acco_owner   VARCHAR2(64 CHAR) NOT NULL,
    id           VARCHAR2(1024 CHAR) NOT NULL,
    secret       VARCHAR2(1024 CHAR) NOT NULL
);

CREATE UNIQUE INDEX tapi_acco_owner_id_idx ON
    tapi_informations ( acco_owner ASC,
    id ASC );

ALTER TABLE tapi_informations ADD CONSTRAINT tapi_pk PRIMARY KEY ( acco_owner,
id );

ALTER TABLE tapi_informations
    ADD CONSTRAINT tapi_acco_fk FOREIGN KEY ( acco_owner )
        REFERENCES accounts ( owner );
