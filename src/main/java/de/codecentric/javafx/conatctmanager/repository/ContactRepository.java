package de.codecentric.javafx.conatctmanager.repository;

import static javafx.collections.FXCollections.observableArrayList;

import java.net.UnknownHostException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import javax.inject.Singleton;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;

import de.codecentric.javafx.conatctmanager.model.Contact;

@Singleton
public class ContactRepository {

    private static final String MONGODB_DBNAME = "testDB";
    private static final String MONGODB_SERVER = "localhost";
    private Datastore ds;
    private ObservableList<Contact> data;

    public ContactRepository() {
        try {
            configureMorphia();
            readData();
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

    public void updateEmail(Contact contact, String newEmail) {
        Query<Contact> updateQuery = ds.createQuery(Contact.class).field(Mapper.ID_KEY).equal(contact.getId());
        UpdateOperations<Contact> ops = ds.createUpdateOperations(Contact.class).set("email", new SimpleStringProperty(newEmail));
        ds.update(updateQuery, ops);
    }

    private void readData() {
        this.data = observableArrayList(ds.find(Contact.class).asList());
    }

    public ObservableList<Contact> getData() {
        return this.data;
    }

    public void save(Contact newContact) {
        ds.save(newContact);
    }

    public void delete(Contact removedContact) {
        ds.delete(removedContact);
    }
}
