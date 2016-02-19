--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure DELETE_TEXT_FROM_PURPOSES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "DELETE_TEXT_FROM_PURPOSES" 
(
  DTFP_TEXT IN VARCHAR2 
) AS 
BEGIN
  DELETE FROM PURPOSES
  WHERE TEXT = DTFP_TEXT;
END DELETE_TEXT_FROM_PURPOSES;

/
