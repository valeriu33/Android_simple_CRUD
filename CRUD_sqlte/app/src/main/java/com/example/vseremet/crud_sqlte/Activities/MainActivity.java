package com.example.vseremet.crud_sqlte.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.EventHandlers.OnClickListenerCreateStudent;
import com.example.vseremet.crud_sqlte.EventHandlers.OnLongClickListenerStudentRecord;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button createUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createUserBtn = findViewById(R.id.createStudentBtn);

        createUserBtn.setOnClickListener(new OnClickListenerCreateStudent());

        countRecords();
        readRecords();
    }

    public void countRecords(){
        int recordCount = new TableControllerStudent(this).count();
        TextView textViewRecordCount = (TextView) findViewById(R.id.textViewRecordCount);
        textViewRecordCount.setText(recordCount + " records found.");
    }

    public void readRecords() {

        LinearLayout linearLayoutRecords = (LinearLayout) findViewById(R.id.linearLayoutRecords);
        linearLayoutRecords.removeAllViews();

        List<ObjectStudent> students = new TableControllerStudent(this).read();

        if (students.size() > 0) {

            for (ObjectStudent obj : students) {

                int id = obj.id;
                String studentFirstname = obj.firstName;
                String studentEmail = obj.email;

                String textViewContents = studentFirstname + " - " + studentEmail;

                TextView textViewStudentItem = new TextView(this);
                textViewStudentItem.setPadding(0, 10, 0, 10);
                textViewStudentItem.setText(textViewContents);
                textViewStudentItem.setTag(Integer.toString(id));

                textViewStudentItem.setOnLongClickListener(new OnLongClickListenerStudentRecord());

                linearLayoutRecords.addView(textViewStudentItem);
            }

        }

        else {

            TextView locationItem = new TextView(this);
            locationItem.setPadding(8, 8, 8, 8);
            locationItem.setText("No records yet.");

            linearLayoutRecords.addView(locationItem);
        }

    }
}
