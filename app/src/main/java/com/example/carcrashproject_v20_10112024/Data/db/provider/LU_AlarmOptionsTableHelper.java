package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.carcrashproject_v20_10112024.Data.db.models.AlarmOption;

public class LU_AlarmOptionsTableHelper {
    public static final String ALARM_OPTIONS_TABLE_NAME = "LU_AlarmOptions";
    public static final String ALARM_OPTIONS_COLUMN_ID = "Id";
    public static final String ALARM_OPTIONS_COLUMN_DESCRIPTION = "Description";

    private DBHelper dbHelper;
    public LU_AlarmOptionsTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }
    public static void createAlarmOptionsTable(SQLiteDatabase db) {
        String CreateAlarmOptionsTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s NVARCHAR(30)" +
                        ");",
                ALARM_OPTIONS_TABLE_NAME,
                ALARM_OPTIONS_COLUMN_ID,
                ALARM_OPTIONS_COLUMN_DESCRIPTION
        );
        db.execSQL(CreateAlarmOptionsTable);
        populateAlarmOptionsTable(db);
    }

    private static void populateAlarmOptionsTable(SQLiteDatabase db) {
        for (AlarmOption option : AlarmOption.values()) {
            ContentValues values = new ContentValues();
            values.put(ALARM_OPTIONS_COLUMN_DESCRIPTION, option.name());
            db.insert(ALARM_OPTIONS_TABLE_NAME, null, values);
        }
    }
}
