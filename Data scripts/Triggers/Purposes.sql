--------------------------------------------------------
--  File created - пятница-февраля-19-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Trigger PURPOSES_TRG
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PURPOSES_TRG" 
BEFORE INSERT ON PURPOSES 
FOR EACH ROW 
BEGIN
  <<COLUMN_SEQUENCES>>
  BEGIN
    IF INSERTING AND :NEW.TEXT IS NULL THEN
      SELECT PURPOSES_SEQ.NEXTVAL INTO :NEW.TEXT FROM SYS.DUAL;
    END IF;
  END COLUMN_SEQUENCES;
END;
/
ALTER TRIGGER "PURPOSES_TRG" ENABLE;
