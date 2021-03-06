package de.codecentric.javafx.conatctmanager.guitest;

import java.awt.AWTException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jemmy.Point;
import org.jemmy.fx.AppExecutor;
import org.jemmy.fx.Browser;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.CheckBoxDock;
import org.jemmy.fx.control.CheckBoxWrap;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.fx.control.TabDock;
import org.jemmy.fx.control.TabPaneDock;
import org.jemmy.fx.control.TableViewDock;
import org.junit.Before;
import org.junit.Test;

import de.codecentric.javafx.conatctmanager.App;

@SuppressWarnings("unchecked")
public class ContactManagerGuiTest {
	
	private SceneDock appScene;
	private static AtomicBoolean started = new AtomicBoolean(false);
	
	/*
	 * start the browser to inspect something
	 */
	public static void main(String[] args) throws AWTException {
		AppExecutor.executeNoBlock(App.class);
		Browser.runBrowser();
	}
	
	@Before
	public void setUp() {
		if (!started.getAndSet(true)) {
			AppExecutor.executeNoBlock(App.class);
		}
		appScene = new SceneDock();
	}
	
	@Test
	public void shouldAddNewCsontact() throws InterruptedException {
		new LabeledDock(appScene.asParent(), "addButton").mouse().click();
		Thread.sleep(2000);
	}

	@Test
	public void shouldRemoveContact() throws InterruptedException {
		new LabeledDock(appScene.asParent(), "addButton").mouse().click();
		new TableViewDock(appScene.asParent()).asTable().select(new Point(0,1));
		new LabeledDock(appScene.asParent(), "removeButton").mouse().click();
		
		Thread.sleep(2000);
	}

	@Test
	public void shouldCreateLogEntry() throws InterruptedException {
		new LabeledDock(appScene.asParent(), "addButton").mouse().click();
		new TableViewDock(appScene.asParent()).asTable().select(new Point(0,1));
		new LabeledDock(appScene.asParent(), "removeButton").mouse().click();
		
		TabPaneDock tabPaneDock = new TabPaneDock(appScene.asParent());
		TabDock tabDock = new TabDock(tabPaneDock.asTabParent(), "tabLog");
		tabPaneDock.selector().select(tabDock.control());
		
		Thread.sleep(2000);
		
		tabDock = new TabDock(tabPaneDock.asTabParent(), "tabContacts");
		tabPaneDock.selector().select(tabDock.control());		
	
		Thread.sleep(2000);
	}

	@Test
	public void shouldFilterLogEntries() throws InterruptedException {
		new LabeledDock(appScene.asParent(), "addButton").mouse().click();
		new TableViewDock(appScene.asParent()).asTable().select(new Point(0,1));
		new LabeledDock(appScene.asParent(), "removeButton").mouse().click();
		
		TabPaneDock tabPaneDock = new TabPaneDock(appScene.asParent());
		TabDock tabDock = new TabDock(tabPaneDock.asTabParent(), "tabLog");
		tabPaneDock.selector().select(tabDock.control());
		
		new CheckBoxDock(appScene.asParent(), "modified").selector().select(CheckBoxWrap.State.CHECKED);
		
		Thread.sleep(2000);
		
		tabDock = new TabDock(tabPaneDock.asTabParent(), "tabContacts");
		tabPaneDock.selector().select(tabDock.control());		
		
		Thread.sleep(2000);
	}
}
