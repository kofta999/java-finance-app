# Finance Manager CLI

A simple, modern command-line application for parsing and analyzing personal finance data from a CSV file. This tool is built with Java and Maven, showcasing modern Java features in a clean, structured project.

## Features

- **View Remaining Balance:** Calculates the sum of all transactions to show your final balance.
- **Spending Summary:** Groups all transactions by category and displays the total amount for each.
- **Filter by Category:** Interactively prompts for a category and lists all associated transactions.

## Requirements

- [Java 21+](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven](https://maven.apache.org/download.cgi)

## How to Build and Run

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    cd finance-app
    ```

2.  **Build the project:**
    This command will compile the source code, run tests, and package the application into a `.jar` file in the `target/` directory.
    ```sh
    mvn clean install
    ```

3.  **Run the application:**
    Execute the packaged JAR file to start the interactive console.
    ```sh
    java -jar target/finance-app-1.0-SNAPSHOT.jar
    ```

## Input Data Format

The application reads transaction data from a CSV file located at `src/main/resources/input.csv`.

The CSV file **must** have a header row and the following columns in order:

- `date`: The date of the transaction in `YYYY-MM-DD` format.
- `description`: A brief text description of the transaction.
- `amount`: The transaction amount. Positive values are income, negative values are expenses.
- `category`: The category of the transaction.

### Example `input.csv`

```csv
date,description,amount,category
2023-10-01,Monthly Salary,3000.0,SALARY
2023-10-02,Groceries,-75.5,FOOD
2023-10-03,New Jacket,-120.0,SHOPPING
2023-10-05,Rent Payment,-1200.0,RENT
2023-10-10,Pharmacy,-25.0,HEALTH
```

### Valid Categories

The `category` column must contain one of the following (case-insensitive):

- `SALARY`
- `FOOD`
- `RENT`
- `SHOPPING`
- `HEALTH`

## Usage

Once the application is running, you will see an interactive menu. Simply type the number corresponding to your choice and press Enter.

```
--- Finance Manager ---
1. View Remaining Balance
2. Show Summary (by category)
3. Filter by category
4. Exit

Select a choice: 1
Remaining Balance:    1579.50 $

--- Finance Manager ---
1. View Remaining Balance
2. Show Summary (by category)
3. Filter by category
4. Exit

Select a choice: 3
Category: food
2023-10-02   | Groceries                      |     -75.50 | FOOD

--- Finance Manager ---
1. View Remaining Balance
2. Show Summary (by category)
3. Filter by category
4. Exit

Select a choice: 4
Exiting...
```
