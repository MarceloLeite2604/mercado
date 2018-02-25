CREATE TABLE accounts
(
    owner VARCHAR2(64) NOT NULL
);

ALTER TABLE accounts
ADD CONSTRAINT acco_pk 
PRIMARY KEY (owner);