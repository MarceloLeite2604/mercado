CREATE TABLE strategies_parameters
(
    account_owner   VARCHAR2(64)  NOT NULL
   ,strategy_name   VARCHAR2(64)  NOT NULL
   ,parameter_name  VARCHAR2(256) NOT NULL
   ,parameter_value VARCHAR2(256)
);

ALTER TABLE strategies_parameters 
ADD CONSTRAINT stpa_pk
PRIMARY KEY
(
    account_owner
   ,strategy_name
   ,parameter_name
);

ALTER TABLE strategies_parameters
ADD CONSTRAINT stpa_stra_fk
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