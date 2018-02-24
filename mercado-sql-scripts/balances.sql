CREATE TABLE balances
(
    account_owner VARCHAR2(64) NOT NULL
   ,currency      VARCHAR2(4)  NOT NULL
   ,amount        FLOAT(126)   NOT NULL
);

ALTER TABLE balances
ADD CONSTRAINT bala_pk 
PRIMARY KEY 
(
    account_owner
   ,currency
);

ALTER TABLE balances 
ADD CONSTRAINT bala_acco_fk
FOREIGN KEY (account_owner)
REFERENCES accounts (account_owner);
    
