module com.tombranfield.scaleconverter {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tombranfield.scaleconverter to javafx.fxml;
    exports com.tombranfield.scaleconverter;
}
