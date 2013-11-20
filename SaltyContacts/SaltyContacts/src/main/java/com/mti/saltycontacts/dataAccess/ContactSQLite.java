package com.mti.saltycontacts.dataAccess;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Antoine on 11/20/13.
 */
public class ContactSQLite extends SQLiteOpenHelper {
    private static final String TABLE_CONTACTS = "table_contacts";
    private static final String COL_CONTACT_ID = "ID";
    private static final String COL_CONTACT_FIRSTNAME = "FIRSTNAME";
    private static final String COL_CONTACT_LASTNAME = "LASTNAME";
    private static final String COL_CONTACT_ADDRESS = "ADDRESS";

    private static final String TABLE_EMAIL = "table_emails";
    private static final String COL_EMAIL_ID = "ID";
    private static final String COL_EMAIL_CONTACT_ID = "CONTACT_ID";
    private static final String COL_EMAIL_TAG_ID = "TAG_ID";
    private static final String COL_EMAIL_VALUE = "VALUE";

    private static final String TABLE_PHONE = "table_phones";
    private static final String COL_PHONE_ID = "ID";
    private static final String COL_PHONE_CONTACT_ID = "CONTACT_ID";
    private static final String COL_PHONE_TAG_ID = "TAG_ID";
    private static final String COL_PHONE_VALUE = "VALUE";

    private static final String TABLE_TAG = "table_tags";
    private static final String COL_TAG_ID = "ID";
    private static final String COL_TAG_VALUE = "VALUE";


    private static final String CREATE_BDD = "CREATE TABLE "
            + TABLE_CONTACTS
            + " (" + COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CONTACT_FIRSTNAME + " TEXT NOT NULL, "
            + COL_CONTACT_LASTNAME + " TEXT NOT NULL, "
            + COL_CONTACT_ADDRESS + " TEXT NOT NULL); " +

            "CREATE TABLE "
            + TABLE_EMAIL
            + " (" + COL_EMAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_EMAIL_CONTACT_ID + " INTEGER, "
            + COL_EMAIL_TAG_ID + " INTEGER, "
            + COL_EMAIL_VALUE + " TEXT NOT NULL); " +

            "CREATE TABLE "
            + TABLE_PHONE
            + " (" + COL_PHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PHONE_CONTACT_ID + " INTEGER, "
            + COL_PHONE_TAG_ID + " INTEGER, "
            + COL_PHONE_VALUE + " TEXT NOT NULL); " +

            "CREATE TABLE "
            + TABLE_TAG
            + " (" + COL_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TAG_VALUE + " TEXT NOT NULL);";

    public ContactSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ContactSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_EMAIL + "; " + "DROP TABLE " + TABLE_PHONE + "; DROP TABLE "  + TABLE_TAG + "; DROP TABLE " + TABLE_CONTACTS);
        this.onCreate(db);
    }
}