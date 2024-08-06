module org.example.countrytravel {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens org.example.countrytravel to javafx.fxml;
    exports org.example.countrytravel;
}