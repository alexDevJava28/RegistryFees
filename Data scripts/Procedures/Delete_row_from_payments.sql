--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure DELETE_ROW_FROM_PAYMENTS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "DELETE_ROW_FROM_PAYMENTS" 
(
  DRFP_DAY IN DATE 
, DRFP_COMPANY IN NUMBER 
, DRFP_SUM IN NUMBER 
, DRFP_PURPOSE IN NUMBER 
) AS 
BEGIN
  DELETE FROM Payments 
  WHERE STATUS = 0
  AND DAY = DRFP_DAY
  AND COMPANY = DRFP_COMPANY
  AND SUM = DRFP_SUM
  AND PURPOSE = DRFP_PURPOSE;
END DELETE_ROW_FROM_PAYMENTS;

/
