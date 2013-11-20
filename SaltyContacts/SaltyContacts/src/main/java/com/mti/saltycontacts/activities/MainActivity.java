package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import com.mti.saltycontacts.models.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Contact> contacts = new ArrayList<Contact>();

        Tag tag1 = new Tag("Maison");
        Tag tag2 = new Tag("Perso");
        PhoneNumber phoneNumber1 = new PhoneNumber("01.11.11.11.11", tag1);
        EmailAddress emailAddress1 = new EmailAddress("vinc.lefebv@gmail.com", tag2);

        Contact contact1 = new Contact("Vincent", "Lefebvre", "Villejuif Paul Vaillant", "url1");
        contact1.addPhoneNumber(phoneNumber1);
        contact1.addEmailAddress(emailAddress1);

        contacts.add(contact1);

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
                Toast.makeText(MainActivity.this, "Click sur un item = " + contact.getFullName(),
                        Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(this, ContactEdition.class);
                Log.d("ANTOINE", "trying to start");
                startActivity(intent);
                Log.d("ANTOINE", "tried to start");
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
