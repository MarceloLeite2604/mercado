CREATE TABLE strategies_parameters
(
    stra_acco_owner VARCHAR2(64)  NOT NULL
   ,stra_currency   VARCHAR2(4)   NOT NULL
   ,name            VARCHAR2(256) NOT NULL
   ,value           VARCHAR2(256)
);

ALTER TABLE strategies_parameters 
ADD CONSTRAINT stpa_pk
PRIMARY KEY
(
    stra_acco_owner
   ,stra_currency
   ,name
);

ALTER TABLE strategies_parameters
ADD CONSTRAINT stpa_stra_fk
FOREIGN KEY
(
    stra_acco_owner
   ,stra_currency
)
REFERENCES strategies
(
    acco_owner
   ,currency
);