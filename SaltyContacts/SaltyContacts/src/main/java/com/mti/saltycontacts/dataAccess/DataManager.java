package com.mti.saltycontacts.dataAccess;

import android.content.Context;

import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

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

        List<PhoneNumber> current = c.getPhoneNumbers();

        ArrayList<Long> phone_ids = new ArrayList<Long>();

        for (PhoneNumber pn : current) {
            pn.setId(contactsBDD.insertOrUpdatePhoneNumber(pn, c));
            phone_ids.add(pn.getId());
        }

        ArrayList<PhoneNumber> phones = contactsBDD.getPhoneNumbers(c);

        for (PhoneNumber pn : phones) {
            if (!phone_ids.contains(pn.getId())) {
                contactsBDD.removePhoneNumber(pn);
            }
        }

        List<EmailAddress> current_addresses = c.getEmailListAddress();
        ArrayList<Long> email_ids = new ArrayList<Long>();

        for (EmailAddress address : current_addresses) {
            address.setId(contactsBDD.insertOrUpdateEmail(address, c));
            email_ids.add(address.getId());
        }

        ArrayList<EmailAddress> addresses = contactsBDD.getEmailAddresses(c);


        for (EmailAddress address : addresses) {
            if (!email_ids.contains(address.getId())) {
                contactsBDD.removeEmail(address);
            }
        }

        contactsBDD.close();
        return c;
    }

    public void removeContact(Contact contact) {
        contactsBDD.openForWrite();

        for (EmailAddress address : contact.getEmailListAddress()) {
            contactsBDD.removeEmail(address);
        }

        for (PhoneNumber pn : contact.getPhoneNumbers()) {
            contactsBDD.removePhoneNumber(pn);
        }
        contactsBDD.removeContact(contact);
        contacts.remove(contact);
        contactsBDD.close();
    }

    public Contact getContact(long id) {
        for (Contact d : contacts) {
            if (d.getId() == id)
                return d;
        }
        return null;
    }
}
