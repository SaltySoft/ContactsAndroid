package com.mti.saltycontacts.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;
import com.mti.saltycontacts.models.Tag;

import java.util.ArrayList;

/**
 * Created by Antoine on 11/20/13.
 */
public class ContactsBDD {
    private static final int VERSION = 2;
    private static final String NOM_BDD = "contacts.db";


    private static final String TABLE_CONTACTS = "table_contacts";
    private static final String COL_CONTACT_ID = "ID";
    private static final int NUM_CONTACT_COL_ID = 0;
    private static final String COL_CONTACT_FIRSTNAME = "FIRSTNAME";
    private static final int NUM_CONTACT_COL_FIRSTNAME = 1;
    private static final String COL_CONTACT_LASTNAME = "LASTNAME";
    private static final int NUM_CONTACT_COL_LASTNAME = 2;
    private static final String COL_CONTACT_ADDRESS = "ADDRESS";
    private static final int NUM_CONTACT_COL_ADDRESS = 3;

    private static final String TABLE_EMAIL = "table_emails";
    private static final String COL_EMAIL_ID = "ID";
    private static final String COL_EMAIL_CONTACT_ID = "CONTACT_ID";
    private static final String COL_EMAIL_TAG_ID = "TAG_ID";
    private static final String COL_EMAIL_VALUE = "VALUE";

    private static final String TABLE_PHONE = "table_phones";
    private static final String COL_PHONE_ID = "ID";
    private static final int NUM_PHONE_COL_ID = 0;
    private static final String COL_PHONE_CONTACT_ID = "CONTACT_ID";
    private static final int NUM_PHONE_CONTACT_ID = 1;
    private static final String COL_PHONE_TAG_ID = "TAG_ID";
    private static final int NUM_PHONE_TAG_ID = 2;
    private static final String COL_PHONE_VALUE = "VALUE";
    private static final int NUM_PHONE_VALUE = 3;

    private static final String TABLE_TAG = "table_tags";
    private static final String COL_TAG_ID = "ID";
    private static final String COL_TAG_VALUE = "VALUE";

    private SQLiteDatabase bdd;

    private ContactSQLite contacts_sqlite;

    private ArrayList<Contact> contacts;

    public ContactSQLite getContacts_sqlite() {
        return contacts_sqlite;
    }

    public void setContacts_sqlite(ContactSQLite contacts_sqlite) {
        this.contacts_sqlite = contacts_sqlite;
    }

    public ContactsBDD(Context context) {
        contacts_sqlite = new ContactSQLite(context, NOM_BDD, null, VERSION);
    }

    public void openForWrite() {
        bdd = contacts_sqlite.getWritableDatabase();
    }

    public void openForRead() {
        bdd = contacts_sqlite.getReadableDatabase();
    }

    public void close() {
        bdd.close();
    }

    public SQLiteDatabase getBdd() {
        return bdd;
    }



    public long insertOrUpdatePhoneNumber(PhoneNumber number, Contact c) {
        if (number != null) {
            if (number.getId() == 0) {
                ContentValues content = new ContentValues();
                content.put(COL_PHONE_CONTACT_ID, c.getId());
                if (number.getTag() != null)
                    content.put(COL_PHONE_TAG_ID, number.getTag().getId());
                else
                    content.put(COL_PHONE_TAG_ID, 0);
                content.put(COL_PHONE_VALUE, number.getNumber());
                long id = bdd.insert(TABLE_PHONE, null, content);
                number.setId(id);
            } else {
                ContentValues content = new ContentValues();
                content.put(COL_PHONE_TAG_ID, number.getTag().getId());
                content.put(COL_PHONE_CONTACT_ID, c.getId());
                content.put(COL_PHONE_VALUE, number.getNumber());
                long id = number.getId();
                id = bdd.update(TABLE_PHONE, content, COL_PHONE_ID + " = " + id, null);
                number.setId(id);
            }
            return number.getId();
        } else {
            return 0;
        }
    }

    public long insertOrUpdateTag(Tag tag) {
        if (tag.getId() == 0) {
            ContentValues content = new ContentValues();
            content.put(COL_TAG_VALUE, tag.getName());
            long id = bdd.insert(TABLE_TAG, null, content);
            tag.setId(id);
        } else {
            ContentValues content = new ContentValues();
            content.put(COL_TAG_VALUE, tag.getName());
            long id = tag.getId();
            id = bdd.update(TABLE_TAG, content, COL_TAG_ID + " = " + id, null);
            tag.setId(id);
        }
        return tag.getId();

    }

    public long insertOrUpdateEmail(EmailAddress emailAddress) {
        return 0;
    }

    public long insertOrUpdateContact(Contact contact) {
        if (contact.getId() == 0) {
            ContentValues content = new ContentValues();
            content.put(COL_CONTACT_FIRSTNAME, contact.getFirstName());
            content.put(COL_CONTACT_LASTNAME, contact.getLastName());
            content.put(COL_CONTACT_ADDRESS, contact.getPostalAddress());
            long id = bdd.insert(TABLE_CONTACTS, null, content);
            contact.setId(id);
        } else {
            ContentValues content = new ContentValues();
            content.put(COL_CONTACT_FIRSTNAME, contact.getFirstName());
            content.put(COL_CONTACT_LASTNAME, contact.getLastName());
            content.put(COL_CONTACT_ADDRESS, contact.getPostalAddress());
            long id = contact.getId();

            id = bdd.update(TABLE_CONTACTS, content, COL_CONTACT_ID + " = " + id, null);
            contact.setId(id);
        }
        return contact.getId();
    }

    public long removeContact(Contact contact) {
        return bdd.delete(TABLE_CONTACTS, COL_CONTACT_ID + " = " + contact.getId(), null);
    }

    public Contact getContact(long id) {
        Cursor c = bdd.query(TABLE_CONTACTS, new String[]
                {COL_CONTACT_ID, COL_CONTACT_FIRSTNAME, COL_CONTACT_LASTNAME, COL_CONTACT_ADDRESS},
                COL_CONTACT_ID + " = " + id, null, null, null, COL_CONTACT_ID);
        return cursorToContact(c);
    }


    public Contact cursorToContact(Cursor c) {
        if (c.getCount() == 0) {
            c.close();
            return null;
        }

        Contact contact = new Contact();
        contact.setId(c.getInt(NUM_CONTACT_COL_ID));
        contact.setFirstName(c.getString(NUM_CONTACT_COL_FIRSTNAME));
        contact.setLastName(c.getString(NUM_CONTACT_COL_LASTNAME));
        contact.setPostalAddress(c.getString(NUM_CONTACT_COL_ADDRESS));
        c.close();
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        Cursor c = bdd.query(TABLE_CONTACTS, new String[]
                {COL_CONTACT_ID, COL_CONTACT_FIRSTNAME, COL_CONTACT_LASTNAME, COL_CONTACT_ADDRESS},
                null, null, null, null, COL_CONTACT_ID);

        if (c.getCount() == 0) {
            c.close();
            return new ArrayList<Contact>();
        }

        ArrayList<Contact> contact_list = new ArrayList<Contact>();
        while (c.moveToNext()) {
            Contact contact = new Contact();
            contact.setId(c.getInt(NUM_CONTACT_COL_ID));
            contact.setFirstName(c.getString(NUM_CONTACT_COL_FIRSTNAME));
            contact.setLastName(c.getString(NUM_CONTACT_COL_LASTNAME));
            contact.setPostalAddress(c.getString(NUM_CONTACT_COL_ADDRESS));

            contact.setPhoneNumbers(getPhoneNumbers(contact));
            contact_list.add(contact);
        }
        c.close();
        return contact_list;
    }

    public ArrayList<PhoneNumber> getPhoneNumbers(Contact contact) {
        Cursor c = bdd.query(TABLE_PHONE, new String[]
                {COL_PHONE_ID, COL_PHONE_CONTACT_ID, COL_PHONE_TAG_ID, COL_PHONE_VALUE},
                COL_PHONE_CONTACT_ID + " = " + contact.getId(), null, null, null, COL_PHONE_ID);

        if (c.getCount() == 0) {
            c.close();
            return  new ArrayList<PhoneNumber>();
        }

        ArrayList<PhoneNumber> number_list = new ArrayList<PhoneNumber>();
        while (c.moveToNext()) {
            PhoneNumber number = new PhoneNumber();
            number.setId(c.getInt(NUM_CONTACT_COL_ID));
//            number.setTag(c.getString(NUM_CONTACT_COL_FIRSTNAME));
            Tag tag = new Tag("TAG");
            number.setTag(tag);
            number.setNumber(c.getString(NUM_PHONE_VALUE));

            number_list.add(number);
        }
        c.close();



        return number_list;
    }

}
