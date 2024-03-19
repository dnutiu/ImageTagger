module com.nuculabs.dev.imagetagger.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.microsoft.onnxruntime;
    requires java.logging;
    requires java.desktop;

    opens com.nuculabs.dev.imagetagger.ui to javafx.fxml;
    exports com.nuculabs.dev.imagetagger.ui;
}