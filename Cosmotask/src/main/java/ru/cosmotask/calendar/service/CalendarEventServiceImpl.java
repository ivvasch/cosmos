package ru.cosmotask.calendar.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.cosmotask.calendar.config.CalendarEventConfig;
import ru.cosmotask.calendar.model.CalendarEvent;
import ru.cosmotask.exception.CalendarException;
import ru.cosmotask.model.Task;
import ru.cosmotask.utils.DateFormatUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class CalendarEventServiceImpl implements CalendarEventService {

    public static final String CALENDAR_ID = "primary";
    private final Calendar service;

    public CalendarEventServiceImpl(CalendarEventConfig calendarConfig) {
        service = calendarConfig.getCalendarService();
    }

    @Override
    public List<Event> getAllEventsForPeriod(DateTime start, DateTime end) {
        Events events;
        try {
            events = service.events().list(CALENDAR_ID)
                    .setTimeMin(start)
                    .setTimeMax(end)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            throw new CalendarException(e.getMessage());
        }
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            log.info("No upcoming events found.");
        } else {
            log.info("We have upcoming events. Cont of events are {}", items.size());
        }
        return items;
    }

    @Override
    public Event saveEventToCalendar(CalendarEvent calendarEvent) {
        LocalDateTime startDate = calendarEvent.getTask().getStartDate();
        LocalDateTime endDate = calendarEvent.getEndDate();
        EventDateTime start = getEventDateTime(startDate);
        EventDateTime end = getEventDateTime(endDate);
        Event event = calendarEvent.getEvent();
        event.setStart(start);
        event.setEnd(end);
        Event.Reminders reminders = getReminders();
        event.setReminders(reminders);
        event.setId(calendarEvent.getTask().getId());
        Event executed;
        try {
            executed = service.events().insert(CALENDAR_ID, event).execute();
        } catch (IOException e) {
            log.error("Error occurred when we trying to save event from task {} to calendar", calendarEvent.getTask());
            throw new CalendarException(e.getMessage());
        }
        return executed;
    }

    private static Event.Reminders getReminders() {
        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        return new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
    }
    private static EventDateTime getEventDateTime(LocalDateTime localDateTime) {
        DateTime dateTime = DateFormatUtil.localDateTimeToDateTimeGoogleFormat(localDateTime);
        return new EventDateTime()
                .setDateTime(dateTime)
                .setTimeZone("Europe/Moscow");
    }

    @Override
    public Event closeEvent(Event event) {
        return new Event();
    }

    @Override
    public void deleteEvent(Event event) {
        if (event == null) {
            throw new CalendarException("Event is null, nothing to delete.");
        }
        try {
            service.events().delete(CALENDAR_ID, event.getId()).execute();
        } catch (IOException e) {
            log.error("Something wrong when we try to delete event with id {} from calendar {}. Cause is {}.", event.getId(), CALENDAR_ID, e.getCause());
            throw new CalendarException("Something wrong when we try to delete event");
        }
    }

    @Override
    public Event getEventByTaskId(Task task) {
        Event first = null;
        if (task != null) {
            DateTime start = DateFormatUtil.localDateTimeToDateTimeGoogleFormat(task.getStartDate());
            DateTime end = DateFormatUtil.localDateTimeToDateTimeGoogleFormat(task.getStartDate().plusDays(1));
            List<Event> allEventsForPeriod = getAllEventsForPeriod(start, end);
            first = allEventsForPeriod.stream().filter(event -> event.getId().equals(task.getId())).findFirst().orElse(null);
        }
        return first;
    }
}
