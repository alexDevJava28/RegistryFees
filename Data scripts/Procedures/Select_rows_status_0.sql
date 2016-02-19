--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Procedure SELECT_ROWS_STATUS_0
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "SELECT_ROWS_STATUS_0" 
(
  SRS0_STATUS IN NUMBER, 
  MYCURS OUT SYS_REFCURSOR
) AS
BEGIN
OPEN MYCURS FOR
  SELECT c.NAME, p.SUM, pu.TEXT
  FROM Payments p
  LEFT JOIN Companies c ON c.COMPANY = p.COMPANY
  LEFT JOIN Purposes pu ON pu.PURPOSE = p.PURPOSE
  WHERE p.STATUS = SRS0_STATUS;
END SELECT_ROWS_STATUS_0;

/
