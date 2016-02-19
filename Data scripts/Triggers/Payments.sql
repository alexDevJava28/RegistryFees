--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Trigger PAYMENTS_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PAYMENTS_TRG" 
BEFORE INSERT ON PAYMENTS 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    NULL;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "PAYMENTS_TRG" ENABLE;
