--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure INSERT_ROW_INTO_PAYMENTS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "INSERT_ROW_INTO_PAYMENTS" 
(
  IRIP_DAY IN DATE 
, IRIP_COMPANY IN NUMBER 
, IRIP_SUM IN NUMBER 
, IRIP_PURPOSE IN NUMBER 
, IRIP_STATUS IN NUMBER 
) AS 
BEGIN
  INSERT INTO Payments
  VALUES(PAYMENTS_SEQ.nextval, IRIP_DAY, IRIP_COMPANY, IRIP_SUM, IRIP_PURPOSE, IRIP_STATUS);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Duplicate value on an index');
END INSERT_ROW_INTO_PAYMENTS;

/
