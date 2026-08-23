CREATE TABLE attachments (
    id            NUMBER                      GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    complaint_id  NUMBER                      NOT NULL,
    filename      VARCHAR2(100)               NOT NULL,
    content_type  VARCHAR2(100)               NOT NULL,
    file_data     BLOB                        NOT NULL,
    uploaded_at   TIMESTAMP                   DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT    fk_attachments_complaints   FOREIGN KEY (complaint_id) REFERENCES complaints(id)
);
