package com.example.carcrashproject_v20_10112024.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carcrashproject_v20_10112024.db.models.Entity;

public class EntitiesTableHelper {
    public static final String ENTITIES_TABLE_NAME = "Entities";
    public static final String ENTITIES_COLUMN_ID = "Id";
    public static final String ENTITIES_COLUMN_FIRST_NAME = "FirstName";
    public static final String ENTITIES_COLUMN_LAST_NAME = "LastName";
    public static final String ENTITIES_COLUMN_PHONE_NUMBER = "PhoneNumber";
    public static final String ENTITIES_COLUMN_EMAIL = "Email";
    public static final String ENTITIES_COLUMN_CAR_NUMBER = "CarNumber";
    public static final String ENTITIES_COLUMN_IDENTIFICATION = "Identification";

    public static void createEntitiesTable(SQLiteDatabase db) {
        String CreateEntitiesTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s NVARCHAR(50), " +
                        "%s NVARCHAR(50), " +
                        "%s NVARCHAR(20), " +
                        "%s NVARCHAR(50), " +
                        "%s NVARCHAR(20), " +
                        "%s NVARCHAR(20)" +
                        ");",
                ENTITIES_TABLE_NAME,
                ENTITIES_COLUMN_ID,
                ENTITIES_COLUMN_FIRST_NAME,
                ENTITIES_COLUMN_LAST_NAME,
                ENTITIES_COLUMN_PHONE_NUMBER,
                ENTITIES_COLUMN_EMAIL,
                ENTITIES_COLUMN_CAR_NUMBER,
                ENTITIES_COLUMN_IDENTIFICATION
        );
        db.execSQL(CreateEntitiesTable);
    }
    private DBHelper dbHelper;
    public EntitiesTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }
    public void InsertNewEntity(Entity entity){
        Log.i("DBLog", "Started inserting new entity");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ENTITIES_COLUMN_FIRST_NAME, entity.getFirstName());
        values.put(ENTITIES_COLUMN_LAST_NAME, entity.getLastName());
        values.put(ENTITIES_COLUMN_PHONE_NUMBER, entity.getPhoneNumber());
        values.put(ENTITIES_COLUMN_EMAIL, entity.getEmail());
        values.put(ENTITIES_COLUMN_CAR_NUMBER, entity.getCarNumber());
        values.put(ENTITIES_COLUMN_IDENTIFICATION, entity.getIdentification());


        long id = db.insert(ENTITIES_TABLE_NAME, null, values);
        entity.setId((int) id);
        db.close();



    }
}
