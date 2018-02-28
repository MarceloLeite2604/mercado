CREATE TABLE properties (
    name    VARCHAR2(128 CHAR) NOT NULL,
    value   VARCHAR2(2000 CHAR)
)
LOGGING;

CREATE UNIQUE INDEX prop_name_idx ON
    properties ( name ASC );

ALTER TABLE properties ADD CONSTRAINT prop_pk PRIMARY KEY ( name );
