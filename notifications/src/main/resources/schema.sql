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
    account_value INT CHECK (account_value >= 0),
    currency VARCHAR(3),
    CONSTRAINT users_bank_accounts FOREIGN KEY (user_id) REFERENCES accounts.users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO accounts.users(login, password, name, surname, birthdate, role)
    VALUES ('1', '$2a$12$nITs0fz17B8LUqL0uRKJ/e42ifhMuJoYWCDblanBZPHhGk9eUClCO', 'a', 'b', '2000-01-01', 'USER'); --pass = 1
INSERT INTO accounts.bank_accounts(account_number, account_value, user_id, currency) VALUES (111, 250, 1, 'RUB');

--INSERT INTO users(username, password, role) VALUES
    --('user1', '$2a$12$o9FYCapKCbubG3r5mHtTA.dWWr0xsIJPku3y4NXWywaUpW.DRrIx.', 'USER'), --pass = pass1
    --('user2', '$2a$12$TjUVWFCg/9OOsgVKoCAba.wg.8xGrmGfowL.p8S.U.17DtaNOJfPa', 'USER'), --pass = pass2
    --('1', '$2a$12$tjr4cxHzoJ/pdOXPPyFgGuKlnILawpnKWO/7yKKPkNxNRgUuJ7s1y', 'USER'), --pass = 1
    --('2', '$2a$12$poSSKtrQ7yWlvX2LuTUsL.rIbxHDm6GuyVP8BhUBjGeZ35uYouloa', 'USER'), --pass = 2
    --('admin', '$$2a$12$wC5ziXwxwZyXFERx0QEh7OmwHJUw4HbH1.c/IDEGjmoyMJCFP7dpC', 'ADMIN'); --pass = admin

