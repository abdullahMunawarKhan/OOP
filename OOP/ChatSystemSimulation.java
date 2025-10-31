import java.util.Scanner;

class SimpleUser implements Runnable {
    private String userName;
    private boolean running = true;
    private int messageCount = 0;
    
    public SimpleUser(String userName) {
        this.userName = userName;
    }
    
    @Override
    public void run() {
        System.out.println(userName + " joined the chat!");
        
        // Send 3 messages
        for (int i = 1; i <= 3 && running; i++) {
            messageCount = i;
            System.out.println(userName + ": Hello everyone! (Message " + i + ")");
            
            try {
                // Wait 2 seconds between messages
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(userName + " was interrupted!");
                break;
            }
        }
        
        System.out.println(userName + " left the chat after " + messageCount + " messages.");
    }
    
    public void stop() {
        running = false;
    }
    
    public String getUserName() {
        return userName;
    }
}

public class ChatSystemSimulation {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=== SIMPLE CHAT SYSTEM ===");
        
        while (true) {
            System.out.println("\n1. Start Simple Chat Demo");
            System.out.println("2. Start Custom Users Chat");
            System.out.println("3. Exit");
            System.out.print("Choose option (1-3): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    runSimpleDemo();
                    break;
                case 2:
                    runCustomChat();
                    break;
                case 3:
                    System.out.println("Thank you for using the chat system!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void runSimpleDemo() {
        System.out.println("\n--- Starting Simple Demo ---");
        
        // Create 3 users
        SimpleUser alice = new SimpleUser("Alice");
        SimpleUser bob = new SimpleUser("Bob");
        SimpleUser charlie = new SimpleUser("Charlie");
        
        // Create threads for each user
        Thread t1 = new Thread(alice);
        Thread t2 = new Thread(bob);
        Thread t3 = new Thread(charlie);
        
        // Start all threads
        System.out.println("Starting chat threads...");
        t1.start();
        t2.start();
        t3.start();
        
        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Demo was interrupted!");
        }
        
        System.out.println("--- Demo Complete ---");
    }
    
    private static void runCustomChat() {
        System.out.print("How many users do you want? ");
        int numUsers = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        
        Thread[] threads = new Thread[numUsers];
        SimpleUser[] users = new SimpleUser[numUsers];
        
        // Create users
        for (int i = 0; i < numUsers; i++) {
            System.out.print("Enter name for user " + (i + 1) + ": ");
            String name = scanner.nextLine();
            
            users[i] = new SimpleUser(name);
            threads[i] = new Thread(users[i]);
        }
        
        // Start all threads
        System.out.println("\nStarting chat with " + numUsers + " users...");
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all to finish
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Chat was interrupted!");
        }
        
        System.out.println("--- Chat Session Complete ---");
    }
}
