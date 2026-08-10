CREATE TABLE users (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR2(100)  NOT NULL,
    email         VARCHAR2(150)  NOT NULL UNIQUE,
    password_hash VARCHAR2(255)  NOT NULL,
    role          VARCHAR2(20)   NOT NULL,
    created_at    TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT    chk_users_role CHECK (role IN ('CUSTOMER', 'ADMIN', 'AGENT'))
);
