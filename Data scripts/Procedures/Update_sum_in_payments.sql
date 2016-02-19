--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure UPDATE_SUM_IN_PAYMENTS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UPDATE_SUM_IN_PAYMENTS" 
(
  USIP_PAYMENT IN NUMBER 
, USIP_SUM IN NUMBER 
) AS 
BEGIN
  UPDATE PAYMENTS
  SET SUM = USIP_SUM
  WHERE PAYMENT = USIP_PAYMENT;
END UPDATE_SUM_IN_PAYMENTS;

/
