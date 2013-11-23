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
    private static final String COL_TAG_TYPE = "TYPE";


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
                    + COL_EMAIL_TAG_ID + " INTEGER, "
                    + COL_EMAIL_VALUE + " TEXT NOT NULL); ";
    private static final String CREATE_PHONE_TABLE =
            "CREATE TABLE "
                    + TABLE_PHONE
                    + " (" + COL_PHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_PHONE_CONTACT_ID + " INTEGER, "
                    + COL_PHONE_TAG_ID + " INTEGER, "
                    + COL_PHONE_VALUE + " TEXT NOT NULL); ";
    private static final String CREATE_TAG_TABLE =
            "CREATE TABLE "
                    + TABLE_TAG
                    + " (" + COL_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_TAG_TYPE + " INTEGER NOT NULL,"
                    + COL_TAG_VALUE + " TEXT NOT NULL);";

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
        db.execSQL(CREATE_TAG_TABLE);


        //Email tags
        Tag personal = new Tag();
        personal.setName("Personal");

        ContentValues content = new ContentValues();
        content.put(COL_TAG_VALUE, personal.getName());
        content.put(COL_TAG_TYPE, personal.getType().getNumVal());
        long id = db.insert(TABLE_TAG, null, content);

        Tag professional = new Tag();
        professional.setName("Personal");

        content = new ContentValues();
        content.put(COL_TAG_VALUE, professional.getName());
        content.put(COL_TAG_TYPE, professional.getType().getNumVal());
        id = db.insert(TABLE_TAG, null, content);


        //Phone tags
        Tag home = new Tag();
        home.setName("Home");
        home.setType(Tag.Types.PHONE);

        content = new ContentValues();
        content.put(COL_TAG_VALUE, home.getName());
        content.put(COL_TAG_TYPE, home.getType().getNumVal());
        id = db.insert(TABLE_TAG, null, content);

        Tag mobile = new Tag();
        mobile.setName("Mobile");
        mobile.setType(Tag.Types.PHONE);

        content = new ContentValues();
        content.put(COL_TAG_VALUE, mobile.getName());
        content.put(COL_TAG_TYPE, mobile.getType().getNumVal());
        id = db.insert(TABLE_TAG, null, content);

        Tag work = new Tag();
        work.setName("Work");
        work.setType(Tag.Types.PHONE);

        content = new ContentValues();
        content.put(COL_TAG_VALUE, work.getName());
        content.put(COL_TAG_TYPE, work.getType().getNumVal());
        id = db.insert(TABLE_TAG, null, content);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_TAG);
        db.execSQL("DROP TABLE " + TABLE_EMAIL);
        db.execSQL("DROP TABLE " + TABLE_PHONE);
        db.execSQL("DROP TABLE " + TABLE_CONTACTS);
        this.onCreate(db);
    }
}
