package com.karacyk.reservationmanager;

import java.time.LocalDate;

public class Reservation {
    //klasa odpowiadajaca za rezerwacje
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfGuests;
    private String roomType;

    private String nameSurname;

    private String telephone;

    public Reservation(LocalDate startDate, LocalDate endDate, int numberOfGuests, String roomType, String nameSurname, String telephone) {
        //tworzenie nowej rezerwacji w konstruktorze
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfGuests = numberOfGuests;
        this.roomType = roomType;
        this.nameSurname = nameSurname;
        this.telephone = telephone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNameSurname() {
        return nameSurname;
    }
}
