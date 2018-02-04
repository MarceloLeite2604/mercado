CREATE TABLE temporal_tickers
(
  currency            VARCHAR2(4 BYTE) NOT NULL,
  start_time          TIMESTAMP        NOT NULL,
  end_time            TIMESTAMP        NOT NULL,
  orders              NUMBER(9,0)      NOT NULL,
  buy_orders          NUMBER(9,0)      NOT NULL,
  sell_orders         NUMBER(9,0)      NOT NULL,
  buy                 FLOAT(126)       NOT NULL,
  previous_buy        FLOAT(126)       NOT NULL,
  sell                FLOAT(126)       NOT NULL,
  previous_sell       FLOAT(126)       NOT NULL,
  last_price          FLOAT(126)       NOT NULL,
  previous_last_price FLOAT(126)       NOT NULL,
  first_price         FLOAT(126)       NOT NULL,
  highest_price       FLOAT(126)       NOT NULL,
  lowest_price        FLOAT(126)       NOT NULL,
  average_price       FLOAT(126)       NOT NULL,
  time_duration       NUMBER(10,0)     NOT NULL,
  volume_traded       FLOAT(126)       NOT NULL
)
PARTITION BY LIST (currency) 
SUBPARTITION BY RANGE (start_time)
(
  PARTITION trades_btc VALUES('BTC')
  (
    SUBPARTITION temporal_tickers_btc_2013   VALUES LESS THAN (to_timestamp_tz('01/01/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2014_1 VALUES LESS THAN (to_timestamp_tz('01/06/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2014_2 VALUES LESS THAN (to_timestamp_tz('01/01/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2015_1 VALUES LESS THAN (to_timestamp_tz('01/06/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2015_2 VALUES LESS THAN (to_timestamp_tz('01/01/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2016_1 VALUES LESS THAN (to_timestamp_tz('01/06/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2016_2 VALUES LESS THAN (to_timestamp_tz('01/01/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2017_1 VALUES LESS THAN (to_timestamp_tz('01/06/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2017_2 VALUES LESS THAN (to_timestamp_tz('01/01/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2018_1 VALUES LESS THAN (to_timestamp_tz('01/06/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2018_2 VALUES LESS THAN (to_timestamp_tz('01/01/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2019_1 VALUES LESS THAN (to_timestamp_tz('01/06/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_2019_2 VALUES LESS THAN (to_timestamp_tz('01/01/2020 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_ltc VALUES('LTC')
  (
    SUBPARTITION temporal_tickers_ltc_2013   VALUES LESS THAN (to_timestamp_tz('01/01/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2014_1 VALUES LESS THAN (to_timestamp_tz('01/06/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2014_2 VALUES LESS THAN (to_timestamp_tz('01/01/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2015_1 VALUES LESS THAN (to_timestamp_tz('01/06/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2015_2 VALUES LESS THAN (to_timestamp_tz('01/01/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2016_1 VALUES LESS THAN (to_timestamp_tz('01/06/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2016_2 VALUES LESS THAN (to_timestamp_tz('01/01/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2017_1 VALUES LESS THAN (to_timestamp_tz('01/06/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2017_2 VALUES LESS THAN (to_timestamp_tz('01/01/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2018_1 VALUES LESS THAN (to_timestamp_tz('01/06/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2018_2 VALUES LESS THAN (to_timestamp_tz('01/01/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2019_1 VALUES LESS THAN (to_timestamp_tz('01/06/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_2019_2 VALUES LESS THAN (to_timestamp_tz('01/01/2020 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_ltc_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_bch VALUES('BCH')
  (
    SUBPARTITION temporal_tickers_bch_2013   VALUES LESS THAN (to_timestamp_tz('01/01/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2014_1 VALUES LESS THAN (to_timestamp_tz('01/06/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2014_2 VALUES LESS THAN (to_timestamp_tz('01/01/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2015_1 VALUES LESS THAN (to_timestamp_tz('01/06/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2015_2 VALUES LESS THAN (to_timestamp_tz('01/01/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2016_1 VALUES LESS THAN (to_timestamp_tz('01/06/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2016_2 VALUES LESS THAN (to_timestamp_tz('01/01/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2017_1 VALUES LESS THAN (to_timestamp_tz('01/06/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2017_2 VALUES LESS THAN (to_timestamp_tz('01/01/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2018_1 VALUES LESS THAN (to_timestamp_tz('01/06/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2018_2 VALUES LESS THAN (to_timestamp_tz('01/01/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2019_1 VALUES LESS THAN (to_timestamp_tz('01/06/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_2019_2 VALUES LESS THAN (to_timestamp_tz('01/01/2020 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_bch_others VALUES LESS THAN (maxvalue)
  ),
  PARTITION trades_btg VALUES('BTG')
  (
    SUBPARTITION temporal_tickers_btg_2013   VALUES LESS THAN (to_timestamp_tz('01/01/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2014_1 VALUES LESS THAN (to_timestamp_tz('01/06/2014 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2014_2 VALUES LESS THAN (to_timestamp_tz('01/01/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2015_1 VALUES LESS THAN (to_timestamp_tz('01/06/2015 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2015_2 VALUES LESS THAN (to_timestamp_tz('01/01/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2016_1 VALUES LESS THAN (to_timestamp_tz('01/06/2016 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2016_2 VALUES LESS THAN (to_timestamp_tz('01/01/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2017_1 VALUES LESS THAN (to_timestamp_tz('01/06/2017 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2017_2 VALUES LESS THAN (to_timestamp_tz('01/01/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2018_1 VALUES LESS THAN (to_timestamp_tz('01/06/2018 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2018_2 VALUES LESS THAN (to_timestamp_tz('01/01/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2019_1 VALUES LESS THAN (to_timestamp_tz('01/06/2019 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_2019_2 VALUES LESS THAN (to_timestamp_tz('01/01/2020 00:00:00 00:00', 'DD/MM/YYYY HH24:MI:SS TZH:TZM')),
    SUBPARTITION temporal_tickers_btg_others VALUES LESS THAN (maxvalue)
  )
);

ALTER TABLE temporal_tickers ADD PRIMARY KEY (currency, start_time, end_time);
