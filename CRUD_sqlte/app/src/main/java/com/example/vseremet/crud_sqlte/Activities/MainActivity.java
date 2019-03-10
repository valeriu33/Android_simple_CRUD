package com.example.vseremet.crud_sqlte.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vseremet.crud_sqlte.APIconnection.EgovProjAPI;
import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.EventHandlers.OnClickListenerCreateStudent;
import com.example.vseremet.crud_sqlte.EventHandlers.OnLongClickListenerStudentRecord;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Button createUserBtn, makeRequestBtn;
    TextView requestResponseTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        createUserBtn = findViewById(R.id.createStudentBtn);
        makeRequestBtn = findViewById(R.id.makeRequestBtn);
        requestResponseTV = findViewById(R.id.requestResponseTV);

        createUserBtn.setOnClickListener(new OnClickListenerCreateStudent());

        makeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        StringBuffer response = new StringBuffer();

                        URL myAPIEndpoint = null;
                        try {

                            myAPIEndpoint = new URL("http://104.248.17.105:80/api/values");

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                        HttpURLConnection myConnection = null;

                        try {

                            if (myAPIEndpoint != null) {
                                myConnection = (HttpURLConnection) myAPIEndpoint.openConnection();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (myConnection != null) {
                            myConnection.setRequestProperty("User-Agent", "CRUD_sqlite-v0.1");
                            myConnection.setRequestProperty("Accept", "application/json");
                        }

                        try {

                            if (myConnection.getResponseCode() == 200) {

                                BufferedReader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                            } else {
                                // Error handling code goes here
                            }
                            requestResponseTV.setText(response);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (myConnection != null) {
                                myConnection.disconnect();
                            }
                        }

                    }
                });
            }
        });

        countRecords();
        readRecords();
    }

    public void countRecords(){
        final int[] recordCount = {0};
        final TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);

        EgovProjAPI.get("students/count", null, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                requestResponseTV.setText(throwable.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                recordCount[0] = Integer.parseInt(responseString);

                textViewRecordCount.setText(recordCount[0] + " records found.");
            }
        });
    }

    public void readRecords() {

        final LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        final List<ObjectStudent> students = new LinkedList<>();

        final Context context = this;

        EgovProjAPI.get("students", null, new JsonHttpResponseHandler(){
            @Override

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                requestResponseTV.setText(throwable.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {
                try {
                    for (int i = 0; i < responseArray.length(); i++) {
                        ObjectStudent responseStudent = new ObjectStudent();

                        responseStudent.id = Integer.parseInt(responseArray.getJSONObject(i).getString("id"));
                        responseStudent.email = responseArray.getJSONObject(i).getString("email");
                        responseStudent.userName = responseArray.getJSONObject(i).getString("name");
                        students.add(responseStudent);
                    }
                }catch (JSONException a){}

                    if (students.size() > 0) {

                        for (ObjectStudent obj : students) {

                            int id = obj.getId();
                            String studentFirstname = obj.getUserName();
                            String studentEmail = obj.getEmail();

                            String textViewContents = studentFirstname + " - " + studentEmail;

                            TextView textViewStudentItem = new TextView(context);
                            textViewStudentItem.setPadding(0, 10, 0, 10);
                            textViewStudentItem.setText(textViewContents);
                            textViewStudentItem.setTag(Integer.toString(id));

                            textViewStudentItem.setOnLongClickListener(new OnLongClickListenerStudentRecord());

                            linearLayoutRecords.addView(textViewStudentItem);
                        }

                    }

                    else {

                        TextView locationItem = new TextView(context);
                        locationItem.setPadding(8, 8, 8, 8);
                        locationItem.setText("No records yet.");

                        linearLayoutRecords.addView(locationItem);
                    }
                }
            });
        }
}

