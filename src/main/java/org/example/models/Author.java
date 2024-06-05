package org.example.models;

import org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.example.Main.sc;

public class Author {
    private long id;
    private String name;
    private String surname;

    public Author() {
    }

    public Author(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public static ArrayList<Author> authors() {
        ArrayList<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";
        try {
            Connection con = Main.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ((rs.next())) {
                Author aut = new Author(rs.getLong("id"), rs.getString("name"), rs.getNString("surname"));
                authors.add(aut);
            }
            con.close();
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return authors;
    }

    public static void printAuthor() {
        System.out.println("Įveskite autoriaus Kurį norite rasti ID");
        try {
            long id = sc.nextLong();
            Author a = findById(id);
            if (a.id != 0) {
                System.out.println(a);
            } else {
                System.out.println("Autoriaus su tokiu id nera");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite autoriaus Kurį norite rasti ID skaičiais");
            sc.next();
        }
    }

    public static Author findById(Long id) {
        String query = "SELECT * FROM `authors` WHERE id = ?";
        Author aut = new Author();
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while ((rs.next())) {
                aut = new Author(rs.getLong("id"), rs.getString("name"), rs.getString("surname"));
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Failed to find author:");
            System.out.println(e);
        }
        return aut;
    }

    public static void createAuthor() {
        Author author = new Author();
        System.out.println("Įveskite autoriaus vardą");
        String name = sc.nextLine();
        System.out.println("Įveskite autorias pavardę");
        String surname = sc.nextLine();
        author.create(sc, name, surname);
    }

    public void create(Scanner sc, String name, String surname) {
        String query = "INSERT INTO `authors`(`name`, `surname`) VALUES (?,?)";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setString(2, surname);
            pst.executeUpdate();
            long bid = 0;
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                bid = generatedKeys.getLong(1);
            }
            con.close();
            pst.close();
            System.out.println("Autorius " + name + " " + surname + " sukurtas. Priskirtas ID - " + bid);
        } catch (Exception e) {
            System.out.println("Failed to create an author:");
            System.out.println(e);
        }
    }

    public static void updateAuthor() {
        System.out.println("Įveskite autoriaus Kurį norite redaguoti ID");
        try {
            long id = sc.nextLong();
            sc.nextLine();
            Author a = findById(id);
            System.out.println(a);
            if (a.id != 0) {
                System.out.println("Įveskite autoriaus vardą");
                a.setName(sc.nextLine());
                System.out.println("Įveskite autoriaus pavardę");
                a.setSurname(sc.nextLine());
                a.update();
                System.out.println("Autorius sėkmingai redaguotas - " + a.getName() + " " + a.getSurname());
            } else {
                System.out.println("Autoriaus pagal tokį ID nėra");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite autoriaus kurį norite rasti ID skaičiais");
            sc.next();
        }
    }

    public void update() {
        String query = "UPDATE `authors` SET `name`= ?,`surname`= ? WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, this.name);
            pst.setString(2, this.surname);
            pst.setLong(3, this.id);
            pst.executeUpdate();
            con.close();
            pst.close();
        } catch (Exception e) {
            System.out.println("Failed to update an author:");
            System.out.println(e);
        }
    }

//    public static void update( String name, String surname, Long id) {
//        String query = "UPDATE `authors` SET `name`= ?,`surname`= ? WHERE id = ?";
//        try {
//            Connection con = Main.connect();
//            PreparedStatement pst = con.prepareStatement(query);
//            pst.setString(1, name);
//            pst.setString(2, surname);
//            pst.setLong(3, id);
//            pst.executeUpdate();
//            con.close();
//            pst.close();
//            System.out.println("Autorius " + id + " redaguotas sėkmingai, " + name + " " + surname);
//        } catch (Exception e) {
//            System.out.println("Failed to update an author:");
//            System.out.println(e);
//        }
//    }

    public static void deleteAuthor() {
        System.out.println("Įveskite autoriaus Kurį norite ištrinti ID");
        try {
            long id = sc.nextLong();
            sc.nextLine();
            Author a = findById(id);
            System.out.println(a);
            if (a.id != 0) {
                delete(id);
            } else {
                System.out.println("Autoriaus pagal tokį ID nėra");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite autoriaus Kurį norite rasti ID skaičiais");
            sc.next();
        }
    }

    public static void delete(long id) {
        String query = "DELETE FROM `authors` WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            pst.executeUpdate();
            con.close();
            pst.close();
            System.out.println("Autorius " + id + " sėkmingai ištrintas");
        } catch (Exception e) {
            System.out.println("Failed to delete an author:");
            System.out.println(e);
        }
    }

    public static void listAuthorsAndBooks() {
        for (Author author : Author.authors()) {
            System.out.println();
            System.out.println(author.toString());
            boolean hasBook = false;
            for (Book book : Book.books()) {
                if (book.getAuthor_id() == author.getId()) {
                    System.out.println("Knyga -" + book.getTitle() + ". Žanras - " + book.getGenre() + ". ID-" + book.getId());
                    hasBook = true;
                }
            }
            if (!hasBook) {
                System.out.println("Autorius knygų neturi");
            }
        }
    }

    public static void printAuthorAndBook() {
        System.out.println("Įveskite ID autorias, kurio knygas norite peržiūrėti");
        try {
            long id = sc.nextLong();
            Author a = findById(id);
            if (a.id != 0) {
                System.out.println(a);
                boolean hasBook = false;
                for (Book book : Book.books()) {
                    if (book.getAuthor_id() == a.getId()) {
                        System.out.println("Knyga -" + book.getTitle() + ". Žanras - " + book.getGenre() + ". ID-" + book.getId());
                        hasBook = true;
                    }
                }
                if (!hasBook) {
                    System.out.println("Autorius knygų neturi");
                }
            } else {
                System.out.println("Autoriaus su tokiu id nera");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite autoriaus Kurį norite rasti ID skaičiais");
            sc.next();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Author: " +
                "id- " + id +
                ", name- " + name + '\'' +
                ", surname- " + surname + '\'' +
                ';';
    }
}