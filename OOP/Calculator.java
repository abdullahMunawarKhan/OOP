import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {
    
    public static double add(double a, double b) {
        return a + b;
    }
    public static double subtract(double a, double b) {
        return a - b;
    }
    public static double multiply(double a, double b) {
        return a * b;
    }
    public static double divide(double a, double b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
        }
        return a / b;
    }
    
    public static double modulus(double a, double b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Cannot find modulus with zero!");
        }
        return a % b;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char choice = 'Y'; // Initialize the variable here
        
        System.out.println("===== ROBUST JAVA CALCULATOR =====");
        
        do {
            try {
                System.out.println("\n----- Calculator Menu -----");
                System.out.println("1. Addition (+)");
                System.out.println("2. Subtraction (-)");
                System.out.println("3. Multiplication (*)");
                System.out.println("4. Division (/)");
                System.out.println("5. Modulus (%)");
                System.out.println("6. Exit");
                System.out.print("Enter your choice (1-6): ");
                
                int operation = sc.nextInt();
                
                if (operation == 6) {
                    System.out.println("Thank you for using the calculator!");
                    break;
                }
                
                if (operation < 1 || operation > 5) {
                    System.out.println("Invalid choice! Please select between 1-6.");
                    continue;
                }
                
                System.out.print("Enter first number: ");
                double num1 = sc.nextDouble();
                
                System.out.print("Enter second number: ");
                double num2 = sc.nextDouble();
                
                double result = 0;
                
                switch (operation) {
                    case 1:
                        result = add(num1, num2);
                        System.out.println("Result: " + num1 + " + " + num2 + " = " + result);
                        break;
                    case 2:
                        result = subtract(num1, num2);
                        System.out.println("Result: " + num1 + " - " + num2 + " = " + result);
                        break;
                    case 3:
                        result = multiply(num1, num2);
                        System.out.println("Result: " + num1 + " * " + num2 + " = " + result);
                        break;
                    case 4:
                        result = divide(num1, num2);
                        System.out.println("Result: " + num1 + " / " + num2 + " = " + result);
                        break;
                    case 5:
                        result = modulus(num1, num2);
                        System.out.println("Result: " + num1 + " % " + num2 + " = " + result);
                        break;
                }
                
            } catch (ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input! Please enter numeric values only.");
                sc.nextLine(); // Clear the buffer
            } catch (Exception e) {
                System.out.println("Error: An unexpected error occurred - " + e.getMessage());
                sc.nextLine();
            } finally {
                System.out.println("Operation completed.");
            }
            
            System.out.print("\nDo you want to perform another calculation? (Y/N): ");
            choice = sc.next().charAt(0);
            
        } while (choice == 'Y' || choice == 'y');
        
        System.out.println("\nExiting calculator. Goodbye!");
        sc.close();
    }
}
