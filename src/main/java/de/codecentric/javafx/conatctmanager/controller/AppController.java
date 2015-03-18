package de.codecentric.javafx.conatctmanager.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import de.codecentric.javafx.conatctmanager.repository.ContactRepository;

public class AppController implements Initializable {

    @FXML
    private ContactsController contactsController;

    @FXML
    private LogController logController;

    private ContactRepository contactRepository;

    @FXML
    private TabPane tabs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactRepository = new ContactRepository();
        contactsController.setData(contactRepository.getData());
        logController.setData(contactRepository.getData());

        tabs.addEventFilter(AddEvent.ADD_EVENT,
                e -> {
                    System.out.println("Contact added...");
                }
                );
    }
}
