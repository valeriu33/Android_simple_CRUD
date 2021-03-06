package com.example.vseremet.crud_sqlte.EventHandlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vseremet.crud_sqlte.APIconnection.EgovProjAPI;
import com.example.vseremet.crud_sqlte.Activities.MainActivity;
import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OnClickListenerCreateStudent implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        final Context rootContext = v.getContext();

        LayoutInflater layoutInflater = (LayoutInflater) rootContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = layoutInflater.inflate(R.layout.student_input_form, null, false);

        final EditText studentFirstnameET = (EditText) formElementsView.findViewById(R.id.studentFirstnameET);
        final EditText studentEmailET = (EditText) formElementsView.findViewById(R.id.studentEmailET);



        final AlertDialog dialog = new AlertDialog.Builder(rootContext)
                .setView(formElementsView)
                .setTitle("Create Student")
//                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final boolean[] createSuccessful = {true};

                        String userName = studentFirstnameET.getText().toString();
                        String email = studentEmailET.getText().toString();

                        ObjectStudent objectStudent = new ObjectStudent();
                        if (!objectStudent.trySetUserName(userName)) {
                            Toast.makeText(rootContext, "Username is too short.", Toast.LENGTH_SHORT).show();
                            createSuccessful[0] = false;
                        }

                        if (!objectStudent.trySetEmail(email)) {
                            Toast.makeText(rootContext, "This is not a valid email", Toast.LENGTH_SHORT).show();
                            createSuccessful[0] = false;
                        }

//                                if(!objectStudent.trySetUserName(password)){
//                                    Toast.makeText(rootContext, "Password must have at least: six of more " +
//                                            "characters, one uppercase letter, one lowercase letter and one number", Toast.LENGTH_LONG).show();
//                                }

                        if (createSuccessful[0]) {
                            try {
                                JSONObject jsonParams = new JSONObject();
                                jsonParams.put("name", objectStudent.getUserName());
                                jsonParams.put("email", objectStudent.getEmail());
                                StringEntity entity = new StringEntity(jsonParams.toString());

                                EgovProjAPI.put(rootContext, "students", entity, "application/json", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        if (createSuccessful[0]) {
                                            Toast.makeText(rootContext, "Student \""// + objectStudent.getUserName()
                                                    + "\" was saved in the DataBase.", Toast.LENGTH_SHORT).show();

                                            ((MainActivity) rootContext).countRecords();
                                            ((MainActivity) rootContext).readRecords();

                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Toast.makeText(rootContext, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                                            createSuccessful[0] = false;
                                    }
                                });


                            }catch (Exception e){}
                        }


                    }
                });
            }

        });
        dialog.show();
    }
}
