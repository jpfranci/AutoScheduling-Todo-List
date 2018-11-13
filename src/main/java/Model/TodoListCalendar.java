package Model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import observer.CalendarObserver;
import observer.Subject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class TodoListCalendar extends Subject{
    private static final String APPLICATION_NAME = "TodoList AutoScheduler";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    // set to 7 days by default
    public static final long RANGE_TO_SET_ACTIVITIES = 604800000;
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final LocalTime MIN_SCHEDULE_TIME = LocalTime.of(10,0);
    private static final LocalTime MAX_SCHEDULE_TIME = LocalTime.of(22,0);
    private static final String DATE_TIME_ENDING = ":00.000-08:00";

    private Calendar calendar;
    private ArrayList<LocalDateTime> localDateTimeStarts = new ArrayList<>();
    private ArrayList<LocalDateTime> localDateTimeEnds = new ArrayList<>();
    public ArrayList<String> events = new ArrayList<>();

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = TodoListCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void tryToScheduleEntries(Map<TodoListEntryActivity, TodoListEntry> todoListEntryMap) {
        Collection<TodoListEntry> todoListEntries = todoListEntryMap.values();
        getEntries();

        LocalDateTime maxDateTime = LocalDateTime.of(LocalDate.now().plusDays(7), MAX_SCHEDULE_TIME);
        int sizeListScheduledBefore = localDateTimeEnds.size();

        scheduleActivities(todoListEntries, maxDateTime);

        if (localDateTimeStarts.size() > sizeListScheduledBefore) {
            notifyObserver(calendar, getTimeAtMidnight());
        }
    }

    private void scheduleActivities(Collection<TodoListEntry> todoListEntries, LocalDateTime maxDateTime) {
        for (TodoListEntry entry : todoListEntries) {
            if (!events.contains(entry.getTodoListEntryActivity().getActivity())) {
                double timeToSchedule = entry.getTime();
                long minutesToAdd = (long) (timeToSchedule * 60);

                LocalDateTime toSchedule = LocalDateTime.of(LocalDate.now(), MIN_SCHEDULE_TIME);
                LocalDateTime maxTimeToday = LocalDateTime.of(LocalDate.now(), MAX_SCHEDULE_TIME);
                LocalDateTime trialDate = toSchedule.plusMinutes(minutesToAdd);

                boolean isScheduled = false;
                for (int indexToAccess = 0, days = 0, sizeList = localDateTimeStarts.size();
                     !isScheduled && toSchedule.isBefore(maxDateTime);) {

                    while (!isScheduled && indexToAccess < sizeList && toSchedule.isBefore(maxTimeToday)) {
                        try {
                            isScheduled = tryToScheduleActivity(toSchedule, trialDate, entry, indexToAccess);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        toSchedule = localDateTimeEnds.get(indexToAccess);
                        trialDate = toSchedule.plusMinutes(minutesToAdd);
                        indexToAccess++;
                    }
                    if (indexToAccess == sizeList) {
                        toSchedule = incrementDateIfNeeded(toSchedule, maxTimeToday, days);

                        try {
                            scheduleEventAndAddToLists(toSchedule, entry, indexToAccess,
                                    toSchedule.plusMinutes(minutesToAdd));
                            isScheduled = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (!isScheduled) {
                        days++;
                        toSchedule = LocalDateTime.of(LocalDate.now().plusDays(days), MIN_SCHEDULE_TIME);
                        maxTimeToday = maxTimeToday.plusDays(1);
                    }
                }
            }
        }
    }

    private LocalDateTime incrementDateIfNeeded(LocalDateTime toSchedule, LocalDateTime maxTimeToday, int days) {
        if (!toSchedule.isBefore(maxTimeToday)) {
            toSchedule = LocalDateTime.of(LocalDate.now().plusDays(days + 1), MIN_SCHEDULE_TIME);
        }
        return toSchedule;
    }

    private boolean tryToScheduleActivity(LocalDateTime toSchedule, LocalDateTime trialDate,
                                          TodoListEntry entry,
                                          int indexToAccess) throws IOException {
        if (toSchedule.isBefore(localDateTimeStarts.get(indexToAccess)) &&
                trialDate.isBefore(localDateTimeEnds.get(indexToAccess))) {
            scheduleEventAndAddToLists(toSchedule, entry, indexToAccess, trialDate);
            return true;
        }
        return false;
    }

    private void scheduleEventAndAddToLists(LocalDateTime toSchedule,
                                            TodoListEntry entry,
                                            int indexToAccess, LocalDateTime trialDate) throws IOException {
        scheduleEvent(toSchedule, entry, trialDate);
        localDateTimeStarts.add(indexToAccess, toSchedule);
        localDateTimeEnds.add(indexToAccess, trialDate);
        events.add(entry.getTodoListEntryActivity().getActivity());
    }

    private void scheduleEvent(LocalDateTime toSchedule, TodoListEntry entry, LocalDateTime trialDate) throws IOException {
        Event event = new Event().setSummary(entry.getTodoListEntryActivity().getActivity());

        DateTime startDateTime = new DateTime(LocalDateTimeToDateTimeString(toSchedule));
        EventDateTime start = createNewDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(LocalDateTimeToDateTimeString(trialDate));
        EventDateTime end = createNewDateTime(endDateTime);
        event.setEnd(end);

        calendar.events().insert("primary", event).execute();
    }

    private EventDateTime createNewDateTime(DateTime startDateTime) {
        return new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
    }

    private String LocalDateTimeToDateTimeString(LocalDateTime trialDate) {
        return trialDate.toString().concat(DATE_TIME_ENDING);
    }

    private void getEntries() {
        String pageToken = null;
        try {
            do {
                Events events = null;
                events = getEvents(pageToken);

                List<Event> items = events.getItems();
                getLocalDatesOfAllEntries(items);
                pageToken = events.getNextPageToken();
            } while (pageToken != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocalDatesOfAllEntries(List<Event> items) {
        for (Event event : items) {
            String summary = event.getSummary();
            if (!events.contains(summary)) {
                localDateTimeStarts.add
                        (LocalDateTime.parse(event.getStart().getDateTime()
                                .toString().split("[.]")[0]));
                localDateTimeEnds.add(LocalDateTime.parse(event.getEnd().getDateTime()
                        .toString().split("[.]")[0]));
                events.add(summary);
                Collections.sort(localDateTimeEnds);
                Collections.sort(localDateTimeStarts);
            }
        }
    }

    private long getTimeAtMidnight() {
        LocalDate date = LocalDate.now();
        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch = date.atStartOfDay(zoneId).toEpochSecond();
        return epoch * 1000;
    }

    private Events getEvents(String pageToken) throws IOException {
        Events events;
        long curTime = getTimeAtMidnight();
        long timeEnd = curTime + RANGE_TO_SET_ACTIVITIES;

        DateTime timeMin = new DateTime(curTime);
        DateTime timeMax = new DateTime(timeEnd);

        events = calendar.events().list("primary").setPageToken(pageToken)
                .setTimeMin(timeMin).setTimeMax(timeMax).execute();

        return events;
    }

    public TodoListCalendar(CalendarObserver calendarObserver) {
        try {
            System.out.println("Please enter your credentials for your google account to link to your TodoList");

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

        } catch (IOException e) {
            System.out.println("Calendar couldn't be successfully loaded!");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } finally {
            addObserver(calendarObserver);
        }
    }
}
