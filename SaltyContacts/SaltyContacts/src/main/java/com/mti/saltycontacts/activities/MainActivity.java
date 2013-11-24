package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.ContactsListAdapter;
import com.mti.saltycontacts.dataAccess.DataManager;
import com.mti.saltycontacts.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    List<Contact> contacts;
    DataManager dataManager;

    LinearLayout loading_layout;
    ListView list;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = DataManager.getInstance(MainActivity.this);
        this.contacts = dataManager.getContacts();

    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();

    }

    private void refresh() {
        Contact[] contacts_array = new Contact[contacts.size()];
        contacts.toArray(contacts_array);
        list = (ListView) findViewById(R.id.my_contacts_list);
        loading_layout = (LinearLayout) findViewById(R.id.loading_content);
        ContactsListAdapter adapter = new ContactsListAdapter(this, R.layout.contacts_list, contacts_array);
        list.setAdapter(adapter);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

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
            case R.id.action_import_contacts:
                ProgressAsyncTask contact_loader = new ProgressAsyncTask();
                contact_loader.execute();
                refresh();
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

    public class ProgressAsyncTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading_layout.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            progressBar.setMax(100);
        }

        public void progress(int value) {
            this.publishProgress(value);
            progressBar.setProgress(value);
        }

        @Override
        protected Void doInBackground(Void... params) {
            dataManager.fillContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading_layout.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            refresh();
        }


    }
}
