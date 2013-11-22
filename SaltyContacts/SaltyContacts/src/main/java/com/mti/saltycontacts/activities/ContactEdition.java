package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.ContactsListAdapter;
import com.mti.saltycontacts.adapters.PhoneNumbersEditionAdapter;
import com.mti.saltycontacts.dataAccess.ContactsBDD;
import com.mti.saltycontacts.dataAccess.DataManager;
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
    LinearLayout phone_list;
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = DataManager.getInstance(ContactEdition.this);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_contact_edition);

        firstname_input = (EditText) findViewById(R.id.edition_firstname_input);
        lastname_input = (EditText) findViewById(R.id.edition_lastname_input);
        address_input = (EditText) findViewById(R.id.edition_address_input);
        this.phone_list = (LinearLayout) findViewById(R.id.edition_phone_list);

        Button save_button = (Button) findViewById(R.id.edition_save_button);
        save_button.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.contact = dataManager.getContact(bundle.getLong("CONTACT_ID"));
            this.fillForm();
        }
        if (contact == null) {
            contact = new Contact();
        }


        ImageButton add_phone_button = (ImageButton) findViewById(R.id.edition_add_phone_button);
        add_phone_button.setOnClickListener(this);
    }

    private void renderPhoneList() {
        if (this.phone_list.getChildCount() > 0) {
            this.phone_list.removeAllViews();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < this.contact.getPhoneNumbers().size(); i++) {
            PhoneNumber pn = this.contact.getPhoneNumbers().get(i);
            PhoneFragment pf = new PhoneFragment(pn);
            ft.add(R.id.edition_phone_list, pf);
        }
        ft.commit();
    }

    private void addPhoneNumberFragment(PhoneNumber number) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PhoneFragment pf = new PhoneFragment(number);
        ft.add(R.id.edition_phone_list, pf);
        ft.commit();
    }

    private void fillForm() {
        if (this.contact != null) {
            firstname_input.setText(this.contact.getFirstName());
            lastname_input.setText(this.contact.getLastName());
            address_input.setText(this.contact.getPostalAddress());
            renderPhoneList();
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

                contact = dataManager.persist(contact);

                Intent intent = new Intent(ContactEdition.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.edition_add_phone_button:
                Tag tag = new Tag("tag");
                PhoneNumber pn = new PhoneNumber("123123123", tag);
                contact.addPhoneNumber(pn);
                this.addPhoneNumberFragment(pn);
                break;
        }
    }

    public class PhoneFragment extends Fragment implements View.OnClickListener {

        private PhoneNumber phone_number;
        private EditText input;
        private TextView display;
        private ImageButton edit_button;
        private ImageButton delete_button;
        private ImageButton validate_button;

        public PhoneFragment(PhoneNumber phone) {
            this.phone_number = phone;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.phone_list, container, false);
            this.display = (TextView) view.findViewById(R.id.phone_number_container);
            this.display.setText(phone_number.getNumber());

            this.input = (EditText) view.findViewById(R.id.phone_number_input);

            this.edit_button = (ImageButton) view.findViewById(R.id.phone_edit_button);
            this.edit_button.setOnClickListener(this);

            this.delete_button = (ImageButton) view.findViewById(R.id.phone_delete_button);
            this.delete_button.setOnClickListener(this);

            this.validate_button = (ImageButton) view.findViewById(R.id.phone_validate_button);
            this.validate_button.setOnClickListener(this);

            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.phone_edit_button:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(phone_number.getNumber());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    break;
                case R.id.phone_validate_button:
                    this.display.setVisibility(View.VISIBLE);
                    this.input.setVisibility(View.GONE);
                    this.phone_number.setNumber(input.getText().toString());
                    this.display.setText(phone_number.getNumber());
                    this.validate_button.setVisibility(View.GONE);
                    this.edit_button.setVisibility(View.VISIBLE);
                    this.delete_button.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    break;
            }
        }
    }
}
