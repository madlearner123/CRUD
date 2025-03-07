module org.example.cursomaster {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.base;
    requires java.sql;


    opens org.example.cursomaster to javafx.base;
    opens org.example.cursomaster.models to javafx.base;
    opens org.example.cursomaster.jdbc.modulos to javafx.base;
    exports org.example.cursomaster;
}