CREATE TABLE complaints (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id   NUMBER                 NOT NULL,
    agent_id      NUMBER,
    category_id   NUMBER                 NOT NULL,
    title         VARCHAR2(150)          NOT NULL,
    description   CLOB                   NOT NULL,
    status        VARCHAR2(20)           NOT NULL,
    created_at    TIMESTAMP              DEFAULT SYSTIMESTAMP NOT NULL,
    updated_at    TIMESTAMP              DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT    chk_complaint_status   CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT    fk_complaints_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT    fk_complaints_agent    FOREIGN KEY (agent_id)    REFERENCES users(id),
    CONSTRAINT    fk_complaints_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
