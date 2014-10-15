package de.codecentric.javafx.conatctmanager.controller;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.function.Predicate;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;

import de.codecentric.javafx.conatctmanager.model.Contact;
import de.codecentric.javafx.conatctmanager.model.LogEntry;
import de.codecentric.javafx.conatctmanager.repository.LogEntryRepository;

public class LogController  {
	
	@FXML
	private ListView<String> logView;
	
	@FXML
	private CheckBox added;

	@FXML
	private CheckBox modified;
	
	@FXML
	private CheckBox removed;
	
	@FXML
	private TextField filter;

	private FilteredList<String> filtered;
	
	private ObservableList<String> items;
	
	private LogEntryRepository repository = new LogEntryRepository();
	
	@PostConstruct
	public void initialize() {
		items = logView.getItems();
		filtered = new FilteredList<String>(items);
		logView.setItems(filtered);
		
		items.addAll(repository.readAll().stream().map(LogEntry::toString)
			.collect(toList()));
	}
	
	public void setData(ObservableList<Contact> data) {
		data.addListener((ListChangeListener.Change<? extends Contact> change) -> {
			handleListChange(change);
			}
		);
	}
	
	@FXML
	public void filter() {
		if (added.isSelected() || removed.isSelected() || modified.isSelected()
				|| !filter.getText().isEmpty()) {
			Predicate<String> predicate = msg -> false;
			if (added.isSelected()) {
				predicate = predicate.or(msg->msg.contains("added"));
			}
			if (removed.isSelected()) {
				predicate = predicate.or(msg->msg.contains("removed"));
			}
			if (modified.isSelected()) {
				predicate = predicate.or(msg->msg.contains("modified"));
			}
			if (!filter.getText().isEmpty()) {
				predicate = predicate.or(msg->msg.contains(filter.getText()));
			}
			filtered.setPredicate(predicate);
		} else {
			filtered.setPredicate(msg -> true);
		}
	}

	private void handleListChange(ListChangeListener.Change<? extends Contact> change) {
		while(change.next()) {
			filter.disableProperty().set(false);
			if (change.wasAdded()) {
				for (Contact addedContact :change.getAddedSubList()) {
					handleAdd(addedContact);
				}
			} else if (change.wasRemoved()) {
				for (Contact removedContact :change.getRemoved()) {
					handleRemove(removedContact);
				}
			}
			
		}
	}

	private void handleRemove(Contact removedContact) {
		logMessage("Contact: " + removedContact.getFirstName() + " " + removedContact.getLastName() + " removed.");
	}

	private void handleAdd(Contact addedContact) {
		logMessage("Contact: " + addedContact.getFirstName() + " " + addedContact.getLastName() + " added.");
		addedContact.emailProperty().addListener((contact, oldEmail, newEmail) -> {
				handleModify(oldEmail, newEmail);
			});
	}

	private void handleModify(String oldEmail, String newEmail) {
		logMessage("E-Mail modified: " + oldEmail + " -> " + newEmail);
	}

	private void logMessage(String msg) {
		Date now = new Date();
		LogEntry logEntry = new LogEntry(msg, now);
		repository.save(logEntry);
		items.add(logEntry.toString());
	}
	
}
