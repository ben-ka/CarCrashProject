package com.example.carcrashproject_v20_10112024.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.carcrashproject_v20_10112024.db.models.Accident;
import com.example.carcrashproject_v20_10112024.db.models.Alarm;

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
}
