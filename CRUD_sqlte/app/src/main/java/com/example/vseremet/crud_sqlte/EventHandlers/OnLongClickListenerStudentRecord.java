package com.example.vseremet.crud_sqlte.EventHandlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vseremet.crud_sqlte.APIconnection.EgovProjAPI;
import com.example.vseremet.crud_sqlte.Activities.MainActivity;
import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OnLongClickListenerStudentRecord implements View.OnLongClickListener {

    Context context;
    String id;

    @Override
    public boolean onLongClick(View v) {

        context = v.getContext();
        id = v.getTag().toString();

        final CharSequence[] items = { "Edit", "Delete" };

        new AlertDialog.Builder(context).setTitle("Student Record")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int item) {

                        if (item == 0) {
                            editRecord(Integer.parseInt(id));
                        }

                        else if (item == 1) {

                            EgovProjAPI.delete("students/"+Integer.parseInt(id), null, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    Toast.makeText(context, "Student record was deleted.", Toast.LENGTH_SHORT).show();

                                    ((MainActivity) context).countRecords();
                                    ((MainActivity) context).readRecords();

                                    dialog.dismiss();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(context, "Unable to delete student record.", Toast.LENGTH_SHORT).show();


                                    ((MainActivity) context).countRecords();
                                    ((MainActivity) context).readRecords();

                                    dialog.dismiss();
                                }
                            });
                        }


                    }
                }).show();

        return false;
    }

    public void editRecord(final int studentId) {
        final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);
        final ObjectStudent objectStudent = new ObjectStudent();

        EgovProjAPI.get("students/" + studentId, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseObject) {
                try {
                    objectStudent.id = Integer.parseInt(responseObject.getString("id"));
                    objectStudent.email = responseObject.getString("email");
                    objectStudent.userName = responseObject.getString("name");

                }catch (JSONException a){}

                //-----

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.student_input_form, null, false);

                final EditText studentFirstnameET = (EditText) formElementsView.findViewById(R.id.studentFirstnameET);
                final EditText studentEmailET = (EditText) formElementsView.findViewById(R.id.studentEmailET);

                studentFirstnameET.setText(objectStudent.getUserName());
                studentEmailET.setText(objectStudent.getEmail());

                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Edit Record")
                        .setPositiveButton("Save Changes", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.setOnShowListener(
                        new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialogInterface) {

                                Button okButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                okButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        boolean updateSuccessful = true;

                                        ObjectStudent newobjectStudent = new ObjectStudent();

                                        String userName = studentFirstnameET.getText().toString();
                                        String email = studentEmailET.getText().toString();

                                        newobjectStudent.setId(studentId);

                                        if (!newobjectStudent.trySetUserName(userName)) {
                                            Toast.makeText(context, "Username is too short.", Toast.LENGTH_SHORT).show();
                                            updateSuccessful = false;
                                        }

                                        if (!newobjectStudent.trySetEmail(email)) {
                                            Toast.makeText(context, "This is not a valid email", Toast.LENGTH_SHORT).show();
                                            updateSuccessful = false;
                                        }

                                        //if (createSuccessful[0]) {
                                        try {
                                            JSONObject jsonParams = new JSONObject();
                                            jsonParams.put("ID", newobjectStudent.getId());
                                            jsonParams.put("name", newobjectStudent.getUserName());
                                            jsonParams.put("email", newobjectStudent.getEmail());
                                            StringEntity entity = new StringEntity(jsonParams.toString());

                                            EgovProjAPI.post(context, "students", entity, "application/json", new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    //if (createSuccessful[0]) {
                                                    Toast.makeText(context, "Student record was updated.", Toast.LENGTH_SHORT).show();

                                                    ((MainActivity) context).countRecords();
                                                    ((MainActivity) context).readRecords();

                                                    dialog.dismiss();
                                                    //}
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                    Toast.makeText(context, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                                                    //createSuccessful[0] = false;
                                                }
                                            });


                                        }catch (Exception e){}
                                        //}

                                    }
                                });
                            }
                        });
                dialog.show();

                //-----
            }
        });


    }
}
