CREATE TABLE strategies_variables
(
    account_owner  VARCHAR2(64)  NOT NULL
   ,strategy_name  VARCHAR2(64)  NOT NULL
   ,variable_name  VARCHAR2(256) NOT NULL
   ,variable_value VARCHAR2(256)
);

ALTER TABLE strategies_variables 
ADD CONSTRAINT stva_pk
PRIMARY KEY
(
    account_owner
   ,strategy_name
   ,variable_name
);

ALTER TABLE strategies_variables
ADD CONSTRAINT stpa_stva_fk
FOREIGN KEY
(
    account_owner
   ,strategy_name
)
REFERENCES strategies
(
    account_owner
   ,strategy_name
);