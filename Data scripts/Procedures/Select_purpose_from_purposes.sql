--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SELECT_PURPOSE_FROM_PURPOSES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SELECT_PURPOSE_FROM_PURPOSES" 
(
  SPFP_TEXT IN VARCHAR2 
, SPFP_PURPOSE OUT NUMBER 
) AS 
BEGIN
  SELECT PURPOSE
  INTO SPFP_PURPOSE
  FROM PURPOSES
  WHERE SPFP_TEXT IS NULL OR TEXT = SPFP_TEXT;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        SPFP_PURPOSE := 0;
END SELECT_PURPOSE_FROM_PURPOSES;

/
