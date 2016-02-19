--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure GET_TOTAL_FOR_LOAD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "GET_TOTAL_FOR_LOAD" 
(
  GTFL_DAY_FROM IN DATE 
, GTFL_DAY_TO IN DATE
, GTFL_COMPANY IN NUMBER
, GTFL_PURPOSE IN NUMBER
, GTFL_TOTAL OUT NUMBER 
) AS 
BEGIN
  SELECT SUM(SUM)
  INTO GTFL_TOTAL
  FROM Payments 
  WHERE DAY >= GTFL_DAY_FROM
  AND DAY <= GTFL_DAY_TO
  AND GTFL_COMPANY IS NULL OR COMPANY = GTFL_COMPANY
  AND GTFL_PURPOSE IS NULL OR PURPOSE = GTFL_PURPOSE;
END GET_TOTAL_FOR_LOAD;

/
