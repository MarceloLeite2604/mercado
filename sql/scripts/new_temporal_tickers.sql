CREATE TABLE temporal_tickers
(
  "ID"                 NUMBER(19,0)     NOT NULL
 ,"CURRENCY"           VARCHAR2(4 BYTE) NOT NULL
 ,"START"              DATE             NOT NULL
 ,"END"                DATE             NOT NULL
 ,"ORDERS"             NUMBER(19,0)     NOT NULL
 ,"BUY_ORDERS"         NUMBER(19,0)     NOT NULL
 ,"SELL_ORDERS"        NUMBER(19,0)     NOT NULL
 ,"BUY"                NUMBER(20,8)
 ,"PREVIOUS_BUY"       NUMBER(20,8)
 ,"SELL"               NUMBER(20,8)
 ,"PREVIOUS_SELL"      NUMBER(20,8)
 ,"LAST"               NUMBER(20,8)
 ,"PREVIOUS_LAST"      NUMBER(20,8)
 ,"FIRST"              NUMBER(20,8)
 ,"HIGHEST"            NUMBER(20,8)
 ,"LOWEST"             NUMBER(20,8)
 ,"AVERAGE"            NUMBER(20,8)
 ,"DURATION"           NUMBER(19,0)     NOT NULL
 ,"VOLUME_TRADED"      NUMBER(20,8)     NOT NULL
)
PARTITION BY LIST ("CURRENCY") 
SUBPARTITION BY RANGE ("START")
(
  PARTITION trades_btc VALUES('BTC')
  (
    SUBPARTITION temporal_tickers_btc_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_ltc VALUES('LTC')
  (
    SUBPARTITION temporal_tickers_ltc_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_ltc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_bch VALUES('BCH')
  (
    SUBPARTITION temporal_tickers_bch_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_bch_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_btg VALUES('BTG')
  (
    SUBPARTITION temporal_tickers_btg_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION temporal_tickers_btg_others VALUES LESS THAN (maxvalue)
  )
);

ALTER TABLE temporal_tickers ADD CONSTRAINT teti_pk PRIMARY KEY ("ID");
ALTER TABLE temporal_tickers ADD CONSTRAINT teti_uk UNIQUE ("CURRENCY", "START", "END");

CREATE INDEX teti_idx1 ON temporal_tickers("CURRENCY", "DURATION", "START");
