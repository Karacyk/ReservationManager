package com.karacyk.reservationmanager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {

    public java.sql.Date convertToDatabaseColumn(LocalDate entityValue) {
        //zmiala typu localdate na date z sql
        return java.sql.Date.valueOf(entityValue);
    }
    SQLHelper sqlHelper;
    private List<Reservation> reservations;

    public ReservationManager() throws SQLException, ClassNotFoundException {
        this.sqlHelper = new SQLHelper();
        this.reservations = sqlHelper.getAllData();
    }

    public void addReservation(Reservation reservation) throws SQLException, ClassNotFoundException {
        //dodawanie rezerwacji do lokalnej tabeli oraz sql
        reservations.add(reservation);
        sqlHelper.insertRecord(
                convertToDatabaseColumn(reservation.getStartDate()),
                convertToDatabaseColumn(reservation.getEndDate()),
                reservation.getNumberOfGuests(),
                reservation.getRoomType(),
                reservation.getNameSurname(),
                reservation.getTelephone()
        );
    }

    public void removeReservation(Reservation reservation) throws SQLException, ClassNotFoundException {
        //to samo co wyzej, tylko usuwanie
        reservations.remove(reservation);
        sqlHelper.removeReservation(reservation.getNameSurname());
    }

    public List<Reservation> getAllReservations() {
        //pobieranie all rezerwacji z lokalnej tabeli
        return reservations;
    }
}