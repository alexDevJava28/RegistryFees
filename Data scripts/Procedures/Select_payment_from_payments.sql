--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SELECT_PAYMENT_FROM_PAYMENTS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SELECT_PAYMENT_FROM_PAYMENTS" 
(
  SPFP_DAY IN DATE 
, SPFP_COMPANY IN NUMBER 
, SPFP_SUM IN NUMBER 
, SPFP_PURPOSE IN NUMBER 
, SPFP_PAYMENT OUT NUMBER 
) AS 
BEGIN
  SELECT PAYMENT
  INTO SPFP_PAYMENT
  FROM Payments 
  WHERE STATUS = 0
  AND DAY = SPFP_DAY
  AND COMPANY = SPFP_COMPANY
  AND SUM = SPFP_SUM
  AND PURPOSE = SPFP_PURPOSE;
END SELECT_PAYMENT_FROM_PAYMENTS;

/
