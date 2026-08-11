CREATE TABLE status_history (
    id            NUMBER            GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    complaint_id  NUMBER            NOT NULL,
    old_status    VARCHAR2(20)      NOT NULL,
    new_status    VARCHAR2(20)      NOT NULL,
    changed_by    NUMBER            NOT NULL,
    remark        VARCHAR2(500),
    changed_at    TIMESTAMP         DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT    chk_old_status    CHECK (old_status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT    chk_new_status    CHECK (new_status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT    fk_statusHistory_complaints FOREIGN KEY (complaint_id) REFERENCES complaints(id),
    CONSTRAINT    fk_statusHistory_users      FOREIGN KEY (changed_by)   REFERENCES users(id)
);
