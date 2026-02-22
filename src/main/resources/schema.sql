CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

--

CREATE TABLE IF NOT EXISTS accounts (
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    currency TEXT NOT NULL,
    user_id TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--

CREATE TABLE IF NOT EXISTS transactions (
    id TEXT PRIMARY KEY,
    date TEXT NOT NULL,
    description TEXT NOT NULL,
    amount REAL NOT NULL,
    category TEXT NOT NULL,
    account_id TEXT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);
