--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure UPDATE_NAME_IN_COMPANIES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UPDATE_NAME_IN_COMPANIES" 
(
  UNIC_NAME IN VARCHAR2 
, UNIC_COMPANY IN NUMBER 
) AS 
BEGIN
  UPDATE COMPANIES
  SET NAME = UNIC_NAME
  WHERE COMPANY = UNIC_COMPANY;
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Duplicate value on an index');
END UPDATE_NAME_IN_COMPANIES;

/
