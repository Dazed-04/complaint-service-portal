CREATE OR REPLACE TRIGGER trg_complaints_set_updated_at
BEFORE UPDATE ON complaints
FOR EACH ROW
BEGIN
    :NEW.updated_at := SYSTIMESTAMP;
END;
/
