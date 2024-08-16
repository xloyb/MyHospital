module org.example.miniprojet {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;

    opens org.example.miniprojet to javafx.fxml;
    exports org.example.miniprojet;
    opens org.example.miniprojet.models to javafx.base;
    opens org.example.miniprojet.controller to javafx.base, javafx.fxml;
    requires org.controlsfx.controls;
    requires kernel;
    requires layout;
    requires io;


}