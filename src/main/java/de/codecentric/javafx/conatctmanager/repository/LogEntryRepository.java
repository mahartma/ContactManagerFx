package de.codecentric.javafx.conatctmanager.repository;

import java.net.UnknownHostException;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import de.codecentric.javafx.conatctmanager.model.Contact;
import de.codecentric.javafx.conatctmanager.model.LogEntry;

public class LogEntryRepository {
	private static final String MONGODB_DBNAME = "testDB";
	private static final String MONGODB_SERVER = "localhost";
	private Datastore ds;
	
	public LogEntryRepository() {
		try {
			configureMorphia();
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
	
	public void save(LogEntry entry) {
		ds.save(entry);
	}

	public List<LogEntry> readAll() {
		return ds.find(LogEntry.class).asList();
	}
}
