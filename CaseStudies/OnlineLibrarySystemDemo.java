import java.util.ArrayList;
import java.util.Scanner;

// Simple Book class
class SimpleBook {
    private String bookId;
    private String title;
    private String author;
    private boolean isIssued;
    private String issuedTo;
    
    public SimpleBook(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedTo = null;
    }
    
    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }
    public String getIssuedTo() { return issuedTo; }
    
    // Issue/Return methods
    public void issueBook(String memberName) {
        isIssued = true;
        issuedTo = memberName;
    }
    
    public void returnBook() {
        isIssued = false;
        issuedTo = null;
    }
    
    public void displayBook() {
        String status = isIssued ? "Issued to " + issuedTo : "Available";
        System.out.printf("%-8s %-25s %-20s %-15s\n", bookId, title, author, status);
    }
}

// Simple Member class
class SimpleMember {
    private String memberId;
    private String name;
    private int booksIssued;
    
    public SimpleMember(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.booksIssued = 0;
    }
    
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public int getBooksIssued() { return booksIssued; }
    
    public boolean canIssueBook() {
        return booksIssued < 3; // Maximum 3 books
    }
    
    public void issueBook() { booksIssued++; }
    public void returnBook() { if (booksIssued > 0) booksIssued--; }
    
    public void displayMember() {
        System.out.printf("%-10s %-20s %d books\n", memberId, name, booksIssued);
    }
}

// Simple Library class
class SimpleLibrary {
    private ArrayList<SimpleBook> books;
    private ArrayList<SimpleMember> members;
    
    public SimpleLibrary() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        initializeData();
    }
    
    private void initializeData() {
        // Add sample books
        books.add(new SimpleBook("B001", "Java Programming", "James Gosling"));
        books.add(new SimpleBook("B002", "Clean Code", "Robert Martin"));
        books.add(new SimpleBook("B003", "Design Patterns", "Gang of Four"));
        
        // Add sample members
        members.add(new SimpleMember("M001", "Alice Smith"));
        members.add(new SimpleMember("M002", "Bob Johnson"));
        members.add(new SimpleMember("M003", "Charlie Brown"));
    }
    
    public void displayAllBooks() {
        System.out.println("\n===== ALL BOOKS =====");
        System.out.printf("%-8s %-25s %-20s %-15s\n", "Book ID", "Title", "Author", "Status");
        System.out.println("---------------------------------------------------------------");
        for (SimpleBook book : books) {
            book.displayBook();
        }
    }
    
    public void displayAllMembers() {
        System.out.println("\n===== ALL MEMBERS =====");
        System.out.printf("%-10s %-20s %s\n", "Member ID", "Name", "Books Issued");
        System.out.println("----------------------------------------");
        for (SimpleMember member : members) {
            member.displayMember();
        }
    }
    
    public SimpleBook findBook(String bookId) {
        for (SimpleBook book : books) {
            if (book.getBookId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }
    
    public SimpleMember findMember(String memberId) {
        for (SimpleMember member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    
    public void issueBook(String bookId, String memberId) {
        try {
            SimpleBook book = findBook(bookId);
            if (book == null) {
                throw new Exception("Book not found!");
            }
            
            SimpleMember member = findMember(memberId);
            if (member == null) {
                throw new Exception("Member not found!");
            }
            
            if (book.isIssued()) {
                throw new Exception("Book is already issued!");
            }
            
            if (!member.canIssueBook()) {
                throw new Exception("Member has reached book limit!");
            }
            
            // Issue the book
            book.issueBook(member.getName());
            member.issueBook();
            
            System.out.println("\n✓ Book issued successfully!");
            System.out.println("Book: " + book.getTitle());
            System.out.println("Member: " + member.getName());
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void returnBook(String bookId) {
        try {
            SimpleBook book = findBook(bookId);
            if (book == null) {
                throw new Exception("Book not found!");
            }
            
            if (!book.isIssued()) {
                throw new Exception("Book is not issued!");
            }
            
            String memberName = book.getIssuedTo();
            
            // Find member and update record
            for (SimpleMember member : members) {
                if (member.getName().equals(memberName)) {
                    member.returnBook();
                    break;
                }
            }
            
            book.returnBook();
            
            System.out.println("\n✓ Book returned successfully!");
            System.out.println("Book: " + book.getTitle());
            System.out.println("Returned by: " + memberName);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void addBook(String bookId, String title, String author) {
        books.add(new SimpleBook(bookId, title, author));
        System.out.println("Book added successfully!");
    }
    
    public void addMember(String memberId, String name) {
        members.add(new SimpleMember(memberId, name));
        System.out.println("Member added successfully!");
    }
    
    public void showStatistics() {
        int totalBooks = books.size();
        int issuedBooks = 0;
        
        for (SimpleBook book : books) {
            if (book.isIssued()) {
                issuedBooks++;
            }
        }
        
        System.out.println("\n===== LIBRARY STATISTICS =====");
        System.out.println("Total Books: " + totalBooks);
        System.out.println("Available Books: " + (totalBooks - issuedBooks));
        System.out.println("Issued Books: " + issuedBooks);
        System.out.println("Total Members: " + members.size());
    }
}

public class OnlineLibrarySystemDemo {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleLibrary library = new SimpleLibrary();
        
        System.out.println("===== SIMPLE LIBRARY SYSTEM =====");
        
        while (true) {
            System.out.println("\n1. Display All Books");
            System.out.println("2. Display All Members");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Add New Book");
            System.out.println("6. Add New Member");
            System.out.println("7. Show Statistics");
            System.out.println("8. Exit");
            System.out.print("Choose option (1-8): ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    library.displayAllBooks();
                    break;
                    
                case 2:
                    library.displayAllMembers();
                    break;
                    
                case 3:
                    System.out.print("Enter Book ID: ");
                    String issueBookId = sc.nextLine();
                    System.out.print("Enter Member ID: ");
                    String issueMemberId = sc.nextLine();
                    library.issueBook(issueBookId, issueMemberId);
                    break;
                    
                case 4:
                    System.out.print("Enter Book ID to return: ");
                    String returnBookId = sc.nextLine();
                    library.returnBook(returnBookId);
                    break;
                    
                case 5:
                    System.out.print("Enter Book ID: ");
                    String bookId = sc.nextLine();
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    library.addBook(bookId, title, author);
                    break;
                    
                case 6:
                    System.out.print("Enter Member ID: ");
                    String memberId = sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    library.addMember(memberId, name);
                    break;
                    
                case 7:
                    library.showStatistics();
                    break;
                    
                case 8:
                    System.out.println("Thank you for using the library system!");
                    sc.close();
                    return;
                    
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}
