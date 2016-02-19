--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure GET_TOTAL_STATUS_0
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "GET_TOTAL_STATUS_0" 
(
  GTS0_TOTAL OUT NUMBER
)AS 
BEGIN
  SELECT SUM(SUM)
  INTO GTS0_TOTAL
  FROM Payments 
  WHERE STATUS = 0;
END GET_TOTAL_STATUS_0;

/
