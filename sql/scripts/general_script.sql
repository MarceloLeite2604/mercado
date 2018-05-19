-- DELETE FROM trades;
-- DELETE FROM temporal_tickers;
-- COMMIT;

SELECT count(*) FROM trades;
-- First run: 50523
-- Second run: 50523
-- Third run: 57078
-- Fourth run: 77206

SELECT count(*) FROM temporal_tickers;
-- First run: 186 (31*2*3) 
-- Second run: 367 (186 + ((60*3) + 1))
-- Third run: 5227

SELECT count(*) FROM temporal_tickers WHERE "LAST" is null AND "PREVIOUS_LAST" is null AND currency != 'BCH';
SELECT count(*) FROM temporal_tickers WHERE "BUY" is null AND "PREVIOUS_BUY" is null AND currency != 'BCH';
SELECT count(*) FROM temporal_tickers WHERE "SELL" is null AND "PREVIOUS_SELL" is null AND currency != 'BCH';