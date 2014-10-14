package de.codecentric.javafx.conatctmanager.controller;

import java.util.List;

import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import de.codecentric.javafx.conatctmanager.model.Contact;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;


public class LogControllerTest {

	@Test
	public void shouldBindFilteredList() throws Exception {
		Storage replication = new Storage("/tmp/databaseDir",null,0);

	    IMongodConfig mongodConfig = new MongodConfigBuilder()
	            .version(Version.Main.PRODUCTION)
	            .replication(replication)
	            .net(new Net(52602, Network.localhostIsIPv6()))
	            .build();
		MongodStarter runtime = MongodStarter.getDefaultInstance();
		MongodExecutable mongodExecutable = runtime.prepare(mongodConfig);
		mongodExecutable.start();
		MongoClient mongoClient = new MongoClient("localhost", 52602);
		Morphia morphia = new Morphia();
		Datastore ds = morphia.createDatastore(mongoClient, "testDB");
		morphia.map(Contact.class);
		ds.save(new Contact("Max", "Hartmann", "maxadresse@gmail.com"));
		List<Contact> asList = ds.find(Contact.class).asList();
//		asList.forEach(c->ds.delete(c));
		System.out.println(asList.size());
	}
}
