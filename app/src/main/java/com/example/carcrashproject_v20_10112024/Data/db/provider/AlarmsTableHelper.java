package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.carcrashproject_v20_10112024.Data.db.models.Accident;
import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;

public class AlarmsTableHelper {
    public static final String ALARMS_TABLE_NAME = "Alarms";
    public static final String ALARMS_COLUMN_ID = "Id";
    public static final String ALARMS_COLUMN_ENTITY_ID = "EntityId";
    public static final String ALARMS_COLUMN_LATITUDE = "Latitude";
    public static final String ALARMS_COLUMN_LONGITUDE = "Longitude";
    public static final String ALARMS_COLUMN_DATETIME = "DateTime";
    public static final String ALARMS_COLUMN_ALARM_OPTION_ID = "AlarmOptionId";

    public static void createAlarmsTable(SQLiteDatabase db) {
        String CreateAlarmsTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s NVARCHAR(15), " +
                        "%s NVARCHAR(15), " +
                        "%s DATETIME, " +
                        "%s INTEGER, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s), " +  // Foreign key to EntityId
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +    // Foreign key to AlarmOptionId
                        ");",
                ALARMS_TABLE_NAME,
                ALARMS_COLUMN_ID,
                ALARMS_COLUMN_ENTITY_ID,
                ALARMS_COLUMN_LATITUDE,
                ALARMS_COLUMN_LONGITUDE,
                ALARMS_COLUMN_DATETIME,
                ALARMS_COLUMN_ALARM_OPTION_ID,
                ALARMS_COLUMN_ENTITY_ID, "Entities", "Id",         // References Entities(Id)
                ALARMS_COLUMN_ALARM_OPTION_ID, "LU_AlarmOptions", "Id" // References LU_AlarmOptions(Id)
        );
        db.execSQL(CreateAlarmsTable);
    }
    private DBHelper dbHelper;
    public AlarmsTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }

    public void insertNewAlarm(Alarm alarm)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(ALARMS_COLUMN_LATITUDE, alarm.getLatitude());
        values.put(ALARMS_COLUMN_LONGITUDE, alarm.getLongitude());
        values.put(ALARMS_COLUMN_DATETIME, alarm.getDateTime());


        long id = db.insert(ALARMS_TABLE_NAME, null, values);
        alarm.setId((int) id);
        db.close();


    }

    public void updateAlarmOption(int alarmOption, int alarmId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ALARMS_COLUMN_ALARM_OPTION_ID, alarmOption);
        String whereClause = ALARMS_COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(alarmId)};
        int rowsAffected = db.update(ALARMS_TABLE_NAME, values, whereClause, whereArgs);
    }

    public Alarm getAlarmFromAccident(Accident accident){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Alarm alarm = null;
        // Query to get the alarm associated with the accident
        String query = "SELECT * FROM " + ALARMS_TABLE_NAME + " WHERE " + ALARMS_COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accident.getAlarmId())};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            // Create an Alarm object and populate it with data from the cursor
            alarm = new Alarm();
            alarm.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ALARMS_COLUMN_ID)));
            alarm.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(ALARMS_COLUMN_LATITUDE)));
            alarm.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(ALARMS_COLUMN_LONGITUDE)));
            alarm.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(ALARMS_COLUMN_DATETIME)));
            alarm.setAlarmOptionId(cursor.getInt(cursor.getColumnIndexOrThrow(ALARMS_COLUMN_ALARM_OPTION_ID)));
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return alarm;
    }

}
