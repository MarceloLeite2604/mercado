CREATE TABLE strategies
(
    acco_owner VARCHAR2(64) NOT NULL
   ,currency   VARCHAR2(4)  NOT NULL
);

ALTER TABLE strategies
ADD CONSTRAINT stra_pk
PRIMARY KEY
(
    acco_owner
   ,currency
);

ALTER TABLE strategies
ADD CONSTRAINT stra_acco_fk
FOREIGN KEY(acco_owner)
REFERENCES accounts(owner);