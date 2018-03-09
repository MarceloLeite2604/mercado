CREATE TABLE classes (
    stra_acco_owner   VARCHAR2(64 CHAR) NOT NULL,
    stra_currency     VARCHAR2(4 CHAR) NOT NULL,
    name              VARCHAR2(256 CHAR) NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX class_stra_acco_owner_stra_currency_name_idx ON
    classes (
        stra_acco_owner
    ASC,
        stra_currency
    ASC,
        name
    ASC );

ALTER TABLE classes
    ADD CONSTRAINT class_pk PRIMARY KEY ( stra_acco_owner,
    stra_currency,
    name );

ALTER TABLE classes
    ADD CONSTRAINT class_stra_fk FOREIGN KEY ( stra_acco_owner,
    stra_currency )
        REFERENCES strategies ( acco_owner,
        currency )
    NOT DEFERRABLE;
