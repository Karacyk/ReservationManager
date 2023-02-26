module com.karacyk.reservationmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.karacyk.reservationmanager to javafx.fxml;
    exports com.karacyk.reservationmanager;
}