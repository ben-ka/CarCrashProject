package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.carcrashproject_v20_10112024.Data.db.models.AccidentDocument;
import com.example.carcrashproject_v20_10112024.Data.db.models.Alarm;

public class AccidentDocumentsTableHelper {
    public static final String ACCIDENT_DOCUMENTS_TABLE_NAME = "AccidentDocuments";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_ID = "Id";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA = "FileData";
    public static final String ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID = "AccidentId";

    private DBHelper dbHelper;
    public AccidentDocumentsTableHelper(Context context){
        dbHelper = new DBHelper(context);
    }

    public static void createAccidentDocumentsTable(SQLiteDatabase db) {
        String CreateAccidentDocumentsTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER, " +
                        "%s BLOB, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                        ");",
                ACCIDENT_DOCUMENTS_TABLE_NAME,
                ACCIDENT_DOCUMENTS_COLUMN_ID,
                ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID,
                ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA,
                ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID, "Accidents", "Id" // References Accidents(Id)
        );
        db.execSQL(CreateAccidentDocumentsTable);
    }

    public AccidentDocument insertAccidentDocument(AccidentDocument document){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA, document.getFileData());
        values.put(ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID, document.getAccidentId());
        long id = db.insert(ACCIDENT_DOCUMENTS_TABLE_NAME, null, values);
        document.setId((int) id);
        db.close();
        return document;
    }
    public AccidentDocument getAccidentDocumentById(int documentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ACCIDENT_DOCUMENTS_TABLE_NAME,
                new String[]{AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ID,
                        AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID,
                        AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA},
                AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(documentId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ID));
            int accidentId = cursor.getInt(cursor.getColumnIndexOrThrow(AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID));
            byte[] fileData = cursor.getBlob(cursor.getColumnIndexOrThrow(AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA));

            cursor.close();
            return new AccidentDocument(id, accidentId, fileData);
        }
        db.close();

        return null;
    }

    public AccidentDocument getDocumentByAccidentId(int accidentId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ACCIDENT_DOCUMENTS_TABLE_NAME,
                new String[]{AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ID,
                        AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID,
                        AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA},
                AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ACCIDENT_ID + " = ?",
                new String[]{String.valueOf(accidentId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_ID));
            byte[] fileData = cursor.getBlob(cursor.getColumnIndexOrThrow(AccidentDocumentsTableHelper.ACCIDENT_DOCUMENTS_COLUMN_FILE_DATA));

            cursor.close();
            return new AccidentDocument(id, accidentId, fileData);
        }
        db.close();

        return null;
    }

}
