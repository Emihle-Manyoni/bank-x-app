CREATE TABLE Customer (
    CustomerID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    IDNumber VARCHAR(13) UNIQUE,
    EmailAddress VARCHAR(255) UNIQUE
);

CREATE TABLE Accounts (
    AccountID INT PRIMARY KEY AUTO_INCREMENT,
    CustomerID INT,
    AccountType VARCHAR(50),
    InterestRate DECIMAL(5,2),
    AccountNumber BIGINT UNIQUE,
    Balance DECIMAL(15,2),
    CreditedAmount DECIMAL(15,2),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

CREATE TABLE ExternalBank (
    ExternalBankID INT PRIMARY KEY AUTO_INCREMENT,
    BankName VARCHAR(255),
    BankCode VARCHAR(10) UNIQUE
);

CREATE TABLE Transactions (
    TransactionID INT PRIMARY KEY AUTO_INCREMENT,
    AccountID INT,
    ExternalBankID INT NULL,
    TransactionAmount DECIMAL(15,2),
    TransactionDate DATETIME,
    TransactionCharge DECIMAL(15,2),
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TransactionType VARCHAR(50),
    isExternal BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID),
    FOREIGN KEY (ExternalBankID) REFERENCES ExternalBank(ExternalBankID)
);



CREATE TABLE Reconciliation (
    ReconciliationID INT PRIMARY KEY AUTO_INCREMENT,
    TransactionAmount DECIMAL(15,2),
    TransactionDate DATETIME,
    TransactionCharge DECIMAL(15,2),
    CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    TransactionType VARCHAR(50),
    Status VARCHAR(50),
    ReconciliationStatus VARCHAR(50),
    BankName VARCHAR(255),
    BankCode VARCHAR(10)
);
