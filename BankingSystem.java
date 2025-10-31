import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// ================== CUSTOM EXCEPTIONS ==================
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

class DailyLimitExceededException extends Exception {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}

class InvalidAmountException extends Exception {
    public InvalidAmountException(String message) {
        super(message);
    }
}

// ================== TRANSACTION CLASS (COMPOSITION) ==================
class Transaction {
    private String transactionId;
    private String type;
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    
    public Transaction(String type, double amount, double balanceAfter) {
        this.transactionId = generateTransactionId();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("%-18s | %-15s | Rs. %10.2f | Balance: Rs. %10.2f | %s",
            transactionId, type, amount, balanceAfter, timestamp.format(formatter));
    }
}

// ================== PRINTABLE INTERFACE ==================
interface Printable {
    void printPassbook(LocalDateTime fromDate, LocalDateTime toDate);
    void printMiniStatement();
}

// ================== ABSTRACT ACCOUNT CLASS ==================
abstract class Account implements Printable {
    // Static members
    private static int accountCounter = 10001;
    protected static final double MIN_BALANCE = 1000.0;
    
    // Instance variables (Encapsulation - private fields)
    private String accountNumber;
    private String accountHolderName;
    private String phoneNumber;
    private String email;
    private String accountType;
    private double balance;
    private LocalDate accountOpenDate;
    private List<Transaction> transactions;
    private double dailyWithdrawalLimit;
    private double todayWithdrawnAmount;
    private LocalDate lastWithdrawalDate;
    private boolean isActive;
    
    // Constructor
    public Account(String accountHolderName, String phoneNumber, String email, 
                   String accountType, double initialDeposit, double dailyLimit) {
        this.accountNumber = generateAccountNumber();
        this.accountHolderName = accountHolderName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.accountOpenDate = LocalDate.now();
        this.transactions = new ArrayList<>();
        this.dailyWithdrawalLimit = dailyLimit;
        this.todayWithdrawnAmount = 0.0;
        this.lastWithdrawalDate = LocalDate.now();
        this.isActive = true;
        
        // Add initial deposit transaction
        transactions.add(new Transaction("OPENING_DEPOSIT", initialDeposit, balance));
    }
    
    // Static synchronized method for account number generation
    private static synchronized String generateAccountNumber() {
        return "ACC" + String.format("%08d", accountCounter++);
    }
    
    // Abstract methods (must be implemented by subclasses)
    public abstract double calculateInterest();
    public abstract void applyMonthlyCharges();
    public abstract String getAccountFeatures();
    
    // Deposit method
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero!");
        }
        if (!isActive) {
            System.out.println("❌ Account is inactive! Cannot perform transaction.");
            return;
        }
        
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance));
        System.out.println("✓ Rs. " + String.format("%.2f", amount) + " deposited successfully!");
        System.out.println("  New Balance: Rs. " + String.format("%.2f", balance));
    }
    
    // Withdraw method with daily limit check
    public void withdraw(double amount) throws InsufficientBalanceException, 
                                               DailyLimitExceededException, 
                                               InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero!");
        }
        
        if (!isActive) {
            System.out.println("❌ Account is inactive! Cannot perform transaction.");
            return;
        }
        
        // Reset daily withdrawal if new day
        if (!lastWithdrawalDate.equals(LocalDate.now())) {
            todayWithdrawnAmount = 0.0;
            lastWithdrawalDate = LocalDate.now();
        }
        
        // Check daily limit
        if (todayWithdrawnAmount + amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                "Daily withdrawal limit exceeded!\n" +
                "  Daily Limit: Rs. " + String.format("%.2f", dailyWithdrawalLimit) + "\n" +
                "  Already Withdrawn Today: Rs. " + String.format("%.2f", todayWithdrawnAmount) + "\n" +
                "  Available Today: Rs. " + String.format("%.2f", dailyWithdrawalLimit - todayWithdrawnAmount)
            );
        }
        
        // Check minimum balance
        if (balance - amount < MIN_BALANCE) {
            throw new InsufficientBalanceException(
                "Insufficient balance!\n" +
                "  Current Balance: Rs. " + String.format("%.2f", balance) + "\n" +
                "  Minimum Balance Required: Rs. " + String.format("%.2f", MIN_BALANCE) + "\n" +
                "  Maximum Withdrawal: Rs. " + String.format("%.2f", balance - MIN_BALANCE)
            );
        }
        
        balance -= amount;
        todayWithdrawnAmount += amount;
        transactions.add(new Transaction("WITHDRAWAL", amount, balance));
        System.out.println("✓ Rs. " + String.format("%.2f", amount) + " withdrawn successfully!");
        System.out.println("  New Balance: Rs. " + String.format("%.2f", balance));
        System.out.println("  Remaining Daily Limit: Rs. " + 
                         String.format("%.2f", dailyWithdrawalLimit - todayWithdrawnAmount));
    }
    
    // Check balance
    public double checkBalance() {
        return balance;
    }
    
    // Display account information (Polymorphism - can be overridden)
    public void displayAccountInfo() {
        System.out.println("\n" + "═".repeat(80));
        System.out.println("                         ACCOUNT INFORMATION");
        System.out.println("═".repeat(80));
        System.out.println("Account Number        : " + accountNumber);
        System.out.println("Account Holder Name   : " + accountHolderName);
        System.out.println("Phone Number          : " + phoneNumber);
        System.out.println("Email                 : " + email);
        System.out.println("Account Type          : " + accountType);
        System.out.println("Account Status        : " + (isActive ? "Active ✓" : "Inactive ✗"));
        System.out.println("Current Balance       : Rs. " + String.format("%.2f", balance));
        System.out.println("Account Open Date     : " + accountOpenDate);
        System.out.println("Daily Withdrawal Limit: Rs. " + String.format("%.2f", dailyWithdrawalLimit));
        System.out.println("Today Withdrawn       : Rs. " + String.format("%.2f", todayWithdrawnAmount));
        System.out.println("Total Transactions    : " + transactions.size());
        System.out.println("\n" + getAccountFeatures());
        System.out.println("═".repeat(80) + "\n");
    }
    
    // Passbook implementation (Interface method)
    @Override
    public void printPassbook(LocalDateTime fromDate, LocalDateTime toDate) {
        System.out.println("\n" + "═".repeat(110));
        System.out.println("                                      PASSBOOK STATEMENT");
        System.out.println("═".repeat(110));
        System.out.println("Account: " + accountNumber + " | Holder: " + accountHolderName + " | Type: " + accountType);
        System.out.println("Period: " + fromDate.toLocalDate() + " to " + toDate.toLocalDate());
        System.out.println("═".repeat(110));
        System.out.println(String.format("%-18s | %-15s | %-14s | %-20s | %s", 
            "Transaction ID", "Type", "Amount", "Balance", "Date & Time"));
        System.out.println("─".repeat(110));
        
        boolean found = false;
        for (Transaction txn : transactions) {
            if (!txn.getTimestamp().isBefore(fromDate) && !txn.getTimestamp().isAfter(toDate)) {
                System.out.println(txn);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("                             No transactions found in the specified period.");
        }
        
        System.out.println("═".repeat(110));
        System.out.println("Current Balance: Rs. " + String.format("%.2f", balance));
        System.out.println("═".repeat(110) + "\n");
    }
    
    // Mini statement - last 5 transactions
    @Override
    public void printMiniStatement() {
        System.out.println("\n" + "═".repeat(110));
        System.out.println("                                    MINI STATEMENT (Last 5 Transactions)");
        System.out.println("═".repeat(110));
        System.out.println("Account: " + accountNumber + " | Holder: " + accountHolderName);
        System.out.println("═".repeat(110));
        System.out.println(String.format("%-18s | %-15s | %-14s | %-20s | %s", 
            "Transaction ID", "Type", "Amount", "Balance", "Date & Time"));
        System.out.println("─".repeat(110));
        
        int size = transactions.size();
        int start = Math.max(0, size - 5);
        
        if (size == 0) {
            System.out.println("                                  No transactions available.");
        } else {
            for (int i = start; i < size; i++) {
                System.out.println(transactions.get(i));
            }
        }
        
        System.out.println("═".repeat(110));
        System.out.println("Current Balance: Rs. " + String.format("%.2f", balance));
        System.out.println("═".repeat(110) + "\n");
    }
    
    // Transfer money to another account
    public void transferMoney(Account targetAccount, double amount) 
            throws InsufficientBalanceException, DailyLimitExceededException, InvalidAmountException {
        System.out.println("\n💸 Initiating transfer...");
        
        // Withdraw from this account
        this.withdraw(amount);
        
        // Deposit to target account
        try {
            targetAccount.deposit(amount);
            transactions.add(new Transaction("TRANSFER_OUT_TO_" + targetAccount.getAccountNumber(), 
                                           amount, balance));
            targetAccount.getTransactions().add(
                new Transaction("TRANSFER_IN_FROM_" + this.accountNumber, amount, targetAccount.checkBalance()));
            System.out.println("✓ Transfer successful!");
            System.out.println("  Rs. " + String.format("%.2f", amount) + " transferred to " + 
                             targetAccount.getAccountNumber());
        } catch (InvalidAmountException e) {
            // Revert the withdrawal
            balance += amount;
            System.out.println("❌ Transfer failed! Amount reverted.");
        }
    }
    
    // Getters (Encapsulation)
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getAccountType() { return accountType; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    
    // Protected methods for subclasses
    protected void setBalance(double balance) { this.balance = balance; }
    protected List<Transaction> getTransactions() { return transactions; }
    protected double getDailyWithdrawalLimit() { return dailyWithdrawalLimit; }
}

// ================== SAVINGS ACCOUNT (INHERITANCE) ==================
class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 4.5; // 4.5% per annum
    private static final double DAILY_WITHDRAWAL_LIMIT = 50000.0;
    private static final double FREE_TRANSACTIONS = 5;
    private int monthlyTransactionCount;
    
    public SavingsAccount(String name, String phone, String email, double initialDeposit) {
        super(name, phone, email, "SAVINGS", initialDeposit, DAILY_WITHDRAWAL_LIMIT);
        this.monthlyTransactionCount = 1; // Opening deposit counts as 1
    }
    
    // Polymorphism - Method Overriding
    @Override
    public double calculateInterest() {
        double interest = (checkBalance() * INTEREST_RATE) / (100 * 12); // Monthly interest
        return interest;
    }
    
    @Override
    public void applyMonthlyCharges() {
        if (monthlyTransactionCount > FREE_TRANSACTIONS) {
            double charges = (monthlyTransactionCount - FREE_TRANSACTIONS) * 10.0;
            if (checkBalance() >= charges) {
                setBalance(checkBalance() - charges);
                getTransactions().add(new Transaction("TRANSACTION_CHARGES", charges, checkBalance()));
                System.out.println("ℹ Monthly transaction charges applied: Rs. " + charges);
            }
        }
        monthlyTransactionCount = 0; // Reset for next month
    }
    
    @Override
    public String getAccountFeatures() {
        return "Account Features:\n" +
               "  • Interest Rate: " + INTEREST_RATE + "% per annum\n" +
               "  • Free Transactions: " + (int)FREE_TRANSACTIONS + " per month\n" +
               "  • Transaction Charge: Rs. 10 per transaction after free limit\n" +
               "  • Daily Withdrawal Limit: Rs. " + String.format("%.2f", DAILY_WITHDRAWAL_LIMIT);
    }
    
    public void creditInterest() {
        double interest = calculateInterest();
        setBalance(checkBalance() + interest);
        getTransactions().add(new Transaction("INTEREST_CREDIT", interest, checkBalance()));
        System.out.println("✓ Interest of Rs. " + String.format("%.2f", interest) + 
                         " credited @ " + INTEREST_RATE + "% per annum!");
    }
    
    // Method Overloading
    @Override
    public void deposit(double amount) throws InvalidAmountException {
        super.deposit(amount);
        monthlyTransactionCount++;
    }
    
    @Override
    public void withdraw(double amount) throws InsufficientBalanceException, 
                                                DailyLimitExceededException, 
                                                InvalidAmountException {
        super.withdraw(amount);
        monthlyTransactionCount++;
    }
}

// ================== CURRENT ACCOUNT (INHERITANCE) ==================
class CurrentAccount extends Account {
    private static final double MONTHLY_MAINTENANCE = 500.0;
    private static final double DAILY_WITHDRAWAL_LIMIT = 200000.0;
    private static final double OVERDRAFT_LIMIT = 50000.0;
    private double overdraftUsed;
    
    public CurrentAccount(String name, String phone, String email, double initialDeposit) {
        super(name, phone, email, "CURRENT", initialDeposit, DAILY_WITHDRAWAL_LIMIT);
        this.overdraftUsed = 0.0;
    }
    
    @Override
    public double calculateInterest() {
        return 0.0; // Current accounts don't earn interest
    }
    
    @Override
    public void applyMonthlyCharges() {
        if (checkBalance() >= MONTHLY_MAINTENANCE) {
            setBalance(checkBalance() - MONTHLY_MAINTENANCE);
            getTransactions().add(new Transaction("MONTHLY_MAINTENANCE", MONTHLY_MAINTENANCE, checkBalance()));
            System.out.println("ℹ Monthly maintenance charge applied: Rs. " + MONTHLY_MAINTENANCE);
        } else {
            System.out.println("⚠ Warning: Insufficient balance for monthly maintenance charge!");
        }
    }
    
    @Override
    public String getAccountFeatures() {
        return "Account Features:\n" +
               "  • No Interest Earnings\n" +
               "  • Monthly Maintenance: Rs. " + MONTHLY_MAINTENANCE + "\n" +
               "  • Overdraft Facility: Rs. " + String.format("%.2f", OVERDRAFT_LIMIT) + "\n" +
               "  • Unlimited Transactions\n" +
               "  • Daily Withdrawal Limit: Rs. " + String.format("%.2f", DAILY_WITHDRAWAL_LIMIT);
    }
    
    // Additional method specific to Current Account
    public void useOverdraft(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Overdraft amount must be greater than zero!");
        }
        
        if (overdraftUsed + amount > OVERDRAFT_LIMIT) {
            System.out.println("❌ Overdraft limit exceeded! Available: Rs. " + 
                             String.format("%.2f", OVERDRAFT_LIMIT - overdraftUsed));
            return;
        }
        
        overdraftUsed += amount;
        setBalance(checkBalance() + amount);
        getTransactions().add(new Transaction("OVERDRAFT_USED", amount, checkBalance()));
        System.out.println("✓ Overdraft of Rs. " + String.format("%.2f", amount) + " used!");
        System.out.println("  Total Overdraft Used: Rs. " + String.format("%.2f", overdraftUsed));
    }
    
    public void repayOverdraft(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Repayment amount must be greater than zero!");
        }
        
        if (amount > overdraftUsed) {
            System.out.println("❌ Repayment amount exceeds overdraft used!");
            return;
        }
        
        overdraftUsed -= amount;
        setBalance(checkBalance() - amount);
        getTransactions().add(new Transaction("OVERDRAFT_REPAY", amount, checkBalance()));
        System.out.println("✓ Overdraft repaid: Rs. " + String.format("%.2f", amount));
        System.out.println("  Remaining Overdraft: Rs. " + String.format("%.2f", overdraftUsed));
    }
}

// ================== BANK CLASS ==================
class Bank {
    private Map<String, Account> accounts;
    private String bankName;
    private String ifscCode;
    
    public Bank(String bankName, String ifscCode) {
        this.bankName = bankName;
        this.ifscCode = ifscCode;
        this.accounts = new HashMap<>();
    }
    
    public Account createAccount(String name, String phone, String email, 
                                String accountType, double initialDeposit) {
        if (initialDeposit < Account.MIN_BALANCE) {
            System.out.println("❌ Initial deposit must be at least Rs. " + Account.MIN_BALANCE);
            return null;
        }
        
        Account account;
        if (accountType.equalsIgnoreCase("SAVINGS") || accountType.equals("1")) {
            account = new SavingsAccount(name, phone, email, initialDeposit);
        } else if (accountType.equalsIgnoreCase("CURRENT") || accountType.equals("2")) {
            account = new CurrentAccount(name, phone, email, initialDeposit);
        } else {
            System.out.println("❌ Invalid account type!");
            return null;
        }
        
        accounts.put(account.getAccountNumber(), account);
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           ✓ ACCOUNT CREATED SUCCESSFULLY!");
        System.out.println("═".repeat(60));
        System.out.println("Account Number : " + account.getAccountNumber());
        System.out.println("Account Type   : " + account.getAccountType());
        System.out.println("Holder Name    : " + account.getAccountHolderName());
        System.out.println("Initial Deposit: Rs. " + String.format("%.2f", initialDeposit));
        System.out.println("═".repeat(60) + "\n");
        
        return account;
    }
    
    public Account getAccount(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("❌ Account not found!");
        }
        return account;
    }
    
    public void displayBankInfo() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + centerText(bankName, 58) + "║");
        System.out.println("╠" + "═".repeat(58) + "╣");
        System.out.println("║  IFSC Code: " + String.format("%-43s", ifscCode) + "║");
        System.out.println("║  Total Accounts: " + String.format("%-38d", accounts.size()) + "║");
        System.out.println("║  Active Accounts: " + String.format("%-37d", countActiveAccounts()) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");
    }
    
    private int countActiveAccounts() {
        int count = 0;
        for (Account acc : accounts.values()) {
            if (acc.isActive()) count++;
        }
        return count;
    }
    
    private String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }
    
    public int getTotalAccounts() {
        return accounts.size();
    }
}

// ================== MAIN APPLICATION ==================
public class BankingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Bank bank = new Bank("STATE BANK OF JAVA", "SBOJ0001234");
    
    public static void main(String[] args) {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + centerText("WELCOME TO BANKING MANAGEMENT SYSTEM", 58) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");
        
        while (true) {
            try {
                mainMenu();
            } catch (Exception e) {
                System.out.println("❌ An error occurred: " + e.getMessage());
            }
        }
    }
    
    private static void mainMenu() {
        System.out.println("\n╔" + "═".repeat(58) + "╗");
        System.out.println("║" + centerText("MAIN MENU", 58) + "║");
        System.out.println("╠" + "═".repeat(58) + "╣");
        System.out.println("║  1. Create New Account                                   ║");
        System.out.println("║  2. Login to Existing Account                            ║");
        System.out.println("║  3. Display Bank Information                             ║");
        System.out.println("║  4. Exit                                                 ║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        System.out.print("\nEnter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                createNewAccount();
                break;
            case 2:
                loginAccount();
                break;
            case 3:
                bank.displayBankInfo();
                break;
            case 4:
                System.out.println("\n" + "═".repeat(60));
                System.out.println(centerText("Thank you for using our Banking System!", 60));
                System.out.println("═".repeat(60) + "\n");
                System.exit(0);
                break;
            default:
                System.out.println("❌ Invalid choice! Please try again.");
        }
    }
    
    private static void createNewAccount() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println(centerText("CREATE NEW ACCOUNT", 60));
        System.out.println("═".repeat(60));
        
        scanner.nextLine(); // Clear buffer
        
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.print("Enter choice (1/2): ");
        String type = scanner.nextLine();
        
        System.out.print("Enter Initial Deposit (Min Rs. 1000): Rs. ");
        double deposit = getDoubleInput();
        
        bank.createAccount(name, phone, email, type, deposit);
    }
    
    private static void loginAccount() {
        scanner.nextLine(); // Clear buffer
        System.out.print("\nEnter Account Number: ");
        String accNum = scanner.nextLine();
        
        Account account = bank.getAccount(accNum);
        
        if (account != null) {
            if (!account.isActive()) {
                System.out.println("❌ This account is inactive!");
                return;
            }
            accountOperations(account);
        }
    }
    
    private static void accountOperations(Account account) {
        while (true) {
            System.out.println("\n╔" + "═".repeat(58) + "╗");
            System.out.println("║" + centerText("ACCOUNT OPERATIONS", 58) + "║");
            System.out.println("╠" + "═".repeat(58) + "╣");
            System.out.println("║  1. Deposit Money                                        ║");
            System.out.println("║  2. Withdraw Money                                       ║");
            System.out.println("║  3. Check Balance                                        ║");
            System.out.println("║  4. Display Account Information                          ║");
            System.out.println("║  5. Print Passbook                                       ║");
            System.out.println("║  6. Print Mini Statement                                 ║");
            System.out.println("║  7. Transfer Money                                       ║");
            
            if (account instanceof SavingsAccount) {
                System.out.println("║  8. Credit Interest (Savings Account)                    ║");
            } else if (account instanceof CurrentAccount) {
                System.out.println("║  8. Use Overdraft (Current Account)                      ║");
                System.out.println("║  9. Repay Overdraft (Current Account)                    ║");
            }
            
            System.out.println("║  0. Logout                                               ║");
            System.out.println("╚" + "═".repeat(58) + "╝");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        System.out.print("\nEnter amount to deposit: Rs. ");
                        double depositAmt = getDoubleInput();
                        account.deposit(depositAmt);
                        break;
                        
                    case 2:
                        System.out.print("\nEnter amount to withdraw: Rs. ");
                        double withdrawAmt = getDoubleInput();
                        account.withdraw(withdrawAmt);
                        break;
                        
                    case 3:
                        System.out.println("\n" + "═".repeat(50));
                        System.out.println("Current Balance: Rs. " + String.format("%.2f", account.checkBalance()));
                        System.out.println("═".repeat(50));
                        break;
                        
                    case 4:
                        account.displayAccountInfo();
                        break;
                        
                    case 5:
                        System.out.print("\nEnter number of days back (e.g., 30): ");
                        int days = getIntInput();
                        LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
                        LocalDateTime toDate = LocalDateTime.now();
                        account.printPassbook(fromDate, toDate);
                        break;
                        
                    case 6:
                        account.printMiniStatement();
                        break;
                        
                    case 7:
                        scanner.nextLine();
                        System.out.print("\nEnter target account number: ");
                        String targetAccNum = scanner.nextLine();
                        Account targetAccount = bank.getAccount(targetAccNum);
                        
                        if (targetAccount != null && targetAccount.isActive()) {
                            System.out.print("Enter amount to transfer: Rs. ");
                            double transferAmt = getDoubleInput();
                            account.transferMoney(targetAccount, transferAmt);
                        }
                        break;
                        
                    case 8:
                        if (account instanceof SavingsAccount) {
                            ((SavingsAccount) account).creditInterest();
                        } else if (account instanceof CurrentAccount) {
                            System.out.print("\nEnter overdraft amount: Rs. ");
                            double overdraftAmt = getDoubleInput();
                            ((CurrentAccount) account).useOverdraft(overdraftAmt);
                        }
                        break;
                        
                    case 9:
                        if (account instanceof CurrentAccount) {
                            System.out.print("\nEnter repayment amount: Rs. ");
                            double repayAmt = getDoubleInput();
                            ((CurrentAccount) account).repayOverdraft(repayAmt);
                        } else {
                            System.out.println("❌ Invalid choice!");
                        }
                        break;
                        
                    case 0:
                        System.out.println("\n✓ Logged out successfully!");
                        return;
                        
                    default:
                        System.out.println("❌ Invalid choice!");
                }
            } catch (InsufficientBalanceException | DailyLimitExceededException | InvalidAmountException e) {
                System.out.println("\n❌ Transaction Failed!");
                System.out.println("   " + e.getMessage());
            }
        }
    }
    
    // Utility methods
    private static int getIntInput() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.print("Invalid input! Enter a number: ");
                scanner.nextLine();
            }
        }
    }
    
    private static double getDoubleInput() {
        while (true) {
            try {
                return scanner.nextDouble();
            } catch (Exception e) {
                System.out.print("Invalid input! Enter a valid amount: ");
                scanner.nextLine();
            }
        }
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text + 
               " ".repeat(Math.max(0, width - text.length() - padding));
    }
}