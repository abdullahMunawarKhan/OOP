import java.util.Scanner;

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
class ATM {
    private double balance;
    private String accountNumber;
    
    public ATM(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    public void checkBalance() {
        System.out.println("\n===== BALANCE INQUIRY =====");
        System.out.println("Account Number: " + accountNumber);
        System.out.printf("Current Balance: Rs. %.2f\n", balance);
    }
    public void deposit(double amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive!");
        }
        balance += amount;
        System.out.println("\n===== DEPOSIT SUCCESSFUL =====");
        System.out.printf("Amount Deposited: Rs. %.2f\n", amount);
        System.out.printf("New Balance: Rs. %.2f\n", balance);
    }
    // Method to withdraw money
    public void withdraw(double amount) throws InsufficientFundsException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive!");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds! Your current balance is Rs. " + balance);
        }
        balance -= amount;
        System.out.println("\n===== WITHDRAWAL SUCCESSFUL =====");
        System.out.printf("Amount Withdrawn: Rs. %.2f\n", amount);
        System.out.printf("Remaining Balance: Rs. %.2f\n", balance);
    }
    public double getBalance() {
        return balance;
    }
}
public class ATMSimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Initialize ATM with account
        ATM atm = new ATM("ACC123456789", 10000.00);
        System.out.println("===== WELCOME TO ATM SYSTEM =====");
        System.out.println("Your account has been initialized with Rs. 10,000.00\n");
        while (true) {
            System.out.println("\n----- ATM Menu -----");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        atm.checkBalance();
                        break;
                        
                    case 2:
                        System.out.print("\nEnter amount to deposit: Rs. ");
                        double depositAmount = sc.nextDouble();
                        atm.deposit(depositAmount);
                        break;
                        
                    case 3:
                        System.out.print("\nEnter amount to withdraw: Rs. ");
                        double withdrawAmount = sc.nextDouble();
                        atm.withdraw(withdrawAmount);
                        break;
                        
                    case 4:
                        System.out.println("\nThank you for using our ATM services!");
                        System.out.println("Have a great day!");
                        sc.close();
                        return;
                        
                    default:
                        System.out.println("Invalid choice! Please select 1-4.");
                }
                
            } catch (InsufficientFundsException e) {
                System.out.println("\n✗ Transaction Failed!");
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("\n✗ Invalid Input!");
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n✗ Error: Invalid input! Please enter numeric values only.");
                sc.nextLine(); // Clear buffer
            } finally {
                System.out.println("\nTransaction processing completed.");
            }
        }
    }
}
