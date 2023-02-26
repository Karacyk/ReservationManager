package com.karacyk.reservationmanager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationApplication extends Application {
    private ReservationManager reservationManager;
    private TableView<Reservation> table;

    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        this.reservationManager = new ReservationManager();
        primaryStage.setTitle("Rezerwacje");
        //tworzenie okna
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        //tworzenie inputow
        Label labelStartDate = new Label("Data rozpoczęcia:");
        GridPane.setConstraints(labelStartDate, 0, 0);

        DatePicker startDatePicker = new DatePicker();
        GridPane.setConstraints(startDatePicker, 1, 0);

        Label labelEndDate = new Label("Data zakończenia:");
        GridPane.setConstraints(labelEndDate, 2, 0);

        DatePicker endDatePicker = new DatePicker();
        GridPane.setConstraints(endDatePicker, 3, 0);

        Label nameSurname = new Label("Imię nazwisko: ");
        GridPane.setConstraints(nameSurname, 0, 1);

        TextField nameSurnameField = new TextField();
        GridPane.setConstraints(nameSurnameField, 1, 1);

        Label telephone = new Label("Telefon: ");
        GridPane.setConstraints(telephone, 2, 1);

        TextField telephoneField = new TextField();
        GridPane.setConstraints(telephoneField, 3, 1);

        Label labelNumberOfGuests = new Label("Liczba osób:");
        GridPane.setConstraints(labelNumberOfGuests, 0, 2);

        TextField numberOfGuestsField = new TextField();
        GridPane.setConstraints(numberOfGuestsField, 1, 2);

        Label labelRoomType = new Label("Typ pokoju:");
        GridPane.setConstraints(labelRoomType, 0, 3);

        ComboBox<String> roomTypeField = new ComboBox<>();
        roomTypeField.getItems().addAll(
                "standardowy",
                "eksluzywny",
                "służbowy"
        );
        GridPane.setConstraints(roomTypeField, 1, 3);
        //dodanie buttona i interakcje po kliknieciu
        Button addButton = new Button("Dodaj rezerwację");
        GridPane.setConstraints(addButton, 1, 4);
        addButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String numberofGuestsString = numberOfGuestsField.getText();
            String nameSurnameString = nameSurnameField.getText();
            String telephoneString = telephoneField.getText();
            if(numberofGuestsString == "" || nameSurnameString == "" || telephoneString == "") return;
            int numberOfGuests = Integer.parseInt(numberofGuestsString);
            String roomType =roomTypeField.getValue();
            Reservation reservation = new Reservation(startDate, endDate, numberOfGuests, roomType, nameSurnameString, telephoneString);
            try {
                reservationManager.addReservation(reservation);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            updateTable();
        });
        //tabelka z rezerwacjami
        table = new TableView<>();
        TableColumn<Reservation, LocalDate> startDateColumn = new TableColumn<>("Data rozpoczęcia");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Reservation, LocalDate> endDateColumn = new TableColumn<>("Data zakończenia");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Reservation, String> roomNameColumn = new TableColumn<>("Imie Nazwisko");
        roomNameColumn.setCellValueFactory(new PropertyValueFactory<>("nameSurname"));
        TableColumn<Reservation, String> roomTelephoneColumn = new TableColumn<>("Telefon");
        roomTelephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        TableColumn<Reservation, Integer> numberOfGuestsColumn = new TableColumn<>("Liczba osób");
        numberOfGuestsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfGuests"));
        TableColumn<Reservation, String> roomTypeColumn = new TableColumn<>("Typ pokoju");
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        table.getColumns().addAll(startDateColumn, endDateColumn, roomNameColumn, roomTelephoneColumn, numberOfGuestsColumn, roomTypeColumn);
        Button removeButton = new Button("Usuń rezerwację");
        removeButton.setOnAction(e -> {
            Reservation selectedReservation = table.getSelectionModel().getSelectedItem();
            if (selectedReservation != null) {
                try {
                    reservationManager.removeReservation(selectedReservation);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                updateTable();
            }
        });
        //tabelka z typami
        List<RoomType> roomTypes = new ArrayList<>();
        roomTypes.add(new RoomType("standardowy", 150));
        roomTypes.add(new RoomType("eksluzywny", 300));
        roomTypes.add(new RoomType("służbowy", 500));
        TableView<RoomType> roomTypeTable = new TableView<>();

        TableColumn<RoomType, String> typeColumn = new TableColumn<>("Typ");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<RoomType, Integer> priceColumn = new TableColumn<>("Cena");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        roomTypeTable.getColumns().addAll(typeColumn, priceColumn);
        roomTypeTable.getItems().setAll(roomTypes);



        GridPane.setConstraints(table, 0, 5, 2, 1);
        GridPane.setConstraints(roomTypeTable, 3, 5);
        GridPane.setConstraints(removeButton, 1, 6);
        //dodanie wszystkich elementow do grida/siatki, setConstraints umiejscowia je wzgledem osi x y
        grid.getChildren().addAll(labelStartDate, startDatePicker, labelEndDate, endDatePicker, nameSurname, nameSurnameField, telephone, telephoneField, labelNumberOfGuests, numberOfGuestsField, labelRoomType, roomTypeField, addButton, table, roomTypeTable, removeButton);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        updateTable();
        primaryStage.show();
    }

    private void updateTable() {
        //pobieranie rezerwacji i update tabelki
        List<Reservation> reservations = reservationManager.getAllReservations();
        table.getItems().setAll(reservations);
    }

    public static void main(String[] args) {
        launch(ReservationApplication.class, args);
    }
}