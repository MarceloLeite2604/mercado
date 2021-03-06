CREATE TABLE accounts (
    owner   VARCHAR2(64 CHAR) NOT NULL,
    email   VARCHAR2(64 CHAR)
);

CREATE UNIQUE INDEX acco_owner_idx ON
    accounts ( owner ASC );

ALTER TABLE accounts ADD CONSTRAINT acco_pk PRIMARY KEY ( owner );
