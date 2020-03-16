package com.example.projectx.authentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "USERS";
    private static final String TABLE_NAME = "USERS";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String DATE_ADDED = "Date_Added";
    private static final String GENDER = "Gender";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    public UsersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    public static String getName() {
        return NAME;
    }

    public static String getAge() {
        return AGE;
    }

    public static String getDateAdded() {
        return DATE_ADDED;
    }

    public static String getGender() {
        return GENDER;
    }

    public static String getDBName() {
        return DATABASE_NAME;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getEmail() {
        return EMAIL;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        //    System.out.println(java.time.LocalDate.now());
        String query = "CREATE TABLE " + TABLE_NAME + "(" + NAME + " TEXT, " + EMAIL + " TEXT,"
                + PASSWORD + " TEXT," + AGE + " INT, "+ GENDER + " TEXT," + DATE_ADDED + " TEXT);";
        db.execSQL(query);
        //Log.e("usersdb", "created Table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }
    public void dropTable(SQLiteDatabase db)
    {
        String query = "DROP TABLE IF EXISTS USERS;";
        db.execSQL(query);

    }


}
