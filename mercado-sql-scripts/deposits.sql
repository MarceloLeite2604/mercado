CREATE TABLE deposits
(
    account_id   NUMBER(9)                      NOT NULL
   ,deposit_time TIMESTAMP WITH LOCAL TIME ZONE NOT NULL
   ,currency     VARCHAR2(4)                    NOT NULL
   ,amount       FLOAT(126)                     NOT NULL
);