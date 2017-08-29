package com.code_client.code_client.controllers;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class InstallerController {

    public Button rubyPaneButton;
    public Button closeApplicationButton;

    public void toggleRubyPane() {
        rubyPaneButton.getScene().setRoot(new VBox());
    }

    public void closeApplication() {
        Platform.exit();
    }

}
