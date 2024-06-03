package org.example.models;

import org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Author {
    private long id;
    private String name;
    private  String surname;

    public Author() {
    }

    public Author(long id, String surname, String name) {
        this.id = id;
        this.surname = surname;
        this.name = name;
    }

    public static ArrayList<Author> selectAll() {
        ArrayList<Author> authors = new ArrayList<>();
        String query = "SELECT * FROM authors";
        try {
            Connection con = Main.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ((rs.next())) {
                Author aut = new Author(rs.getLong("id"), rs.getString("name"), rs.getNString("surname") );
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
    public static Author findById(long id) {
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
            con.close();
            pst.close();
            rs.close();
        }catch (Exception e) {
            System.out.println(e);
        }
        return aut;
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
