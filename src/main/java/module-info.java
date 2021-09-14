module com.example.bmi_socketprogramming {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bmi_socketprogramming to javafx.fxml;
    exports com.example.bmi_socketprogramming;
}