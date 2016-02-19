--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Trigger COMPANIES_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "COMPANIES_TRG" 
BEFORE INSERT ON COMPANIES 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    NULL;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "COMPANIES_TRG" ENABLE;
