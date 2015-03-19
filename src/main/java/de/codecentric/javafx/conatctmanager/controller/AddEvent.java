package de.codecentric.javafx.conatctmanager.controller;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * add contact event
 * 
 * @author max.hartmann
 *
 */
public class AddEvent extends Event {

    public static final long serialVersionUID = 1L;

    public final static EventType<AddEvent> ADD_EVENT = new EventType<>("add_event");

    public AddEvent() {
        super(ADD_EVENT);
    }

}
