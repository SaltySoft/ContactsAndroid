package com.mti.saltycontacts.dataAccess;

import android.content.Context;

import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by Antoine on 11/21/13.
 */
public class DataManager {

    private static DataManager instance;

    public static DataManager getInstance(Context ctx) {
        if (instance != null) {
            return instance;
        } else {
            instance = new DataManager(ctx);
            return instance;
        }
    }

    private ArrayList<Contact> contacts;
    private ContactsBDD contactsBDD;

    public DataManager(Context ctx) {
        contacts = new ArrayList<Contact>();
        contactsBDD = new ContactsBDD(ctx.getApplicationContext());

        refreshList();
    }

    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }

    public void refreshList() {
        contactsBDD.openForRead();
        contacts = contactsBDD.getAllContacts();
        contactsBDD.close();
        if (contacts == null) {
            contacts = new ArrayList<Contact>();
        }
    }

    public Contact persist(Contact contact) {
        Contact c = getContact(contact.getId());
        if (c == null) {
            c = new Contact();
            contacts.add(c);
        }
        contact.copy(c);
        contactsBDD.openForWrite();
        contact.setId(contactsBDD.insertOrUpdateContact(c));
        for (PhoneNumber pn : c.getPhoneNumbers()) {
            contactsBDD.insertOrUpdatePhoneNumber(pn, c);
        }
        contactsBDD.close();
        return c;
    }

    public Contact getContact(long id) {
        for(Contact d : contacts){
            if(d.getId() == id)
                return d;
        }
        return null;
    }




}
