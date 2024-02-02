module com.example.cs202pz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cs202pz to javafx.fxml;
    exports com.example.cs202pz;
    exports com.example.cs202pz.Scenes;
    opens com.example.cs202pz.Scenes to javafx.fxml;
    exports com.example.cs202pz.Interfaces;
    opens com.example.cs202pz.Interfaces to javafx.fxml;
}