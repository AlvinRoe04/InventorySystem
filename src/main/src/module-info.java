module wgu.c482.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;

    exports inventorysystem.model;
    opens inventorysystem.model to javafx.fxml;
    exports inventorysystem.controller;
    opens inventorysystem.controller to javafx.fxml;
    exports inventorysystem.main;
    opens inventorysystem.main to javafx.fxml;
}