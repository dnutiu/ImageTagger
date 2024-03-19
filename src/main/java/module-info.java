module com.nuculabs.dev.imagetagger {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens com.nuculabs.dev.imagetagger to javafx.fxml;
    exports com.nuculabs.dev.imagetagger;
}