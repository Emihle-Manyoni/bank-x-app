
INSERT INTO Customer (FirstName, LastName, IDNumber, EmailAddress)
VALUES
('John', 'Doe', '0212360674099', 'john.doe@email.com'),
('Jane', 'Smith', '1245327890967', 'jane.smith@email.com');


INSERT INTO Accounts (CustomerID, AccountType, InterestRate, AccountNumber, Balance, CreditedAmount)
VALUES
(1, 'Savings', 5.00, 100001, 5000.00, 1000.00),
(2, 'Checking', 3.50, 100002, 3000.00, 500.00);

INSERT INTO ExternalBank (BankName, BankCode)
VALUES
('Bank Z', '789');

INSERT INTO Transactions (AccountID, ExternalBankID, TransactionAmount, TransactionDate, TransactionCharge, TransactionType, isExternal)
VALUES
(1, 1, 1500.00, '2025-03-10 09:00:00', 15.00, 'Credit', TRUE);

INSERT INTO Transactions (AccountID, ExternalBankID, TransactionAmount, TransactionDate, TransactionCharge, TransactionType, isExternal)
VALUES
(2, NULL, 500.00, '2025-03-10 10:30:00', 5.00, 'Debit', FALSE);


INSERT INTO Reconciliation (TransactionAmount, TransactionDate, TransactionCharge, TransactionType, Status, ReconciliationStatus, BankName, BankCode)
VALUES
(1500.00, '2025-03-10 09:00:00', 15.00, 'Credit', 'Completed', 'Matched', 'Bank Z', '789'),
(500.00, '2025-03-10 10:30:00', 5.00, 'Debit', 'Pending', 'Unmatched', 'Bank X', '456');
