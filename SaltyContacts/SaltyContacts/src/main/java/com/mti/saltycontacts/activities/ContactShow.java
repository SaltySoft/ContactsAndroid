package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.ContactsListAdapter;
import com.mti.saltycontacts.adapters.PhoneNumbersAdapter;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.PhoneNumber;

/**
 * Created by lefebv_b on 20/11/13.
 */
public class ContactShow extends Activity implements View.OnClickListener {
    private Contact _contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.contactShowTheme);
        setContentView(R.layout.activity_contact_show);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this._contact = (Contact) bundle.getParcelable("CONTACT");
            this.fillContactShow();
        } else {
            this._contact = null;
        }
        Button go_edit_button = (Button) findViewById(R.id.show_edit_button);
        go_edit_button.setOnClickListener(this);

        Button go_back_button = (Button) findViewById(R.id.show_back_button);
        go_back_button.setOnClickListener(this);
    }

    private void fillContactShow() {
        if (this._contact != null) {
            TextView firstname = (TextView) findViewById(R.id.contact_show_firstname);
            TextView lastname = (TextView) findViewById(R.id.contact_show_lastname);
            TextView address = (TextView) findViewById(R.id.contact_show_address);

            firstname.setText(this._contact.getFirstName());
            lastname.setText(this._contact.getLastName());
            address.setText(this._contact.getPostalAddress());

            PhoneNumber[] phone_numbers_array = new PhoneNumber[this._contact.getPhoneNumbers().size()];
            this._contact.getPhoneNumbers().toArray(phone_numbers_array);
            ListView list = (ListView) findViewById(R.id.phone_numbers_list);
            PhoneNumbersAdapter adapter = new PhoneNumbersAdapter(this, R.layout.phone_numbers_list, phone_numbers_array);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_edit_button:
                Intent intent = new Intent(ContactShow.this, ContactEdition.class);
                intent.putExtra("CONTACT", this._contact);
                startActivity(intent);
                break;
            case R.id.show_back_button:
                Intent intent2 = new Intent(ContactShow.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }
    }
}
