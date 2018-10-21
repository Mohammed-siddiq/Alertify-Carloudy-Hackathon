package com.project.uichack.notifyme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import helper.NotificationHelper;

public class MainActivity extends AppCompatActivity {

    JSONObject responseObject;
    Double lat;
    Double lon;
    DateTimeFormatter formatter;
    NotificationHelper notificationHelper = new NotificationHelper();
    ToggleButton toggleButton;
    boolean crimeAlertsPreffered;
    boolean offerAlertsPreffered;
    boolean eventAlertsPreffered;


    BroadcastReceiver locReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            lat = intent.getDoubleExtra("Latitude", 0.0); // get latitude sent by the location services
            lon = intent.getDoubleExtra("Longitude", 0.0);
            Toast.makeText(context, "Lat: " + Double.toString(lat) + ", Long:" + Double.toString(lon), Toast.LENGTH_LONG).show();
            Log.e("REC", "Received");


            DateTime today = new DateTime().withTime(0, 0, 0, 0);//date
            formatter = ISODateTimeFormat.dateHourMinuteSecond();
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url;

            // only if user has preffered crime alerts notify him
            if (crimeAlertsPreffered) {
                url = generateRequest(lat, lon, formatter.print(today), formatter.print(today.minusDays(30)), 1000);
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    responseObject = new JSONObject(new JSONArray(response).get(0).toString());
                                    if (Integer.parseInt(responseObject.getString("count_case_number")) > getResources().getInteger(R.integer.crime_threshold)) {
                                        Log.d("crime", "Crime prone area");
                                        notificationHelper.notifyAndroid(getApplicationContext(), "{" +
                                                "txt:" +
                                                "{" +
                                                "tx1:" +
                                                "{id:342,tx:\"Caution : Crime Prone Area !\"}," +
                                                "tx2:" +
                                                "{id:343,tx:\"You are in a crime prone area!! Be Safe !!! \",s:32,x:550,y:450,w:300,h:400}," +
                                                "tx3:" +
                                                "{id:345,tx:\"\"}" +
                                                "}," +
                                                "img:" +
                                                "{id:1}" +
                                                "}", "[update]");
                                        //Logic to remove the Notififcation
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("responseJson", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                queue.add(stringRequest1);
            }


            // Event Notification

            if (eventAlertsPreffered) {
                url = generateEventUrl(10);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {

                                    responseObject = new JSONObject(response);
                                    JSONObject event = responseObject.getJSONObject("events").getJSONArray("event").getJSONObject(0);
                                    String message1 = event.getString("title") + event.getString("venue_address");
                                    event = responseObject.getJSONObject("events").getJSONArray("event").getJSONObject(1);
                                    String message2 = event.getString("title") + event.getString("venue_address");
                                    Log.d("", "");
                                    notificationHelper.notifyAndroid(getApplicationContext(), "{" +
                                            "txt:" +
                                            "{" +
                                            "tx1:" +
                                            "{id:321,tx:\" Hey there might be interesting events Nearby !\"}," +
                                            "tx2:" +
                                            "{id:322,tx:\"" + message1 + "\",s:32,x:550,y:450,w:300,h:400}," +
                                            "tx3:" +
                                            "{id:323,tx:\"" + message2 + "}" +
                                            "}," +
                                            "img:" +
                                            "{id:1}" +
                                            "}", "[update]");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("responseJson", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


                queue.add(stringRequest);
            }


        }

    };
    private IntentFilter myFilter = new IntentFilter("android.intent.action.MAIN");
    private String userPreference;

    // Generate  event api request
    private String generateEventUrl(int distance) {
        return "http://api.eventful.com/json/events/search?app_key=" + getResources().getString(R.string.app_key) + "&keywords=" + userPreference + "&where=" + lat + "," + lon + "&within=" + distance + "&date=today";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(locReceiver, myFilter); // receiver for the locations
        JodaTimeAndroid.init(this);
        startService(new Intent(this, MyLocationService.class));
        setContentView(R.layout.activity_main);

        //Load the existing preferences
        loadPreferences();

        toggleButton = findViewById(R.id.CrimeAlerts);
        toggleButton.setChecked(crimeAlertsPreffered);

        //Change preference
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //persist the new user preference
                    updatePreference(getResources().getString(R.string.crimePrefrences), true);
                    crimeAlertsPreffered = true;// update for further class

                } else {
                    // The toggle is disabled
                    updatePreference(getResources().getString(R.string.crimePrefrences), false);
                    crimeAlertsPreffered = false;
                }
            }
        });

        toggleButton = findViewById(R.id.EventAlerts);
        toggleButton.setChecked(eventAlertsPreffered);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updatePreference(getResources().getString(R.string.eventPreferences), true);
                    eventAlertsPreffered = true;

                } else {
                    // The toggle is disabled
                    updatePreference(getResources().getString(R.string.eventPreferences), false);
                    eventAlertsPreffered = false;
                }
            }
        });

        toggleButton = findViewById(R.id.offerAlerts);
        toggleButton.setChecked(offerAlertsPreffered);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updatePreference(getResources().getString(R.string.offerPreferences), true);
                    offerAlertsPreffered = true;

                } else {
                    // The toggle is disabled
                    updatePreference(getResources().getString(R.string.offerPreferences), false);
                    offerAlertsPreffered = false;
                }
            }
        });


        int randomIndex = (int) Math.random() * 4;

        userPreference = getResources().getStringArray(R.array.user_preferences)[randomIndex];

    }

    private void loadPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        crimeAlertsPreffered = sharedPref.getBoolean(getResources().getString(R.string.crimePrefrences), false);
        eventAlertsPreffered = sharedPref.getBoolean(getResources().getString(R.string.eventPreferences), false);
        offerAlertsPreffered = sharedPref.getBoolean(getResources().getString(R.string.offerPreferences), false);

    }

    private void updatePreference(String preference, boolean preferenced) {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preference, preferenced);
        editor.commit();


    }

    // unregister locations when paused
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locReceiver);
    }

    private String generateRequest(Double destinationLat, Double destinationLon, String startDate, String endDate, int distance) {
        String url = "https://data.cityofchicago.org/resource/6zsd-86xi.json?";
        url += "$select=count(case_number)&$where=((within_circle(location," + destinationLat + "," + destinationLon + "," + distance + "))" + "AND(date between '" + endDate + "' and '" + startDate + "'))";
        return url;
    }
}
