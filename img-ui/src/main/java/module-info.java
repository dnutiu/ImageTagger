module dev.nuculabs.imagetagger.ui {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.logging;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires kotlinx.coroutines.core;
    requires dev.nuculabs.imagetagger.ai;
    requires dev.nuculabs.imagetagger.core;
    requires org.apache.commons.lang3;

    opens dev.nuculabs.imagetagger.ui to javafx.fxml, javafx.graphics;
    opens dev.nuculabs.imagetagger.ui.controls to javafx.fxml, javafx.graphics, javafx.base;
    opens dev.nuculabs.imagetagger.ui.pages to javafx.fxml, javafx.graphics;
    exports dev.nuculabs.imagetagger.ui;
}