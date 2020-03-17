package com.example.projectx.authentication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     *
     * @return name of the name column in the table USERS
     */
    public static String getName() {
        return NAME;
    }

    /**
     *
     * @return name of the age column in USERS table
     */
    public static String getAge() {
        return AGE;
    }

    /**
     *
     * @return name of the DateAdded column in USERS table
     */
    public static String getDateAdded() {
        return DATE_ADDED;
    }
    /**
     *
     * @return name of the gender column in USERS table
     */
    public static String getGender() {
        return GENDER;
    }
    /**
     *
     * @return name of the Database name
     */

    public static String getDBName() {
        return DATABASE_NAME;
    }
    /**
     *
     * @return name of the USERS table
     */
    public static String getTableName() {
        return TABLE_NAME;
    }
    /**
     *
     * @return name of the email column in USERS table
     */
    public static String getEmail() {
        return EMAIL;
    }
    /**
     *
     * @return name of the password column in USERS table
     */
    public static String getPassword() {
        return PASSWORD;
    }

    @Override
    public void onCreate (SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + "(" + NAME + " TEXT, " + EMAIL + " TEXT,"
                + PASSWORD + " TEXT," + AGE + " INT, "+ GENDER + " TEXT," + DATE_ADDED + " TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        onCreate(db);
    }

    /**
     * drops the Table Users
     * @param db SQLiteDatabase object that manages the database
     */
    public void dropTable(SQLiteDatabase db)
    {
        String query = "DROP TABLE IF EXISTS USERS;";
        db.execSQL(query);

    }


}
