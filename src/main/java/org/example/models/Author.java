package org.example.models;

import org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import static org.example.Main.sc;

public class Author {
    private long id;
    private String name;
    private String surname;

    public Author() {
    }

    public Author(long id, String surname, String name) {
        this.id = id;
        this.surname = surname;
        this.name = name;
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

    public static void printAuthor(){
        System.out.println("Įveskite autoriaus Kurį norite rasti ID");
        long id = sc.nextLong();
        findById(id);
    }

    public static Author findById(Long id) {
        String query = "SELECT * FROM `authors` WHERE id = ?";
        Author aut = null;
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while ((rs.next())) {
                aut = new Author(rs.getLong("id"), rs.getString("name"), rs.getString("surname"));
            }
            System.out.println(aut);
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
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, surname);
            pst.executeUpdate();
            con.close();
            pst.close();
            System.out.println("Autorius " + name + " " + surname + " sukurtas. Priskirtas ID - ???");
        } catch (Exception e) {
            System.out.println("Failed to create an author:");
            System.out.println(e);
        }
    }

    public static void updateAuthor(){
        System.out.println("Įveskite autoriaus Kurį norite redaguoti ID");
        long id = sc.nextLong();
        sc.nextLine();
        System.out.println("Įveskite autoriaus vardą");
        String name = sc.nextLine();
        System.out.println("Įveskite autorias pavardę");
        String surname = sc.nextLine();
        update(sc, name, surname, id);
    }

    public static void update(Scanner sc, String name, String surname, Long id) {
        String query = "UPDATE `authors` SET `name`= ?,`surname`= ? WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, surname);
            pst.setLong(3, id);
            pst.executeUpdate();
            con.close();
            pst.close();
            System.out.println("Autorius " + id + " redaguotas sėkmingai, " + name + " " + surname);
        } catch (Exception e) {
            System.out.println("Failed to update an author:");
            System.out.println(e);
        }
    }

    public static void deleteAuthor() {
        System.out.println("Įveskite autoriaus Kurį norite ištrinti ID");
        long id = sc.nextLong();
        sc.nextLine();
        delete(id);
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
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}