module org.clevernameinc.itnfinal {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires com.google.protobuf;

    opens org.clevernameinc.itnfinal to javafx.fxml;
    exports org.clevernameinc.itnfinal;
    exports org.clevernameinc.itnfinal.forms;
    opens org.clevernameinc.itnfinal.forms to javafx.fxml;
    opens org.clevernameinc.itnfinal.Staging to javafx.base;
    opens org.clevernameinc.itnfinal.user to javafx.base;
}