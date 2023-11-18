package ru.cosmotask.calendar.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import ru.cosmotask.calendar.model.CalendarEvent;
import ru.cosmotask.model.Task;

import java.util.List;

public interface CalendarEventService {

    List<Event> getAllEventsForPeriod(DateTime start, DateTime end);

    Event saveEventToCalendar(CalendarEvent event);

    Event closeEvent(Event event);

    void deleteEvent(Event event);

    Event getEventByTaskId(Task task);
}
