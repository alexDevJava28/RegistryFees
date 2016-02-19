--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure UPDATE_DAY_IN_PAYMENTS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UPDATE_DAY_IN_PAYMENTS" 
(
  UDIP_DAY IN DATE 
) AS 
BEGIN
  UPDATE PAYMENTS
  SET DAY = UDIP_DAY
  WHERE STATUS = 0;
END UPDATE_DAY_IN_PAYMENTS;

/
