package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;

import java.util.ArrayList;

public class AccidentsTableHelper {
    public static final String ACCIDENTS_TABLE_NAME = "Accidents";
    public static final String ACCIDENTS_COLUMN_ID = "Id";
    public static final String ACCIDENTS_COLUMN_ALARM_ID = "AlarmId";

    public static void createAccidentsTable(SQLiteDatabase db) {
        String CreateAccidentsTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                        ");",
                ACCIDENTS_TABLE_NAME,
                ACCIDENTS_COLUMN_ID,
                ACCIDENTS_COLUMN_ALARM_ID,
                ACCIDENTS_COLUMN_ALARM_ID, "Alarms", "Id" // References Alarms(Id)
        );
        db.execSQL(CreateAccidentsTable);
    }
    private DBHelper dbHelper;
    public AccidentsTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }

    public void insertNewAccident(Accident accident)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCIDENTS_COLUMN_ALARM_ID, accident.getAlarmId());
        
        long id = db.insert(ACCIDENTS_TABLE_NAME, null, values);
        accident.setId((int) id);
        db.close();

    }

    public ArrayList<Accident> retrieveAllAccidents()
    {
        ArrayList<Accident> accidents = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM Accidents";
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ACCIDENTS_COLUMN_ID));
                int alarmId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCIDENTS_COLUMN_ALARM_ID));

                // Create Accident object and populate its fields
                Accident accident = new Accident();
                accident.setId(id);
                accident.setAlarmId(alarmId);

                // Add to the list
                accidents.add(accident);
            } while (cursor.moveToNext());
        }
        if(cursor != null){
            cursor.close();
        }
        if(db != null){
            db.close();
        }
        return accidents;
    }
}
