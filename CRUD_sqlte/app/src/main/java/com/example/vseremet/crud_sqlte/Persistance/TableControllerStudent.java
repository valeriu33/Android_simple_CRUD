package com.example.vseremet.crud_sqlte.Persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vseremet.crud_sqlte.Models.ObjectStudent;

import java.util.List;

public class TableControllerStudent extends DatabaseHandler {
    public TableControllerStudent(Context context) {
        super(context);
    }

    public boolean create(ObjectStudent objectStudent) {

        ContentValues values = new ContentValues();

        values.put("firstname", objectStudent.getUserName());
        values.put("email", objectStudent.getEmail());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("students", null, values) > 0;
        db.close();



        return createSuccessful;
    }

    public int count(){
        SQLiteDatabase db = this.getWritableDatabase();

        String querryString = "SELECT * FROM students";
        int recordCount = db.rawQuery(querryString, null).getCount();
        db.close();

        return recordCount;
    }

    public List<ObjectStudent> read() {
        return null;
    }

    public ObjectStudent readSingleRecord(int studentId) {

        ObjectStudent objectStudent = null;

        String sql = "SELECT * FROM students WHERE id = " + studentId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String email = cursor.getString(cursor.getColumnIndex("email"));

            objectStudent = new ObjectStudent();
            objectStudent.setId(id);
            objectStudent.trySetUserName(firstname);
            objectStudent.trySetEmail(email);

        }

        cursor.close();
        db.close();

        return objectStudent;

    }

    public boolean update(ObjectStudent objectStudent) {

        ContentValues values = new ContentValues();

        values.put("firstname", objectStudent.getUserName());
        values.put("email", objectStudent.getEmail());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(objectStudent.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update("students", values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;

    }

    public boolean delete(int id) {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("students", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }
}
