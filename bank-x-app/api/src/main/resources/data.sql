
INSERT INTO Customer (FirstName, LastName, IDNumber, EmailAddress)
VALUES
('John', 'Doe', '9001011234567', 'john.doe@example.com'),
('Alice', 'Smith', '8512129876543', 'alice.smith@example.com');

INSERT INTO Accounts (CustomerID, AccountType, InterestRate, AccountNumber, Balance, CreditedAmount)
VALUES
(1, 'Savings', 0.5, 1000010001, 200.00, 500.00),
(1, 'Cheque', 0.0, 1000010002, 600.00, 0.00),
(2, 'Savings', 0.5, 1000010003, 0.00, 500.00),
(2, 'Cheque', 0.0, 1000010004, 100.00, 0.00);


INSERT INTO ExternalBank (BankName, BankCode)
VALUES
('Bank Z', 'BZ999');

INSERT INTO Transactions (AccountID, ExternalBankID, TransactionAmount, TransactionDate, TransactionCharge, TransactionType, isExternal)
VALUES
(1, 1, 1000.00, '2025-03-10 08:30:00', 15.00, 'Transfer', TRUE),
(2, 1, 500.00, '2025-03-11 14:20:00', 10.00, 'Payment', TRUE),
(3, 1, 1200.00, '2025-03-12 09:45:00', 20.00, 'Transfer', TRUE),
(4, 1, 700.00, '2025-03-13 16:10:00', 12.50, 'Payment', TRUE);

INSERT INTO Reconciliation (TransactionAmount, TransactionDate, TransactionCharge, TransactionType, Status, ReconciliationStatus, BankName, BankCode)
VALUES
(1000.00, '2025-03-10 08:30:00', 15.00, 'Transfer', 'Completed', 'Matched', 'Bank Z', 'BZ999'),
(500.00, '2025-03-11 14:20:00', 10.00, 'Payment', 'Pending', 'Unmatched', 'Bank Z', 'BZ999'),
(1200.00, '2025-03-12 09:45:00', 20.00, 'Transfer', 'Completed', 'Matched', 'Bank Z', 'BZ999'),
(700.00, '2025-03-13 16:10:00', 12.50, 'Payment', 'Failed', 'Unmatched', 'Bank Z', 'BZ999');
