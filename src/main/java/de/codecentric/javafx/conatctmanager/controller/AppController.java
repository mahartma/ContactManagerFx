package de.codecentric.javafx.conatctmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

public class AppController implements Initializable {

    @FXML
    private Node mainView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainView.addEventHandler(AddEvent.ADD_EVENT, event -> System.out.println("Contact added..."));
    }
}
