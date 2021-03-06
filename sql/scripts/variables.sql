CREATE TABLE variables (
    class_stra_acco_owner   VARCHAR2(64 CHAR) NOT NULL,
    class_stra_currency     VARCHAR2(4 CHAR) NOT NULL,
    class_name              VARCHAR2(256 CHAR) NOT NULL,
    name                    VARCHAR2(256 CHAR) NOT NULL,
    value                   VARCHAR2(2000 CHAR)
)
LOGGING;

CREATE UNIQUE INDEX vari_class_stra_acco_owner_class_stra_currency_class_name_name_idx ON
    variables (
        class_stra_acco_owner
    ASC,
        class_stra_currency
    ASC,
        class_name
    ASC,
        name
    ASC );

ALTER TABLE variables
    ADD CONSTRAINT vari_pk PRIMARY KEY ( class_stra_acco_owner,
    class_stra_currency,
    class_name,
    name );

ALTER TABLE variables
    ADD CONSTRAINT vari_class_fk FOREIGN KEY ( class_stra_acco_owner,
    class_stra_currency,
    class_name )
        REFERENCES classes ( stra_acco_owner,
        stra_currency,
        name )
    NOT DEFERRABLE;
