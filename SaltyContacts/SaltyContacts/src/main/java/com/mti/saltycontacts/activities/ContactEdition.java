package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mti.saltycontacts.R;
import com.mti.saltycontacts.business.ImageManager;
import com.mti.saltycontacts.dataAccess.DataManager;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;

public class ContactEdition extends Activity implements View.OnClickListener {

    Contact contact;
    EditText firstname_input;
    EditText lastname_input;
    EditText address_input;
    LinearLayout phone_list;
    DataManager dataManager;
    LinearLayout email_list;
    ImageView pictureChooser;

    Uri selectedImageUri;
    String selectedPath;

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
        email_list = (LinearLayout) findViewById(R.id.edition_email_list);
        pictureChooser = (ImageView) findViewById(R.id.edition_user_image_button);
        pictureChooser.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this.contact = dataManager.getContact(bundle.getLong("CONTACT_ID"));
//            EmailAddress address = new EmailAddress("address@someserver.co", "");
//            contact.addEmailAddress(address);
            this.fillForm();
        }
        if (contact == null) {
            contact = new Contact();
        }


        ImageButton add_phone_button = (ImageButton) findViewById(R.id.edition_add_phone_button);
        add_phone_button.setOnClickListener(this);
        ImageButton add_email_button = (ImageButton) findViewById(R.id.edition_add_email_button);
        add_email_button.setOnClickListener(this);


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

    private void renderEmailList() {
        if (this.email_list.getChildCount() > 0) {
            this.email_list.removeAllViews();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        for (EmailAddress address : contact.getEmailListAddress()) {
            EmailFragment fragment = new EmailFragment(address);
            ft.add(R.id.edition_email_list, fragment);
        }
        ft.commit();
    }

    private void addEmailFragment(EmailAddress address) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EmailFragment fragment = new EmailFragment(address);
        ft.add(R.id.edition_email_list, fragment);
        ft.commit();
    }

    private void addPhoneNumberFragment(PhoneNumber number) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PhoneFragment pf = new PhoneFragment(number);
        ft.add(R.id.edition_phone_list, pf);
        ft.commit();
    }

    private void removeFragment(Fragment f) {
        this.getFragmentManager().beginTransaction().remove(f).commit();
    }

    private void fillForm() {
        if (this.contact != null) {
            firstname_input.setText(this.contact.getFirstName());
            lastname_input.setText(this.contact.getLastName());
            address_input.setText(this.contact.getPostalAddress());
            selectedPath = this.contact.getPictureUrl();
            setImage(this.contact.getPictureUrl());
            renderPhoneList();
            renderEmailList();
        }
    }

    public void setImage(String url) {
        Bitmap image = ImageManager.bitmapFromUrl(ContactEdition.this, url);
        pictureChooser.setImageBitmap(image);
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
        switch (item.getItemId()) {
            case R.id.action_save:
                saveContact();
                break;
            case R.id.action_cancel:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveContact() {
        this.contact.setFirstName(firstname_input.getText().toString());
        this.contact.setLastName(lastname_input.getText().toString());
        this.contact.setPostalAddress(address_input.getText().toString());
        this.contact.setPictureUrl(selectedPath);

        contact = dataManager.persist(contact);

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edition_add_phone_button:
                PhoneNumber pn = new PhoneNumber("", "Mobile");
                contact.addPhoneNumber(pn);
                this.addPhoneNumberFragment(pn);
                break;
            case R.id.edition_add_email_button:
                EmailAddress address = new EmailAddress("", "Personal");
                contact.addEmailAddress(address);
                this.addEmailFragment(address);
                break;
            case R.id.edition_user_image_button:
                ChoosePictureDialogFragment fragment = new ChoosePictureDialogFragment();
                fragment.show(getFragmentManager(), "ChoosePictureDialog");
                break;
        }
    }

    public void openGallery(int req_code) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void openCamera(int req_code) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                setImage(selectedPath);
                break;
            case 1 :
                selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                setImage(selectedPath);
                break;
        }
    }



    public String getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
            View view = inflater.inflate(R.layout.edition_phone_fragment, container, false);
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
                case R.id.phone_delete_button:
                    contact.removePhoneNumber(phone_number);
                    removeFragment(this);
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

    public class EmailFragment extends Fragment implements View.OnClickListener {

        private EmailAddress email_address;
        private EditText input;
        private TextView display;
        private ImageButton edit_button;
        private ImageButton delete_button;
        private ImageButton validate_button;

        public EmailFragment(EmailAddress address) {
            this.email_address = address;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.edition_email_fragment, container, false);
            this.display = (TextView) view.findViewById(R.id.email_container);
            this.display.setText(email_address.getAddress());

            this.input = (EditText) view.findViewById(R.id.email_input);

            this.edit_button = (ImageButton) view.findViewById(R.id.email_edit_button);
            this.edit_button.setOnClickListener(this);

            this.delete_button = (ImageButton) view.findViewById(R.id.email_delete_button);
            this.delete_button.setOnClickListener(this);

            this.validate_button = (ImageButton) view.findViewById(R.id.email_validate_button);
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
                case R.id.email_edit_button:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(email_address.getAddress());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    break;
                case R.id.email_delete_button:
                    contact.removeEmailAddress(email_address);
                    removeFragment(this);
                    break;
                case R.id.email_validate_button:
                    this.display.setVisibility(View.VISIBLE);
                    this.input.setVisibility(View.GONE);
                    this.email_address.setAddress(input.getText().toString());
                    this.display.setText(email_address.getAddress());
                    this.validate_button.setVisibility(View.GONE);
                    this.edit_button.setVisibility(View.VISIBLE);
                    this.delete_button.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    break;
            }
        }
    }

    public class ChoosePictureDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("How do you want to get your picture ?")
                    .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openCamera(0);
                        }
                    })
                    .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openGallery(1);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
