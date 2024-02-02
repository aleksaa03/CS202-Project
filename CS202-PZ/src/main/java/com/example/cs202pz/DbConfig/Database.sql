CREATE DATABASE IF NOT EXISTS CS202PZ;
USE CS202PZ;

CREATE TABLE User
(
    Id        INT PRIMARY KEY AUTO_INCREMENT,
    Firstname VARCHAR(50)  NOT NULL,
    Lastname  VARCHAR(50)  NOT NULL,
    RoleId    SMALLINT     NOT NULL,
    Email     VARCHAR(100) NOT NULL UNIQUE,
    JMBG      VARCHAR(13)  NOT NULL UNIQUE
);

CREATE TABLE Account
(
    Id         INT PRIMARY KEY AUTO_INCREMENT,
    CardNumber VARCHAR(16)    NOT NULL UNIQUE,
    DateExpire DATE           NOT NULL,
    Cvv        VARCHAR(6),
    Amount     DECIMAL(19, 2) NOT NULL,
    Pin        VARCHAR(4)     NOT NULL,
    UserId     INT            NOT NULL,
    CONSTRAINT fk_account_user FOREIGN KEY (UserId) REFERENCES User (Id),
    CONSTRAINT ck_pin_account CHECK (LENGTH(Pin) > 0 AND LENGTH(Pin) <= 4)
);

CREATE TABLE Transaction
(
    Id           INT PRIMARY KEY AUTO_INCREMENT,
    Date         DATETIME       NOT NULL,
    Amount       DECIMAL(19, 2) NOT NULL,
    AmountBefore DECIMAL(19, 2) NOT NULL,
    Status       SMALLINT       NOT NULL,
    AccountId    INT            NOT NULL,
    CONSTRAINT fk_transaction_account FOREIGN KEY (AccountId) REFERENCES Account (Id)
);

CREATE TABLE UserLog
(
    Id      INT PRIMARY KEY AUTO_INCREMENT,
    LogType SMALLINT     NOT NULL,
    Message VARCHAR(100) NOT NULL,
    LogTime DATETIME     DEFAULT NOW() NOT NULL,
    UserId  INT          NOT NULL,
    CONSTRAINT fk_userlog_user FOREIGN KEY (UserId) REFERENCES User (Id)
);

CREATE TABLE ErrorLog
(
    Id        INT PRIMARY KEY AUTO_INCREMENT,
    Message   TEXT,
    ErrorTime DATETIME DEFAULT NOW() NOT NULL
);

INSERT INTO User (Firstname, Lastname, RoleId, Email, JMBG)
VALUES
    ('John', 'Doe', 1, 'john.doe@email.com', '1234567890123'),
    ('Jane', 'Smith', 2, 'jane.smith@email.com', '9876543210987');

INSERT INTO Account (CardNumber, DateExpire, Cvv, Amount, Pin, UserId)
VALUES
    ('1234567812345678', '2025-12-01', '123', 1000.00, '1234', 1),
    ('8765432187654321', '2024-10-01', '456', 500.00, '5678', 2);

INSERT INTO Transaction (Date, Amount, AmountBefore, Status, AccountId)
VALUES
    ('2024-02-01 12:00:00', 50.00, 1000.00, 1, 1),
    ('2024-02-02 14:30:00', 20.00, 500.00, 1, 2);