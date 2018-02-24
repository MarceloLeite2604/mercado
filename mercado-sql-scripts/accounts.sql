CREATE TABLE accounts
(
    account_owner VARCHAR2(64) NOT NULL
);

ALTER TABLE accounts
ADD CONSTRAINT acco_pk 
PRIMARY KEY (account_owner);