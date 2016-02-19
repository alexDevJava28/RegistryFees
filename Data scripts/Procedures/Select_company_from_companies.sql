--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SELECT_COMPANY_FROM_COMPANIES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SELECT_COMPANY_FROM_COMPANIES" 
(
  SCFC_NAME IN VARCHAR2 
, SCFC_COMPANY OUT NUMBER 
) AS 
BEGIN
  SELECT COMPANY
  INTO SCFC_COMPANY
  FROM COMPANIES
  WHERE NAME = SCFC_NAME;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        SCFC_COMPANY := 0;
END SELECT_COMPANY_FROM_COMPANIES;

/
