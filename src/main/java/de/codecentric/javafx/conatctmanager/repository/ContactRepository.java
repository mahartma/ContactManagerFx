package de.codecentric.javafx.conatctmanager.repository;

import static javafx.collections.FXCollections.observableArrayList;

import java.net.UnknownHostException;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import de.codecentric.javafx.conatctmanager.model.Contact;

public class ContactRepository {

	private static final String MONGODB_DBNAME = "testDB";
	private static final String MONGODB_SERVER = "localhost";
	private Datastore ds;
	private ObservableList<Contact> data;

	public ContactRepository() {
		try {
			configureMorphia();
			readData();
			configureEvents();
		} catch (UnknownHostException e) {
			throw new RuntimeException("error connecting to mongodb instance", e);
		}
	}

	private void configureMorphia() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(MONGODB_SERVER, 52602);
		Morphia morphia = new Morphia();
		ds = morphia.createDatastore(mongoClient, MONGODB_DBNAME);
		morphia.map(Contact.class);
	}

	private void configureEvents() {
		data.addListener((ListChangeListener.Change<? extends Contact> change) -> {
			while(change.next()) {
				for(Contact newContact : change.getAddedSubList()) {
					ds.save(newContact);
				}
				for(Contact removedContact : change.getRemoved()) {
					ds.delete(removedContact);
				}
			}
		});
	}

	private void readData() {
		this.data = observableArrayList(ds.find(Contact.class).asList());
	}
	
	public ObservableList<Contact> getData() {
		return this.data;
	}
}
