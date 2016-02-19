--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure UPDATE_STATUS_TO_0
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UPDATE_STATUS_TO_0" 
(
  US_DAY IN DATE 
, US_COMPANY IN NUMBER 
, US_SUM IN NUMBER 
, US_PURPOSE IN NUMBER 
) AS 
BEGIN
  UPDATE Payments 
  SET STATUS = 0 
  WHERE DAY = US_DAY
  AND COMPANY = US_COMPANY
  AND SUM = US_SUM
  AND PURPOSE = US_PURPOSE;
END UPDATE_STATUS_TO_0;

/
