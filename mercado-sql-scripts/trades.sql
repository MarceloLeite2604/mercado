CREATE TABLE trades
(
    currency   VARCHAR2(4 BYTE)         NOT NULL, 
	trade_id   NUMBER(12,0)             NOT NULL, 
	amount     FLOAT(126)               NOT NULL, 
	trade_date TIMESTAMP WITH TIME ZONE NOT NULL, 
	price      FLOAT(126)               NOT NULL, 
	trade_type VARCHAR2(8 BYTE)         NOT NULL
)
PARTITION BY LIST (currency) 
SUBPARTITION BY RANGE (trade_date)
(
  PARTITION trades_btc VALUES('BTC')
  (
    SUBPARTITION trades_btc_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION trades_btc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_ltc VALUES('LTC')
  (
    SUBPARTITION trades_ltc_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION trades_ltc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_bch VALUES('BCH')
  (
    SUBPARTITION trades_bch_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION trades_bch_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_btg VALUES('BTG')
  (
    SUBPARTITION trades_btg_2013   VALUES LESS THAN (to_date('01/01/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2014_1 VALUES LESS THAN (to_date('01/06/2014', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2014_2 VALUES LESS THAN (to_date('01/01/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2015_1 VALUES LESS THAN (to_date('01/06/2015', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2015_2 VALUES LESS THAN (to_date('01/01/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2016_1 VALUES LESS THAN (to_date('01/06/2016', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2016_2 VALUES LESS THAN (to_date('01/01/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2017_1 VALUES LESS THAN (to_date('01/06/2017', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2017_2 VALUES LESS THAN (to_date('01/01/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2018_1 VALUES LESS THAN (to_date('01/06/2018', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2018_2 VALUES LESS THAN (to_date('01/01/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2019_1 VALUES LESS THAN (to_date('01/06/2019', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_2019_2 VALUES LESS THAN (to_date('01/01/2020', 'DD/MM/YYYY')),
    SUBPARTITION trades_btg_others VALUES LESS THAN (maxvalue)
  )
);

ALTER TABLE trades ADD CONSTRAINT trades_pk PRIMARY KEY (currency, trade_id);

CREATE INDEX trades_idx1 ON trades (currency, trade_date);

CREATE INDEX trades_idx2 ON trades (currency, trade_type, trade_date);