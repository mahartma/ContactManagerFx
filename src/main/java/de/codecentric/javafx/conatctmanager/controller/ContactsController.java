package de.codecentric.javafx.conatctmanager.controller;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.inject.Inject;

import de.codecentric.javafx.conatctmanager.model.Contact;
import de.codecentric.javafx.conatctmanager.repository.ContactRepository;

public class ContactsController {

    @FXML
    private Node view;

    @FXML
    private Button removeButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Contact> tableView;

    @FXML
    private TableColumn<Contact, String> firstNameColumn;

    @FXML
    private TableColumn<Contact, String> lastNameColumn;

    @FXML
    private TableColumn<Contact, String> emailColumn;

    private ObservableList<Contact> data;

    private ReadOnlyObjectProperty<Contact> selectedItem;

    @Inject
    private ContactRepository contactRepository;

    @FXML
    public void initialize() {

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        emailColumn.setCellFactory(forTableColumn());
        emailColumn.setCellValueFactory(cellValue -> cellValue.getValue().emailProperty());

        selectedItem = tableView.selectionModelProperty().get().selectedItemProperty();
        removeButton.disableProperty().bind(selectedItem.isNull());

        this.data = contactRepository.getData();
        this.data.forEach(contact -> contact.emailProperty().addListener((changedEmail, oldEmail, newEmail) -> {
            contactRepository.updateEmail(contact, newEmail);
        }));
        this.data.addListener((ListChangeListener.Change<? extends Contact> change) -> {
            while (change.next()) {
                for (Contact newContact : change.getAddedSubList()) {
                    contactRepository.save(newContact);
                    newContact.emailProperty().addListener((changedEmail, oldEmail, newEmail) -> {
                        contactRepository.updateEmail(newContact, newEmail);
                    });
                }
                for (Contact removedContact : change.getRemoved()) {
                    contactRepository.delete(removedContact);
                }
            }
        });
        this.tableView.setItems(data);
    }

    @FXML
    public void filter(ActionEvent event) {
        datePicker.valueProperty().set(LocalDate.of(2014, 1, 1));
    }

    @FXML
    public void add(ActionEvent event) {
        data.add(new Contact("Hans", "Meier", "hans.meier@gmx.de"));
        view.fireEvent(new AddEvent());
    }

    @FXML
    public void remove(ActionEvent event) {
        data.remove(selectedItem.get());
    }
}
