package org.example;

import org.example.models.Author;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner sc;

    public static void main(String[] args) {
        Connection con = connect();
        sc = new Scanner(System.in);

        while (true) {
            printMenu();
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
                        Author.createAuthor();
                        break;
                    case 4:
                        Author.updateAuthor();
                        break;
                    case 5:
                        Author.deleteAuthor();
                        break;
                    case 6:
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

    public static void printMenu() {
        System.out.println("---------------");
        System.out.println("Sveiki atvykę į biblioteką. Pasirinkite norimą funkciją:");
        System.out.println("1. Išspausinti autorių sąrašą");
        System.out.println("2. Rasti autorių");
        System.out.println("3. Sukurti naują autorių");
        System.out.println("4. Redaguoti autorių");
        System.out.println("5. Ištrinti autorių");
        System.out.println("6. Išeiti iš programos");
        System.out.println("---------------");
    }
}