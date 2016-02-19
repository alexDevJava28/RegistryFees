--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure INSERT_NAME_INTO_COMPANIES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "INSERT_NAME_INTO_COMPANIES" 
(
  INIC_NAME IN VARCHAR2 
) AS 
BEGIN
  INSERT INTO COMPANIES(COMPANY, NAME)
  VALUES (COMPANIES_SEQ.nextval, INIC_NAME);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Duplicate value on an index');
END INSERT_NAME_INTO_COMPANIES;

/
