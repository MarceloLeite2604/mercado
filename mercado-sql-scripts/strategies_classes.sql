CREATE TABLE strategies_classes
(
    stra_acco_owner VARCHAR2(64)  NOT NULL
   ,stra_currency   VARCHAR2(4)   NOT NULL
   ,class_name      VARCHAR2(256) NOT NULL
);

ALTER TABLE strategies_classes 
ADD CONSTRAINT stcl_pk
PRIMARY KEY
(
    stra_acco_owner
   ,stra_currency
   ,class_name
);

ALTER TABLE strategies_classes
ADD CONSTRAINT stcl_stra_fk
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