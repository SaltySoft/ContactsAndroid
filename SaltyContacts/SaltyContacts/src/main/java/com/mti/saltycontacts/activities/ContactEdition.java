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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.ContactsListAdapter;
import com.mti.saltycontacts.adapters.PhoneNumbersEditionAdapter;
import com.mti.saltycontacts.dataAccess.ContactsBDD;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.PhoneNumber;
import com.mti.saltycontacts.models.Tag;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ContactEdition extends Activity implements View.OnClickListener {

    Contact contact;

    EditText firstname_input;
    EditText lastname_input;
    EditText address_input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edition);

        firstname_input = (EditText) findViewById(R.id.edition_firstname_input);
        lastname_input = (EditText) findViewById(R.id.edition_lastname_input);
        address_input = (EditText) findViewById(R.id.edition_address_input);

        Button save_button = (Button) findViewById(R.id.edition_save_button);
        save_button.setOnClickListener(this);



        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.contact = (Contact) bundle.getParcelable("CONTACT");
            this.fillForm();
        }

        Tag tag = new Tag("tag");
        PhoneNumber pn = new PhoneNumber("03030303", tag);
        contact.addPhoneNumber(pn);



        PhoneNumber[] phone_numbers_array = new PhoneNumber[contact.getPhoneNumbers().size()];
        contact.getPhoneNumbers().toArray(phone_numbers_array);
        ListView list = (ListView) findViewById(R.id.edition_phone_list);
        PhoneNumbersEditionAdapter adapter = new PhoneNumbersEditionAdapter(this, R.layout.phone_list, phone_numbers_array);
        list.setAdapter(adapter);


    }

    private void fillForm() {
        if (this.contact != null) {
            firstname_input.setText(this.contact.getFirstName());
            lastname_input.setText(this.contact.getLastName());
            address_input.setText(this.contact.getPostalAddress());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_edition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edition_save_button:

                this.contact.setFirstName(firstname_input.getText().toString());
                this.contact.setLastName(lastname_input.getText().toString());
                this.contact.setPostalAddress(address_input.getText().toString());

                ContactsBDD contactsBDD = new ContactsBDD(this);
                contactsBDD.openForWrite();
                contactsBDD.insertOrUpdateContact(this.contact);

                Intent intent = new Intent(ContactEdition.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("contact_edited", this.contact);
                startActivity(intent);
                break;
        }
    }
}
