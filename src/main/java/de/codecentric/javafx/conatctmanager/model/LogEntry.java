package de.codecentric.javafx.conatctmanager.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class LogEntry {
	@Id 
	private ObjectId id;
	
    private SimpleStringProperty message;
    private Date createdAt;
    
    public LogEntry() {
		super();
	}

	public LogEntry(String msg, Date createdAt) {
    	this.message = new SimpleStringProperty(msg);
    	this.createdAt = createdAt;
    }

	public String getMessage() {
		return message.get();
	}
    
    public void setMessage(String message) {
		this.message.set(message);
	}
    
    public Date getCreatedAt() {
		return createdAt;
	}
    
    public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
    
    @Override
    public String toString() {
    	return new SimpleDateFormat("[dd.MM.yyyy HH:mm:ss] ").format(this.createdAt) 
				+ this.message.get();
    }
}
