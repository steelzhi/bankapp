DROP TABLE IF EXISTS accounts.bank_accounts, accounts.users CASCADE;
DROP SCHEMA IF EXISTS accounts CASCADE;
--DROP TYPE IF EXISTS roles CASCADE;

CREATE SCHEMA IF NOT EXISTS accounts;

--CREATE TYPE roles AS ENUM ('USER', 'ADMIN');

CREATE TABLE IF NOT EXISTS accounts.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(20) NOT NULL,
    birthdate DATE,
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS accounts.bank_accounts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id INT,
    account_number VARCHAR(20),
    account_value NUMERIC(10, 2) CHECK (account_value >= 0),
    currency VARCHAR(3),
    CONSTRAINT users_bank_accounts FOREIGN KEY (user_id) REFERENCES accounts.users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO accounts.users(login, password, name, surname, birthdate, role) VALUES
     ('1', '$2a$12$nITs0fz17B8LUqL0uRKJ/e42ifhMuJoYWCDblanBZPHhGk9eUClCO', 'a', 'b', '2000-01-01', 'USER'), --pass = 1
     ('2', '$2a$12$JigV3.EF/NjoRdRIiMSN2uFl7Sg3ye1k8WqVBf14DTtMGBfwGY.W2', 'с', 'в', '2002-03-03', 'USER'), --pass = 2
     ('3', '$2a$12$9giRaG67CXZys9znLapxieiBFa.7eLOA/8jPSBCOoOAmKIkBV8Z/O', 'kk', 'kk', '1992-05-01', 'USER'); --pass = 3

INSERT INTO accounts.bank_accounts(account_number, account_value, user_id, currency) VALUES
    (4071391041, 250, 1, 'RUB'),
    (3651441200, 333, 1, 'USD'),
    (7410200021, 500, 2, 'CNY'),
    (7410400125, 1300, 3, 'CNY'),
    (3651200021, 80, 3, 'USD');
