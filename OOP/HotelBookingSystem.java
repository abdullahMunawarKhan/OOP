import java.util.Scanner;

public class HotelBookingSystem {
    
    private static final int FLOORS = 5;
    private static final int ROOMS_PER_FLOOR = 10;
    private static int[][] hotelRooms = new int[FLOORS][ROOMS_PER_FLOOR];
    
    static {
        hotelRooms[0][2] = 1;
        hotelRooms[1][5] = 1;
        hotelRooms[2][3] = 1;
        hotelRooms[3][7] = 1;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== HOTEL ROOM BOOKING SYSTEM =====\n");
        while (true) {
            System.out.println("\n----- Main Menu -----");
            System.out.println("1. View Room Status");
            System.out.println("2. Book a Room");
            System.out.println("3. View Booking Statistics");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        viewRoomStatus();
                        break;
                    case 2:
                        bookRoom(sc);
                        break;
                    case 3:
                        showStatistics();
                        break;
                    case 4:
                        System.out.println("\nThank you for using Hotel Booking System!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice! Please select 1-4.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid input! Please enter a number.");
                sc.nextLine(); // Clear buffer
            }
        }
    }
    // Method to view room status
    public static void viewRoomStatus() {
        System.out.println("\n===== ROOM STATUS =====");
        System.out.println("O = Available | X = Booked\n");
        
        for (int floor = 0; floor < FLOORS; floor++) {
            System.out.print("Floor " + (floor + 1) + ": ");
            for (int room = 0; room < ROOMS_PER_FLOOR; room++) {
                if (hotelRooms[floor][room] == 0) {
                    System.out.print("O ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }
    // Method to book a room
    public static void bookRoom(Scanner sc) {
        System.out.println("\n----- Book a Room -----");
        try {
            System.out.print("Enter Floor number (1-" + FLOORS + "): ");
            int floor = sc.nextInt() - 1; // Convert to 0-based index
            System.out.print("Enter Room number (1-" + ROOMS_PER_FLOOR + "): ");
            int room = sc.nextInt() - 1; // Convert to 0-based index
            // Validate input
            if (floor < 0 || floor >= FLOORS) {
                System.out.println("Error: Invalid floor number! Please enter between 1 and " + FLOORS);
                return;
            }
            if (room < 0 || room >= ROOMS_PER_FLOOR) {
                System.out.println("Error: Invalid room number! Please enter between 1 and " + ROOMS_PER_FLOOR);
                return;
            }
            // Check if room is available
            if (hotelRooms[floor][room] == 0) {
                hotelRooms[floor][room] = 1;
                System.out.println("\n✓ Room " + (room + 1) + " on Floor " + (floor + 1) + " booked successfully!");
                System.out.println("Your room number is: " + (floor + 1) + String.format("%02d", (room + 1)));
            } else {
                System.out.println("\n✗ Sorry! This room is already booked. Please choose another room.");
            }
        } catch (Exception e) {
            System.out.println("Error: Invalid input! Please enter numeric values.");
            sc.nextLine(); // Clear buffer
        }
    }
    // Method to show booking statistics
    public static void showStatistics() {
        int totalRooms = FLOORS * ROOMS_PER_FLOOR;
        int bookedRooms = 0;
        for (int floor = 0; floor < FLOORS; floor++) {
            for (int room = 0; room < ROOMS_PER_FLOOR; room++) {
                if (hotelRooms[floor][room] == 1) {
                    bookedRooms++;
                }
            }
        }
        int availableRooms = totalRooms - bookedRooms;
        double occupancyRate = (bookedRooms * 100.0) / totalRooms;
        System.out.println("\n===== BOOKING STATISTICS =====");
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("Booked Rooms: " + bookedRooms);
        System.out.println("Available Rooms: " + availableRooms);
        System.out.printf("Occupancy Rate: %.2f%%\n", occupancyRate);
    }
}
