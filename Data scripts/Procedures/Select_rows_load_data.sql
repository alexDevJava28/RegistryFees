--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SELECT_ROWS_LOAD_DATA
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SELECT_ROWS_LOAD_DATA" 
(
  SRLD_DAY_FROM IN DATE 
, SRLD_DAY_TO IN DATE 
, SRLD_COMPANY IN NUMBER 
, SRLD_PURPOSE IN NUMBER 
, SRLD_CURS OUT SYS_REFCURSOR 
) AS 
BEGIN
  OPEN SRLD_CURS FOR
    SELECT p.DAY, c.NAME, p.SUM, pu.TEXT, p.STATUS
    FROM Payments p
    LEFT JOIN Companies c ON c.COMPANY = p.COMPANY
    LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE
    WHERE p.DAY >= SRLD_DAY_FROM
    AND p.DAY <= SRLD_DAY_TO
    AND SRLD_COMPANY IS NULL OR p.COMPANY = SRLD_COMPANY
    AND SRLD_PURPOSE IS NULL OR p.PURPOSE = SRLD_PURPOSE
    ORDER BY p.DAY;
END SELECT_ROWS_LOAD_DATA;

/
