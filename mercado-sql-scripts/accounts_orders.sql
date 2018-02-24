CREATE TABLE accounts_orders
(
    account_id      NUMBER(9)                      NOT NULL
   ,order_time      TIMESTAMP WITH LOCAL TIME ZONE NOT NULL
   ,order_type      VARCHAR2(8)                    NOT NULL
   ,input_currency  VARCHAR2(4)                    NOT NULL
   ,input_amount    FLOAT(126)                         NULL
   ,output_currency VARCHAR2(4)                    NOT NULL
   ,output_amount   FLOAT(126)                         NULL
   ,order_status                                   NOT NULL
);