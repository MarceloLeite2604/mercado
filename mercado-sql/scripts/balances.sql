CREATE TABLE balances (
    acco_owner   VARCHAR2(64 CHAR) NOT NULL,
    currency     VARCHAR2(4 CHAR) NOT NULL,
    amount       NUMBER(20,8) NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX bala_acco_owner_currency_idx ON
    balances ( acco_owner ASC,
    currency ASC );

ALTER TABLE balances ADD CONSTRAINT bala_pk PRIMARY KEY ( acco_owner,
currency );

ALTER TABLE balances
    ADD CONSTRAINT bala_acco_fk FOREIGN KEY ( acco_owner )
        REFERENCES accounts ( owner )
    NOT DEFERRABLE;
