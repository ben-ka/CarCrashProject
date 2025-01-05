package com.example.carcrashproject_v20_10112024.db.provider;

import android.database.sqlite.SQLiteDatabase;

public class AccidentDocumentsTableHelper {
    public static final String ACCIDENT_DOCUMENTS_TABLE_NAME = "AccidentDocuments";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_ID = "Id";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID = "AccidentId";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_FILE_NAME = "FileName";

    public static void createAccidentDocumentsTable(SQLiteDatabase db) {
        String CreateAccidentDocumentsTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s NVARCHAR(50), " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                        ");",
                ACCIDENT_DOCUMENTS_TABLE_NAME,
                ACCIDENT_DOCUMENTS_COLUMN_ID,
                ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID,
                ACCIDENT_DOCUMENTS_COLUMN_FILE_NAME,
                ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID, "Accidents", "Id" // References Accidents(Id)
        );
        db.execSQL(CreateAccidentDocumentsTable);
    }

}
