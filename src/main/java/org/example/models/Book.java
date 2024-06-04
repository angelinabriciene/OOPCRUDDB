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

public class Book {
    private long id;
    private String title;
    private String genre;
    private long author_id;

    public Book() {
    }

    public Book(long id, String title, String genre, long author_id) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author_id = author_id;
    }

    public static ArrayList<Book> books() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try {
            Connection con = Main.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while ((rs.next())) {
                Book boo = new Book(rs.getLong("id"), rs.getString("title"), rs.getNString("genre"), rs.getLong("author_id"));
                books.add(boo);
            }
            con.close();
            stmt.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return books;
    }

    public static void printBook() {
        System.out.println("Įveskite knygos Kurią norite rasti ID");
        try {
            long id = sc.nextLong();
            Book b = findById(id);
            if (b.id != 0) {
                System.out.println(b);
            } else {
                System.out.println("Autoriaus su tokiu id nera");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite knygos kurią norite rasti ID skaičiais");
            sc.next();
        }
    }

    public static Book findById(Long id) {
        String query = "SELECT * FROM `books` WHERE id = ?";
        Book b = new Book();
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while ((rs.next())) {
                b = new Book(rs.getLong("id"), rs.getString("title"), rs.getString("genre"), rs.getLong("author_id"));
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Failed to find book:");
            System.out.println(e);
        }
        return b;
    }

    public static void createBook() {
        Book book = new Book();
        System.out.println("Įveskite knygos pavadinimą");
        String title = sc.nextLine();
        System.out.println("Įveskite knygos žanrą");
        String genre = sc.nextLine();
        long author_id = 0;
        while (true) {
            for (Author author : Author.authors()) {
                System.out.println(author.toString());
            }
            System.out.println();
            System.out.println("Pasirinkite iš sąrašo autorių, kuriam priklauso knyga ir įveskite autorias ID:");
            author_id = sc.nextLong();
            boolean hasAuthor = false;
            for (Author author : Author.authors()) {
                if (author.getId() == author_id) {
                    hasAuthor = true;
                    break;
                }
            }
            if (!hasAuthor) {
                System.out.println("Tokio autoriaus nėra");
            } else {
                break;
            }
        }
        if (author_id != 0) {
            book.create(sc, title, genre, author_id);
        } else {
            System.out.println("Tokio autorias bibliotekoje nėra");
        }
    }

    public void create(Scanner sc, String title, String genre, Long author_id) {
        String query = "INSERT INTO `books`(`title`, `genre`, `author_id`) VALUES (?,?,?)";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, title);
            pst.setString(2, genre);
            pst.setLong(3, author_id);
            pst.executeUpdate();
            long bid = 0;
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                bid = generatedKeys.getLong(1);
            }
            con.close();
            pst.close();


            System.out.println("Knyga " + title + " " + genre + " sukurta. Priskirtas ID - " + bid);
        } catch (Exception e) {
            System.out.println("Failed to create a book:");
            System.out.println(e);
        }
    }


    public static void updateBook() {
        System.out.println("Įveskite knygos kurią norite redaguoti ID");
        try {
            long id = sc.nextLong();
            sc.nextLine();
            Book b = findById(id);
            System.out.println(b);
            if (b.id != 0) {
                System.out.println("Įveskite naują knygos pavadinimą");
                b.setTitle(sc.nextLine());
                System.out.println("Įveskite knygos žanrą");
                b.setGenre(sc.nextLine());
                long author_id = 0;
                while (true) {
                    for (Author author : Author.authors()) {
                        System.out.println(author.toString());
                    }
                    System.out.println();
                    System.out.println("Pasirinkite iš sąrašo autorių, kuriam priklauso knyga ir įveskite autorias ID:");
                    author_id = sc.nextLong();
                    sc.nextLine();
                    boolean hasAuthor = false;
                    for (Author author : Author.authors()) {
                        if (author.getId() == author_id) {
                            hasAuthor = true;
                            break;
                        }
                    }
                    if (!hasAuthor) {
                        System.out.println("Tokio autoriaus nėra");
                    } else {
                        b.setAuthor_id(author_id);
                        break;
                    }
                }
                b.update();
                System.out.println("Knyga sėkmingai redaguota - " + b.getId() + " " + b.getTitle() + " " + b.getGenre() + " " + b.getAuthor_id());
            } else {
                System.out.println("Knygos pagal tokį ID nėra");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite knygos kurią norite rasti ID skaičiais");
            sc.next();
        }
    }

    public void update() {
        System.out.println(this);
        String query = "UPDATE `books` SET `title`= ?,`genre`= ?, `author_id` = ? WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, this.title);
            pst.setString(2, this.genre);
            pst.setLong(3, this.author_id);
            pst.setLong(4, this.id);
            pst.executeUpdate();
            con.close();
            pst.close();
        } catch (Exception e) {
            System.out.println("Failed to update a book:");
            System.out.println(e);
        }
    }

    public static void deleteBook() {
        System.out.println("Įveskite ID knygos, kurią norite ištrinti");
        try {
            long id = sc.nextLong();
            sc.nextLine();
            Book b = findById(id);
            System.out.println(b);
            if (b.id != 0) {
                delete(id);
            } else {
                System.out.println("Knygos pagal tokį ID nėra");
            }
        } catch (InputMismatchException e) {
            System.out.println("Klaida. Įveskite knygos kurią norite rasti ID skaičiais");
            sc.next();
        }
    }

    public static void delete(long id) {
        String query = "DELETE FROM `books` WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            pst.executeUpdate();
            con.close();
            pst.close();
            System.out.println("Knyga " + id + " sėkmingai ištrinta");
        } catch (Exception e) {
            System.out.println("Failed to delete a book:");
            System.out.println(e);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", author_id=" + author_id +
                '}';
    }
}

