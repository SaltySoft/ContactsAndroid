package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.Button;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.ContactsListAdapter;
import com.mti.saltycontacts.dataAccess.ContactsBDD;
import com.mti.saltycontacts.dataAccess.DataManager;
import com.mti.saltycontacts.models.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    List<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ContactsBDD contactsBDD = new ContactsBDD(this);
//        contactsBDD.openForRead();
//
//        contacts = contactsBDD.getAllContacts();
//        contactsBDD.close();

        DataManager dataManager = DataManager.getInstance(MainActivity.this);
        this.contacts = dataManager.getContacts();

//        Tag tag1 = new Tag("Maison1");
//        Tag tag2 = new Tag("Maison2");
//        Tag tag3 = new Tag("Maison3");
//        Tag tag4 = new Tag("Perso1");
//        Tag tag5 = new Tag("Perso2");
//        Tag tag6 = new Tag("Perso3");
//        EmailAddress emailAddress1 = new EmailAddress("vinc.lefebv1@gmail.com", tag4);
//        EmailAddress emailAddress2 = new EmailAddress("vinc.lefebv2@gmail.com", tag5);
//        EmailAddress emailAddress3 = new EmailAddress("vinc.lefebv3@gmail.com", tag6);
//        PhoneNumber phoneNumber1 = new PhoneNumber("0102030405", tag1);
//        PhoneNumber phoneNumber2 = new PhoneNumber("0202020202", tag2);
//        PhoneNumber phoneNumber3 = new PhoneNumber("0303030303", tag3);
//        Contact contact1 = new Contact("Vincent", "Lefebvre", "Paul Vaillant Villejuif", "");
//        contact1.addPhoneNumber(phoneNumber1);
//        contact1.addPhoneNumber(phoneNumber2);
//        contact1.addPhoneNumber(phoneNumber3);
//        contact1.addEmailAddress(emailAddress1);
//        contact1.addEmailAddress(emailAddress2);
//        contact1.addEmailAddress(emailAddress3);
//        contacts = new ArrayList<Contact>();
//        contacts.add(contact1);

        Contact[] contacts_array = new Contact[contacts.size()];
        contacts.toArray(contacts_array);
        ListView list = (ListView) findViewById(R.id.my_contacts_list);
        ContactsListAdapter adapter = new ContactsListAdapter(this, R.layout.contacts_list, contacts_array);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {
                Adapter adapter = adapterView.getAdapter();
                Contact contact = (Contact) adapter.getItem(pos);
//                Toast.makeText(MainActivity.this, "Click sur l'item = " + contact.getFullName(),
//                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, ContactShow.class);
                intent.putExtra("CONTACT", contact);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Adapter adapter = adapterView.getAdapter();
                Contact contact = (Contact) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, ContactEdition.class);
                intent.putExtra("CONTACT_ID", contact.getId());
                startActivity(intent);
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.add_contact_button:
                //Contact creation
                Contact new_contact = new Contact("", "", "", "");
                ArrayList<Parcelable> array = new ArrayList<Parcelable>();

                array.add(new_contact);

                Intent intent = new Intent(this, ContactEdition.class);
                intent.putExtra("CONTACT", new_contact);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

}
