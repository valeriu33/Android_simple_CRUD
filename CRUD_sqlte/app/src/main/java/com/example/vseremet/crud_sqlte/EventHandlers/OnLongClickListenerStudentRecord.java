package com.example.vseremet.crud_sqlte.EventHandlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vseremet.crud_sqlte.Activities.MainActivity;
import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;

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
                    public void onClick(DialogInterface dialog, int item) {

                        if (item == 0) {
                            editRecord(Integer.parseInt(id));
                        }

                        else if (item == 1) {

                            boolean deleteSuccessful = new TableControllerStudent(context).delete(Integer.parseInt(id));

                            if (deleteSuccessful){
                                Toast.makeText(context, "Student record was deleted.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Unable to delete student record.", Toast.LENGTH_SHORT).show();
                            }

                            ((MainActivity) context).countRecords();
                            ((MainActivity) context).readRecords();

                        }

                        dialog.dismiss();

                    }
                }).show();

        return false;
    }

    public void editRecord(final int studentId) {
        final TableControllerStudent tableControllerStudent = new TableControllerStudent(context);
        ObjectStudent objectStudent = tableControllerStudent.readSingleRecord(studentId);

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

                                ObjectStudent objectStudent = new ObjectStudent();

                                String userName = studentFirstnameET.getText().toString();
                                String email = studentFirstnameET.getText().toString();

                                objectStudent.setId(studentId);

                                if (!objectStudent.trySetUserName(userName)) {
                                    Toast.makeText(context, "Username is too short.", Toast.LENGTH_SHORT).show();
                                    updateSuccessful = false;
                                }

                                if (!objectStudent.trySetEmail(email)) {
                                    Toast.makeText(context, "This is not a valid email", Toast.LENGTH_SHORT).show();
                                    updateSuccessful = false;
                                }

                                if (!tableControllerStudent.update(objectStudent)) {
                                    Toast.makeText(context, "Unable to update student record.", Toast.LENGTH_SHORT).show();
                                    updateSuccessful = false;
                                }

                                if (updateSuccessful) {
                                    Toast.makeText(context, "Student record was updated.", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) context).countRecords();
                                    ((MainActivity) context).readRecords();
                                }

                                dialog.dismiss();
                            }
                        });
                    }
                });
    }
}
