CREATE TABLE strategies
(
    account_owner VARCHAR2(64) NOT NULL
   ,strategy_name VARCHAR2(64) NOT NULL
   ,strategy_class VARCHAR2(256) NOT NULL
);

ALTER TABLE strategies
ADD CONSTRAINT stra_pk
PRIMARY KEY
(
    account_owner
   ,strategy_name
);

ALTER TABLE strategies
ADD CONSTRAINT stra_acco_fk
FOREIGN KEY(account_owner)
REFERENCES accounts(account_owner);