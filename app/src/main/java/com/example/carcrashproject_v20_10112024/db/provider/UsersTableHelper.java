package com.example.carcrashproject_v20_10112024.db.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsersTableHelper {
    public static String USERS_TABLE_NAME = "Users";
    public static String USERS_TABLE_COLUMN_ID = "Id";
    public static String USERS_TABLE_COLUMN_ENTITY_ID = "EntityId";
    public static String USERS_TABLE_COLUMN_PASSWORD = "Password";


    private DBHelper dbHelper;

    public UsersTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }

    public static void createUsersTable(SQLiteDatabase db){
        String CreateUsersTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s NVARCHAR(15), " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                        ");",
                UsersTableHelper.USERS_TABLE_NAME,
                UsersTableHelper.USERS_TABLE_COLUMN_ID,
                UsersTableHelper.USERS_TABLE_COLUMN_ENTITY_ID,
                UsersTableHelper.USERS_TABLE_COLUMN_PASSWORD,
                UsersTableHelper.USERS_TABLE_COLUMN_ENTITY_ID,
                "Entities",  // Referenced table (Entities)
                "Id"  // Referenced column (Id in Entities table)
        );
        db.execSQL(CreateUsersTable);
    }

}
