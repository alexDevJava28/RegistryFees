--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure DELETE_COMPANY_FROM_COMPANIES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "DELETE_COMPANY_FROM_COMPANIES" 
(
  DCFC_NAME IN VARCHAR2 
) AS 
BEGIN
  DELETE FROM COMPANIES
  WHERE NAME = DCFC_NAME;
END DELETE_COMPANY_FROM_COMPANIES;

/
