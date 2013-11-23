package com.mti.saltycontacts.dataAccess;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mti.saltycontacts.models.Tag;

/**
 * Created by Antoine on 11/20/13.
 */
public class ContactSQLite extends SQLiteOpenHelper {
    private Context context;


    private static final String TABLE_CONTACTS = "table_contacts";
    private static final String COL_CONTACT_ID = "ID";
    private static final String COL_CONTACT_FIRSTNAME = "FIRSTNAME";
    private static final String COL_CONTACT_LASTNAME = "LASTNAME";
    private static final String COL_CONTACT_ADDRESS = "ADDRESS";
    private static final String COL_CONTACT_PICTURE_URL = "PICTURE_URL";

    private static final String TABLE_EMAIL = "table_emails";
    private static final String COL_EMAIL_ID = "ID";
    private static final String COL_EMAIL_CONTACT_ID = "CONTACT_ID";
    private static final String COL_EMAIL_TAG = "TAG";
    private static final String COL_EMAIL_VALUE = "VALUE";

    private static final String TABLE_PHONE = "table_phones";
    private static final String COL_PHONE_ID = "ID";
    private static final String COL_PHONE_CONTACT_ID = "CONTACT_ID";
    private static final String COL_PHONE_TAG = "TAG";
    private static final String COL_PHONE_VALUE = "VALUE";


    private static final String CREATE_CONTACT_TABLE = "CREATE TABLE "
            + TABLE_CONTACTS
            + " (" + COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CONTACT_FIRSTNAME + " TEXT NOT NULL, "
            + COL_CONTACT_LASTNAME + " TEXT NOT NULL, "
            + COL_CONTACT_ADDRESS + " TEXT NOT NULL, "
            + COL_CONTACT_PICTURE_URL + " TEXT);";
    private static final String CREATE_EMAIL_TABLE =
            "CREATE TABLE "
                    + TABLE_EMAIL
                    + " (" + COL_EMAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_EMAIL_CONTACT_ID + " INTEGER, "
                    + COL_EMAIL_TAG + " TEXT NOT NULL, "
                    + COL_EMAIL_VALUE + " TEXT NOT NULL); ";
    private static final String CREATE_PHONE_TABLE =
            "CREATE TABLE "
                    + TABLE_PHONE
                    + " (" + COL_PHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PHONE_CONTACT_ID + " INTEGER, "
                    + COL_PHONE_TAG + " TEXT NOT NULL, "
                    + COL_PHONE_VALUE + " TEXT NOT NULL); ";

    public ContactSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public ContactSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_EMAIL_TABLE);
        db.execSQL(CREATE_PHONE_TABLE);





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_EMAIL);
        db.execSQL("DROP TABLE " + TABLE_PHONE);
        db.execSQL("DROP TABLE " + TABLE_CONTACTS);
        this.onCreate(db);
    }
}
