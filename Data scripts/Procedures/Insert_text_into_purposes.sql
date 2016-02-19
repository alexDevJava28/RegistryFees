--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure INSERT_TEXT_INTO_PURPOSES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "INSERT_TEXT_INTO_PURPOSES" 
(
  ITIP_TEXT IN VARCHAR2 
) AS 
BEGIN
  INSERT INTO PURPOSES(PURPOSE, TEXT)
  VALUES (PURPOSES_SEQ.nextval, ITIP_TEXT);
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Duplicate value on an index');
END INSERT_TEXT_INTO_PURPOSES;

/
