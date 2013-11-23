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
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ContactsBDD contactsBDD = new ContactsBDD(this);
//        contactsBDD.openForRead();
//
//        contacts = contactsBDD.getAllContacts();
//        contactsBDD.close();

        dataManager = DataManager.getInstance(MainActivity.this);
        this.contacts = dataManager.getContacts();
    }


    @Override
    protected void onResume() {
        super.onResume();

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
                Intent intent = new Intent(MainActivity.this, ContactShow.class);
                intent.putExtra("CONTACT_ID", contact.getId());
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
