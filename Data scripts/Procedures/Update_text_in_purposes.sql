--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure UPDATE_TEXT_IN_PURPOSES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "UPDATE_TEXT_IN_PURPOSES" 
(
  UTIP_TEXT IN VARCHAR2 
, UTIP_PURPOSE IN NUMBER 
) AS 
BEGIN
  UPDATE PURPOSES
  SET TEXT = UTIP_TEXT
  WHERE PURPOSE = UTIP_PURPOSE;
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN
        DBMS_OUTPUT.PUT_LINE('Duplicate value on an index');
END UPDATE_TEXT_IN_PURPOSES;

/
