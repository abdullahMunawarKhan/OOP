import java.util.ArrayList;
import java.util.Scanner;

class Product {
    private String name;
    private int quantity;
    private double price;
    // Simple constructor
    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    // Calculate total cost for this product
    public double getTotal() {
        return quantity * price;
    }
    // Display product details
    public void displayProduct() {
        double total = getTotal();
        System.out.printf("%-20s %5d %10.2f %15.2f\n", 
                         name, quantity, price, total);
    }
    // Getters
    public String getName() { 
        return name; 
    }
     public int getQuantity() { 
        return quantity; 
    }
    public double getPrice() { 
        return price; 
    }
}

public class ECommerceSystem {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Product> cart = new ArrayList<>();
        System.out.println("===== SIMPLE SHOPPING CART =====\n");
        // Get number of products from user
        System.out.print("How many products do you want to buy? ");
        int numProducts = sc.nextInt();
        sc.nextLine(); // Clear buffer
        // Add products to cart
        for (int i = 1; i <= numProducts; i++) {
            System.out.println("\n--- Product " + i + " ---");
            System.out.print("Product Name: ");
            String name = sc.nextLine();
            System.out.print("Quantity: ");
            int quantity = sc.nextInt();
            System.out.print("Price per item: ");
            double price = sc.nextDouble();
            sc.nextLine(); // Clear buffer
            // Create and add product to cart
            Product product = new Product(name, quantity, price);
            cart.add(product);
            
            System.out.println("✓ Product added to cart!");
        }
        // Display bill
        displayBill(cart);
        sc.close();
    }
    
    public static void displayBill(ArrayList<Product> cart) {
        System.out.println("\n\n");
        System.out.println("===============================================");
        System.out.println("                SHOPPING BILL                 ");
        System.out.println("===============================================");
        System.out.printf("%-20s %5s %10s %15s\n", 
                         "Product", "Qty", "Price", "Total");
        System.out.println("-----------------------------------------------");
        
        double grandTotal = 0.0;
        // Show each product
        for (Product product : cart) {
            product.displayProduct();
            grandTotal += product.getTotal();
        }
        System.out.println("===============================================");
        System.out.printf("%40s %.2f\n", "GRAND TOTAL: Rs. ", grandTotal);
        System.out.println("===============================================");
        System.out.println("\nThank you for shopping!");
    }
}
