package com.example.ntlmewsexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import java.net.URI;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.core.service.item.Appointment;

public class MainActivity extends AppCompatActivity {

    private static final String EWS_URL = "https://ews.provizit.com/EWS/Exchange.asmx";
    private static final String USERNAME = "serviceuser@provizit.com";
    private static final String PASSWORD = "Skhan@123";
    private static final String DOMAIN = "provizit.com";

    private TextView calendarDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarDataTextView = findViewById(R.id.calendar_data);
        calendarDataTextView.setText("Fetching calendar data...");

        new EwsTask().execute();
    }

    private class EwsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                ExchangeService service = new ExchangeService();
                service.setCredentials(new WebCredentials(USERNAME, PASSWORD, DOMAIN));
                service.setUrl(new URI(EWS_URL));

                CalendarFolder cf = CalendarFolder.bind(service, WellKnownFolderName.Calendar);
                FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(java.util.Calendar.getInstance().getTime(), new java.util.Date(java.util.Calendar.getInstance().getTime().getTime() + 86400000)));

                StringBuilder sb = new StringBuilder();
                if (findResults.getTotalCount() == 0) {
                    return "No appointments found for the next 24 hours.";
                }

                for (Appointment appt : findResults.getItems()) {
                    sb.append("Subject: ").append(appt.getSubject()).append("\n");
                    sb.append("Start: ").append(appt.getStart()).append("\n");
                    sb.append("End: ").append(appt.getEnd()).append("\n\n");
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            calendarDataTextView.setText(result);
        }
    }
}
