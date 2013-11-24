package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.adapters.EmailsAdapter;
import com.mti.saltycontacts.adapters.PhoneNumbersAdapter;
import com.mti.saltycontacts.business.ImageManager;
import com.mti.saltycontacts.dataAccess.DataManager;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;
import com.mti.saltycontacts.utils.Helper;

/**
 * Created by lefebv_b on 20/11/13.
 */
public class ContactShow extends Activity implements View.OnClickListener {

    private Long _contact_id;
    private Contact _contact;
    private TextView _address;
    DataManager _dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_contact_show);

        this._dataManager = DataManager.getInstance(ContactShow.this);
        this._address = (TextView) findViewById(R.id.contact_show_address);
        this._address.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this._contact_id = bundle.getLong("CONTACT_ID");
            this._contact = this._dataManager.getContact(this._contact_id);
            this.fillContactShow();
            this.managePhoneNumbers();
            this.manageEmailsAddress();
        } else {
            this._contact = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this._contact = this._dataManager.getContact(this._contact_id);

        if (this._contact == null) {
            finish();
        } else {
            this.fillContactShow();
            this.managePhoneNumbers();
            this.manageEmailsAddress();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent2 = new Intent(ContactShow.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(ContactShow.this, ContactEdition.class);
                intent.putExtra("CONTACT_ID", this._contact.getId());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillContactShow() {
        if (this._contact != null) {
            TextView firstname = (TextView) findViewById(R.id.contact_show_firstname);
            TextView lastname = (TextView) findViewById(R.id.contact_show_lastname);

            ImageView image = (ImageView) findViewById(R.id.contact_show_picture);

            firstname.setText(this._contact.getFirstName());
            lastname.setText(this._contact.getLastName());
            this._address.setText(this._contact.getPostalAddress());

            if (this._contact.getPictureUrl() != "") {
                Bitmap bitmap = ImageManager.bitmapFromUrl(ContactShow.this, this._contact.getPictureUrl());
                image.setImageBitmap(bitmap);
            }
        }
    }

    private void managePhoneNumbers() {
        PhoneNumber[] phone_numbers_array = new PhoneNumber[this._contact.getPhoneNumbers().size()];
        this._contact.getPhoneNumbers().toArray(phone_numbers_array);
        ListView list = (ListView) findViewById(R.id.phone_numbers_list);
        PhoneNumbersAdapter adapter = new PhoneNumbersAdapter(this, R.layout.phone_numbers_list, phone_numbers_array);
        list.setAdapter(adapter);
        Helper.getListViewSize(list);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {
                Adapter adapter = adapterView.getAdapter();
                PhoneNumber phoneNumber = (PhoneNumber) adapter.getItem(pos);
                try {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber.getNumber()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("contactshow", "Call failed", e);
                }
            }
        });
    }

    private void manageEmailsAddress() {
        EmailAddress[] email_address_array = new EmailAddress[this._contact.getEmailListAddress().size()];
        this._contact.getEmailListAddress().toArray(email_address_array);
        ListView list = (ListView) findViewById(R.id.emails_address_list);

        EmailsAdapter adapter = new EmailsAdapter(this, R.layout.emails_list, email_address_array);
        list.setAdapter(adapter);
        Helper.getListViewSize(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int pos, long l) {
                Adapter adapter = adapterView.getAdapter();
                EmailAddress emailAddress = (EmailAddress) adapter.getItem(pos);

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress.getAddress()});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, getString(R.string.email_call_message)));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_show_address:
                String postalAddress = this._contact.getPostalAddress();
                if (postalAddress != null && postalAddress != "") {
                    String newPostalAddress = postalAddress.replace(' ', '+');
                    String format = "geo:0,0?q=" + newPostalAddress;
                    Uri uri = Uri.parse(format);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;
        String LOG_TAG = "contactshow";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {
                    Log.i(LOG_TAG, "restart app");
                    // restart app
                    Intent i = new Intent(getApplicationContext(), ContactShow.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("CONTACT", _contact);
                    startActivity(i);

                    isPhoneCalling = false;
                }
            }
        }
    }
}
