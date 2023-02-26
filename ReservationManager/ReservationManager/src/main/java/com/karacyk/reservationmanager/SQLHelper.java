package com.karacyk.reservationmanager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SQLHelper {

    // tutaj link do laczenia z baza danych, reservations - nazwa bazy i tabeli
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/reservations";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "";
    private static final String INSERT_QUERY = "INSERT INTO reservations VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_QUERY = "SELECT * FROM reservations";

    private static final String REMOVE_QUERY = "DELETE FROM `reservations` WHERE `nameSurname` = ?";

    //class forname jest po to aby mysql dzialal, ktory jest dodany w pom.xml jako zależność
    public void insertRecord(java.sql.Date startDate, java.sql.Date endDate, int numberOfGuests, String roomType, String nameSurname, String telephone) throws SQLException, ClassNotFoundException {
        //dodawanie rezerwacji
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            preparedStatement.setString(3, nameSurname);
            preparedStatement.setInt(4, numberOfGuests);
            preparedStatement.setString(5, roomType);
            preparedStatement.setString(6, telephone);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<Reservation> getAllData() throws SQLException, ClassNotFoundException {
        //pobieranie all z tabeli
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(GET_QUERY);) {
            while(rs.next()) {
                reservations.add(new Reservation(rs.getDate("startData").toLocalDate(), rs.getDate("endData").toLocalDate(), rs.getInt("numberOfGuests"), rs.getString("roomType"), rs.getString("nameSurname"), rs.getString("telephone")));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return reservations;
    }

    public void removeReservation(String nameSurname) throws SQLException, ClassNotFoundException {
        //usuwanie z tabeli
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_QUERY)) {
            preparedStatement.setString(1, nameSurname);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}