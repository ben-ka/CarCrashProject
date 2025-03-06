package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.carcrashproject_v20_10112024.Data.db.models.InvolvedDrivers;

public class InvolvedDriversTableHelper {
    public static final String INVOLVED_DRIVERS_TABLE_NAME = "InvolvedDrivers";
    public static final String INVOLVED_DRIVERS_COLUMN_ID = "id";

    public static final String INVOLVED_DRIVERS_COLUMN_ACCIDENT_ID = "accidentId";
    public static final String INVOLVED_DRIVERS_COLUMN_DRIVER_NAME = "driverName";
    public static final String INVOLVED_DRIVERS_COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String INVOLVED_DRIVERS_COLUMN_LICENSE_PLATE= "licensePlate";

    private DBHelper dbHelper;
    public InvolvedDriversTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }
    public static void createInvolvedDriversTable(SQLiteDatabase db) {
        String CreateInvolvedDriversTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s NVARCHAR(50), " +
                        "%s NVARCHAR(50), " +
                        "%s NVARCHAR(50), " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s) " +
                        ");",
                INVOLVED_DRIVERS_TABLE_NAME,
                INVOLVED_DRIVERS_COLUMN_ID,
                INVOLVED_DRIVERS_COLUMN_DRIVER_NAME,
                INVOLVED_DRIVERS_COLUMN_PHONE_NUMBER,
                INVOLVED_DRIVERS_COLUMN_LICENSE_PLATE,
                INVOLVED_DRIVERS_COLUMN_ACCIDENT_ID, "Accidents", "Id"
        );
        db.execSQL(CreateInvolvedDriversTable);
    }

    public InvolvedDrivers insertInvolvedDriver(InvolvedDrivers driver) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INVOLVED_DRIVERS_COLUMN_ACCIDENT_ID, driver.getAccidentId());
        values.put(INVOLVED_DRIVERS_COLUMN_DRIVER_NAME, driver.getDriverName());
        values.put(INVOLVED_DRIVERS_COLUMN_PHONE_NUMBER, driver.getPhoneNumber());
        values.put(INVOLVED_DRIVERS_COLUMN_LICENSE_PLATE, driver.getLicensePlate());

        long id = db.insert(INVOLVED_DRIVERS_TABLE_NAME, null, values);
        driver.setId((int) id);
        db.close();
        return driver;
    }
}
