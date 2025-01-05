package com.example.carcrashproject_v20_10112024.Data.db.provider;

import android.database.sqlite.SQLiteDatabase;

public class QuestionConfigurationTableHelper {
    public static final String QUESTION_CONFIG_TABLE_NAME = "QuestionConfiguration";
    public static final String QUESTION_CONFIG_COLUMN_ID = "Id";
    public static final String QUESTION_CONFIG_COLUMN_DESCRIPTION = "Description";
    public static final String QUESTION_CONFIG_COLUMN_ANSWER_TYPE = "AnswerType";

    public static void createQuestionConfigurationTable(SQLiteDatabase db) {
        String CreateQuestionConfigurationTable = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s NVARCHAR(80), " +
                        "%s INTEGER, " +
                        "FOREIGN KEY (%s) REFERENCES %s(%s)" +
                        ");",
                QUESTION_CONFIG_TABLE_NAME,
                QUESTION_CONFIG_COLUMN_ID,
                QUESTION_CONFIG_COLUMN_DESCRIPTION,
                QUESTION_CONFIG_COLUMN_ANSWER_TYPE,
                QUESTION_CONFIG_COLUMN_ANSWER_TYPE, "LU_AnswerTypes", "Id" // References LU_AnswerTypes(Id)
        );
        db.execSQL(CreateQuestionConfigurationTable);
    }
}
