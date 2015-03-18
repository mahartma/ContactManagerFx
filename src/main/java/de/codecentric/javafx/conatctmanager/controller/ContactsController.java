package de.codecentric.javafx.conatctmanager.controller;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;

import de.codecentric.javafx.conatctmanager.model.Contact;

public class ContactsController {

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

    @FXML
    private VBox view;

    @PostConstruct
    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        emailColumn.setCellFactory(forTableColumn());

        selectedItem = tableView.selectionModelProperty().get().selectedItemProperty();
        removeButton.disableProperty().bind(selectedItem.isNull());
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

    public void setData(ObservableList<Contact> data) {
        this.data = data;
        this.tableView.setItems(data);

    }
}
