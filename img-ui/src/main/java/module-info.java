module dev.nuculabs.imagetagger.ui {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.microsoft.onnxruntime;
    requires java.logging;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires kotlinx.coroutines.core;

    opens dev.nuculabs.imagetagger.ui to javafx.fxml, javafx.graphics;
    opens dev.nuculabs.imagetagger.ui.controls to javafx.fxml, javafx.graphics;
    exports dev.nuculabs.imagetagger.ui;
}