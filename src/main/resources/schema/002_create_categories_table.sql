CREATE TABLE categories (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR2(100)  NOT NULL UNIQUE,
    description   VARCHAR2(150)
);
