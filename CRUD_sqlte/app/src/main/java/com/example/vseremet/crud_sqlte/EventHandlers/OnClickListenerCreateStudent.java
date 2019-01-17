package com.example.vseremet.crud_sqlte.EventHandlers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vseremet.crud_sqlte.Activities.MainActivity;
import com.example.vseremet.crud_sqlte.Models.ObjectStudent;
import com.example.vseremet.crud_sqlte.R;
import com.example.vseremet.crud_sqlte.Persistance.TableControllerStudent;

public class OnClickListenerCreateStudent implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        final Context rootContext = v.getContext();

        LayoutInflater layoutInflater = (LayoutInflater) rootContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = layoutInflater.inflate(R.layout.student_input_form, null, false);

        final EditText studentFirstnameET = (EditText) formElementsView.findViewById(R.id.studentFirstnameET);
        final EditText studentEmailET = (EditText) formElementsView.findViewById(R.id.studentEmailET);

        new AlertDialog.Builder(rootContext)
                .setView(formElementsView)
                .setTitle("Create Student")
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ObjectStudent objectStudent = new ObjectStudent();
                                objectStudent.firstName = studentFirstnameET.getText().toString();
                                objectStudent.email = studentEmailET.getText().toString();

                                boolean createSuccessful = new TableControllerStudent(rootContext).create(objectStudent);

                                if(createSuccessful){
                                    Toast.makeText(rootContext, "Student \"" + objectStudent.firstName + "\" was saved in the DataBase.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(rootContext, "Unable to save student information.", Toast.LENGTH_SHORT).show();
                                }

                                ((MainActivity) rootContext).countRecords();
                                ((MainActivity) rootContext).readRecords();

                                dialog.cancel();
                            }

                        }).show();
    }
}
