package ru.cosmotask.calendar.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cosmotask.exception.CalendarException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Configuration
public class CalendarEventConfig {

    public final String applicationName = "CosmotaskClient2";
    public final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private final List<String> scopes =
            List.of(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR);

    @Bean
    public Calendar getCalendarService() {
        Calendar calendar;
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            calendar = new Calendar.Builder(httpTransport, jsonFactory, getCredentials(httpTransport))
                    .setApplicationName(applicationName)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            log.error("An Error occurred when we try to create bean Calendar");
            throw new CalendarException(e.getMessage());
        }
        return calendar;

    }

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        String credentialsFilePath = "/credentials.json";
        InputStream in = CalendarEventConfig.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        String tokensDirectoryPath = "tokens";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(tokensDirectoryPath)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
