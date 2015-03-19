package de.codecentric.javafx.conatctmanager;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import com.google.inject.Injector;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class App extends Application {

    private static final String TABS_FXML = "de/codecentric/javafx/conatctmanager/view/Tabs.fxml";
    private static final String MONGODB_PATH = "/tmp/databaseDir";
    private Injector injector = createInjector();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        startMongo();

        primaryStage.setTitle("Contact-Manager");

        TabPane root = FXMLLoader.load(getClass().getClassLoader()
                .getResource(TABS_FXML),
                null,
                new JavaFXBuilderFactory(),
                clazz -> injector.getInstance(clazz));

        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void startMongo() throws UnknownHostException, IOException {
        Storage replication = new Storage(MONGODB_PATH, null, 0);
        IMongodConfig mongodConfig;
        mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .replication(replication)
                .net(new Net(52602, Network.localhostIsIPv6()))
                .build();
        MongodStarter runtime = MongodStarter.getDefaultInstance();
        MongodExecutable mongodExecutable = runtime.prepare(mongodConfig);
        mongodExecutable.start();
    }
}
