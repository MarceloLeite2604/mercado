CREATE TABLE balances
(
    acco_owner VARCHAR2(64) NOT NULL
   ,currency   VARCHAR2(4)  NOT NULL
   ,amount     FLOAT(126)   NOT NULL
);

ALTER TABLE balances
ADD CONSTRAINT bala_pk 
PRIMARY KEY 
(
    acco_owner
   ,currency
);

ALTER TABLE balances 
ADD CONSTRAINT bala_acco_fk
FOREIGN KEY (acco_owner)
REFERENCES accounts (owner);
    
