package com.mti.saltycontacts.dataAccess;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Antoine on 11/21/13.
 */
public class DataManager {

    private static DataManager instance;
    private Context _context;
    private  HashMap<String, Contact> map = new HashMap<String, Contact>();

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
        _context = ctx;
        contacts = new ArrayList<Contact>();
        contactsBDD = new ContactsBDD(ctx.getApplicationContext());
        refreshList();
    }


    public void fillContacts() {
        final ContentResolver cr = _context.getContentResolver();
        final Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'", null,
                ContactsContract.Contacts.DISPLAY_NAME);
        final int nameIndex = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        final int hasPhoneIndex = cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        final int idIndex = cur.getColumnIndex(ContactsContract.Contacts._ID);


        while (cur.moveToNext()) {
            Contact contact;
            String name = cur.getString(nameIndex);
            String id = cur.getString(idIndex);
            if (map.get(id) == null) {
                contact = new Contact();
                contact = this.persist(contact);

                contact.setModified(true);
                contact.setFirstName(name);

                contact.setAndroidId(id);
            } else {
                contact = map.get(id);
            }


            String hasNumber = cur.getString(hasPhoneIndex);
            if (Integer.parseInt(hasNumber) > 0) {
                final Cursor pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null
                );
                final int numberIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                final int labelIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);

                while (pCur.moveToNext()) {
                    String number = pCur.getString(numberIndex);
                    String label = pCur.getString(labelIndex);
                    boolean add = true;
                    for (PhoneNumber n : contact.getPhoneNumbers()) {
                        if (n.getNumber().equals(number)) {
                            add = false;
                        }
                    }
                    if (add) {
                        contact.addPhoneNumber(new PhoneNumber(number, label));
                        contact.setModified(true);
                    }
                }
                pCur.close();
            }


            if (contact.isModified()) {
                contact = persist(contact);
                map.put(contact.getFirstName(), contact);
            }

        }
        cur.close();
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
        map.clear();
        for (Contact c : contacts) {
            map.put(c.getAndroidId(), c);
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
        contact.setId(c.getId());
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
