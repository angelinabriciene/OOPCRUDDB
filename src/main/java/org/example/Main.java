package org.example;

import org.example.models.Author;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {

        Connection con = connect();
        System.out.println(Author.selectAll());
        System.out.println(Author.findById(10));
        Author.create("Mama", "Mia");
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
}