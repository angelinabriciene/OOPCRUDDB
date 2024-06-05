package org.example;

import org.example.models.Author;
import org.example.models.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner sc;

    public static void main(String[] args) {
        Connection con = connect();
        sc = new Scanner(System.in);
        while (true) {
            System.out.println("Pasirinkite norimą meniu:");
            System.out.println("1. Autorių meniu");
            System.out.println("2. Knygų meniu");
            switch (sc.nextInt()) {
                case 1:
                    authorsMenu();
                    break;
                case 2:
                    booksMenu();
                    break;
                case 3:
                    System.out.println("Viso gero!");
                    System.exit(1);
            }
        }
    }

    public static void authorsMenu() {
        boolean aMenu = true;
        while (aMenu) {
            printAuthorsMenu();
            try {
                int input = sc.nextInt();
                sc.nextLine();
                switch (input) {
                    case 1:
                        for (Author author : Author.authors()) {
                            System.out.println(author.toString());
                        }
                        break;
                    case 2:
                        Author.printAuthor();
                        break;
                    case 3:
                        Author.listAuthorsAndBooks();
                        break;
                    case 4:
                        Author.printAuthorAndBook();
                        break;
                    case 5:
                        Author.createAuthor();
                        break;
                    case 6:
                        Author.updateAuthor();
                        break;
                    case 7:
                        Author.deleteAuthor();
                        break;
                    case 8:
                        aMenu = false;
                        break;
                    case 9:
                        System.out.println("Viso gero!");
                        System.exit(1);
                    default:
                        System.out.println("Įveskite skaičių rinkdamiesi iš meniu");
                }
            } catch (InputMismatchException e) {
                System.out.println("Klaida. Įveskite skaičių rinkdamiesi iš meniu");
                sc.next();
            }
        }
    }

    private static void booksMenu() {
        boolean bMenu = true;
        while (bMenu) {
            printBooksMenu();
            try {
                int input = sc.nextInt();
                sc.nextLine();
                switch (input) {
                    case 1:
                        Book.printBooks();
                        break;
                    case 2:
                        Book.printBookAndAuthor();
                        break;
                    case 3:
                        Book.createBook();
                        break;
                    case 4:
                        Book.updateBook();
                        break;
                    case 5:
                        Book.deleteBook();
                        break;
                    case 6:
                        bMenu = false;
                        break;
                    case 7:
                        System.out.println("Viso gero!");
                        System.exit(1);
                    default:
                        System.out.println("Įveskite skaičių rinkdamiesi iš meniu");
                }
            } catch (InputMismatchException e) {
                System.out.println("Klaida. Įveskite skaičių rinkdamiesi iš meniu");
                sc.next();
            }
        }
    }

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/libarary", "root", "");
        } catch (Exception e) {
            System.out.println("Failed to connect to database");
        }
        return connection;
    }

    public static void printAuthorsMenu() {
        System.out.println("---------------");
        System.out.println("Sveiki atvykę į autorių biblioteką. Pasirinkite norimą funkciją:");
        System.out.println("1. Išspausinti autorių sąrašą");
        System.out.println("2. Rasti autorių");
        System.out.println("3. Išspausinti autorių sąrašą ir jiems priskirtas knygas");
        System.out.println("4. Rasti autorių ir jo parašytas knygas");
        System.out.println("5. Sukurti naują autorių");
        System.out.println("6. Redaguoti autorių");
        System.out.println("7. Ištrinti autorių");
        System.out.println("8. Grįžti į pagrindinį meniu");
        System.out.println("9. Išeiti iš programos");
        System.out.println("---------------");
    }

    public static void printBooksMenu() {
        System.out.println("---------------");
        System.out.println("Sveiki atvykę į knygų biblioteką. Pasirinkite norimą funkciją:");
        System.out.println("1. Išspausinti knygų sąrašą");
        System.out.println("2. Rasti knygą");
        System.out.println("3. Sukurti naują knygą");
        System.out.println("4. Redaguoti knygą");
        System.out.println("5. Ištrinti knygą");
        System.out.println("6. Grįžti į pagrindinį meniu");
        System.out.println("7. Išeiti iš programos");
        System.out.println("---------------");
    }
}