module ics37201.ics {
    requires javafx.controls;
    requires javafx.fxml;


    opens ics37201.ics to javafx.fxml;
    exports ics37201.ics;
}