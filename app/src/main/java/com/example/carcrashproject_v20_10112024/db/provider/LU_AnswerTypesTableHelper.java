package com.example.carcrashproject_v20_10112024.db.provider;

import android.database.sqlite.SQLiteDatabase;

public class LU_AnswerTypesTableHelper {
    public static final String ANSWER_TYPES_TABLE_NAME = "LU_AnswerTypes";
    public static final String ANSWER_TYPES_COLUMN_ID = "Id";
    public static final String ANSWER_TYPES_COLUMN_DESCRIPTION = "Description";

    public static void createAnswerTypesTable(SQLiteDatabase db) {
        String CreateAnswerTypesTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s NVARCHAR(30)" +
                        ");",
                ANSWER_TYPES_TABLE_NAME,
                ANSWER_TYPES_COLUMN_ID,
                ANSWER_TYPES_COLUMN_DESCRIPTION
        );
        db.execSQL(CreateAnswerTypesTable);
    }
}
