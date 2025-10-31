import java.util.ArrayList;
import java.util.Scanner;

class SimpleBook {
    private String title;
    private String author;
    private boolean isIssued;
    
    public SimpleBook(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
    }
    
    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isIssued() { return isIssued; }
    
    // Issue/Return methods
    public void issueBook() { isIssued = true; }
    public void returnBook() { isIssued = false; }
    
    // Display book info
    public void displayBook() {
        String status = isIssued ? "Issued" : "Available";
        System.out.println(title + " by " + author + " - " + status);
    }
}

class SimpleMember {
    private String name;
    private int booksIssued;
    
    public SimpleMember(String name) {
        this.name = name;
        this.booksIssued = 0;
    }
    
    public String getName() { return name; }
    public int getBooksIssued() { return booksIssued; }
    
    public void issueBook() { booksIssued++; }
    public void returnBook() { if (booksIssued > 0) booksIssued--; }
    
    public void displayMember() {
        System.out.println(name + " - Books issued: " + booksIssued);
    }
}

class SimpleLibrary {
    private ArrayList<SimpleBook> books;
    private ArrayList<SimpleMember> members;
    
    public SimpleLibrary() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        
        // Add some sample books
        books.add(new SimpleBook("Java Programming", "James Gosling"));
        books.add(new SimpleBook("Python Basics", "Guido van Rossum"));
        books.add(new SimpleBook("C++ Guide", "Bjarne Stroustrup"));
        
        // Add some sample members
        members.add(new SimpleMember("Alice"));
        members.add(new SimpleMember("Bob"));
        members.add(new SimpleMember("Charlie"));
    }
    
    public void displayAllBooks() {
        System.out.println("\n===== ALL BOOKS =====");
        for (int i = 0; i < books.size(); i++) {
            System.out.print((i + 1) + ". ");
            books.get(i).displayBook();
        }
    }
    
    public void displayAvailableBooks() {
        System.out.println("\n===== AVAILABLE BOOKS =====");
        int count = 1;
        for (SimpleBook book : books) {
            if (!book.isIssued()) {
                System.out.print(count + ". ");
                book.displayBook();
                count++;
            }
        }
    }
    
    public void displayAllMembers() {
        System.out.println("\n===== ALL MEMBERS =====");
        for (int i = 0; i < members.size(); i++) {
            System.out.print((i + 1) + ". ");
            members.get(i).displayMember();
        }
    }
    
    public void addBook(String title, String author) {
        books.add(new SimpleBook(title, author));
        System.out.println("Book added successfully!");
    }
    
    public void addMember(String name) {
        members.add(new SimpleMember(name));
        System.out.println("Member added successfully!");
    }
    
    public boolean issueBook(int bookIndex, int memberIndex) {
        if (bookIndex < 0 || bookIndex >= books.size()) {
            System.out.println("Invalid book number!");
            return false;
        }
        
        if (memberIndex < 0 || memberIndex >= members.size()) {
            System.out.println("Invalid member number!");
            return false;
        }
        
        SimpleBook book = books.get(bookIndex);
        SimpleMember member = members.get(memberIndex);
        
        if (book.isIssued()) {
            System.out.println("Book is already issued!");
            return false;
        }
        
        book.issueBook();
        member.issueBook();
        System.out.println("Book '" + book.getTitle() + "' issued to " + member.getName());
        return true;
    }
    
    public boolean returnBook(int bookIndex, int memberIndex) {
        if (bookIndex < 0 || bookIndex >= books.size()) {
            System.out.println("Invalid book number!");
            return false;
        }
        
        if (memberIndex < 0 || memberIndex >= members.size()) {
            System.out.println("Invalid member number!");
            return false;
        }
        
        SimpleBook book = books.get(bookIndex);
        SimpleMember member = members.get(memberIndex);
        
        if (!book.isIssued()) {
            System.out.println("Book is not issued!");
            return false;
        }
        
        book.returnBook();
        member.returnBook();
        System.out.println("Book '" + book.getTitle() + "' returned by " + member.getName());
        return true;
    }
}

public class LibraryManagementSystem {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleLibrary library = new SimpleLibrary();
        
        System.out.println("===== SIMPLE LIBRARY SYSTEM =====");
        
        while (true) {
            System.out.println("\n1. View All Books");
            System.out.println("2. View Available Books");
            System.out.println("3. View All Members");
            System.out.println("4. Add New Book");
            System.out.println("5. Add New Member");
            System.out.println("6. Issue Book");
            System.out.println("7. Return Book");
            System.out.println("8. Exit");
            System.out.print("Choose option (1-8): ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    library.displayAllBooks();
                    break;
                    
                case 2:
                    library.displayAvailableBooks();
                    break;
                    
                case 3:
                    library.displayAllMembers();
                    break;
                    
                case 4:
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author name: ");
                    String author = sc.nextLine();
                    library.addBook(title, author);
                    break;
                    
                case 5:
                    System.out.print("Enter member name: ");
                    String name = sc.nextLine();
                    library.addMember(name);
                    break;
                    
                case 6:
                    library.displayAvailableBooks();
                    System.out.print("Enter book number to issue: ");
                    int bookNum = sc.nextInt() - 1; // Convert to 0-based index
                    
                    library.displayAllMembers();
                    System.out.print("Enter member number: ");
                    int memberNum = sc.nextInt() - 1; // Convert to 0-based index
                    
                    library.issueBook(bookNum, memberNum);
                    break;
                    
                case 7:
                    library.displayAllBooks();
                    System.out.print("Enter book number to return: ");
                    int returnBookNum = sc.nextInt() - 1;
                    
                    library.displayAllMembers();
                    System.out.print("Enter member number: ");
                    int returnMemberNum = sc.nextInt() - 1;
                    
                    library.returnBook(returnBookNum, returnMemberNum);
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
