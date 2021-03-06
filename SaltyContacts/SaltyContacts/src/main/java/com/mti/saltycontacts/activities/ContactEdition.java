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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.business.ImageManager;
import com.mti.saltycontacts.dataAccess.DataManager;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.EmailAddress;
import com.mti.saltycontacts.models.PhoneNumber;
import com.mti.saltycontacts.views.QuestionDialog;

public class ContactEdition extends Activity implements View.OnClickListener {

    Contact contact;
    EditText firstname_input;
    EditText lastname_input;
    EditText address_input;
    LinearLayout phone_list;
    DataManager dataManager;
    LinearLayout email_list;
    ImageButton pictureChooser;
    Button deletionButton;

    Uri selectedImageUri;
    String selectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        dataManager = DataManager.getInstance(ContactEdition.this);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_contact_edition);

        firstname_input = (EditText) findViewById(R.id.edition_firstname_input);
        lastname_input = (EditText) findViewById(R.id.edition_lastname_input);
        address_input = (EditText) findViewById(R.id.edition_address_input);
        this.phone_list = (LinearLayout) findViewById(R.id.edition_phone_list);
        email_list = (LinearLayout) findViewById(R.id.edition_email_list);
        pictureChooser = (ImageButton) findViewById(R.id.edition_user_image_button);
        pictureChooser.setOnClickListener(this);
        deletionButton = (Button) findViewById(R.id.delete_contact_button);
        deletionButton.setOnClickListener(this);

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
        if (url != "") {
            Bitmap image = ImageManager.bitmapFromUrl(ContactEdition.this, url);
            pictureChooser.setImageBitmap(image);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                saveContact();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveContact() {
        String firstname = firstname_input.getText().toString();
        String lastname = lastname_input.getText().toString();
        String postalAddress = address_input.getText().toString();

        if (firstname.length() == 0 && lastname.length() == 0) {
            Toast.makeText(ContactEdition.this, R.string.error_name_empty,
                    Toast.LENGTH_LONG).show();
        } else {
            this.contact.setFirstName(firstname);
            this.contact.setLastName(lastname);
            this.contact.setPostalAddress(postalAddress);
            this.contact.setPictureUrl(selectedPath);

            contact = dataManager.persist(contact);

            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edition_add_phone_button:
                PhoneNumber pn = new PhoneNumber("", getString(R.string.default_phone_tag));
                contact.addPhoneNumber(pn);
                this.addPhoneNumberFragment(pn);
                break;
            case R.id.edition_add_email_button:
                EmailAddress address = new EmailAddress("", getString(R.string.default_email_tag));
                contact.addEmailAddress(address);
                this.addEmailFragment(address);
                break;
            case R.id.edition_user_image_button:
                ChoosePictureDialogFragment fragment = new ChoosePictureDialogFragment();
                fragment.show(getFragmentManager(), "ChoosePictureDialog");
                break;
            case R.id.delete_contact_button:
                QuestionDialog dialog = new QuestionDialog(getString(R.string.delete_confirm_message),
                        getString(R.string.delete_confirm_positive),
                        getString(R.string.delete_confirm_negative),
                        new QuestionDialog.QuestionHandler() {
                    @Override
                    public void positiveAction() {
                        dataManager.removeContact(contact);
                        finish();
                    }

                    @Override
                    public void negativeAction() {

                    }
                });
                dialog.show(getFragmentManager(), "DeletionDialog");

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
        if (data != null) {
            switch (requestCode) {
                case 0:
                    selectedImageUri = data.getData();
                    selectedPath = getPath(selectedImageUri);
                    setImage(selectedPath);
                    break;
                case 1:
                    selectedImageUri = data.getData();
                    selectedPath = getPath(selectedImageUri);
                    setImage(selectedPath);
                    break;
            }
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

    public class PhoneFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TagFragment {

        private PhoneNumber phone_number;
        private EditText input;
        private TextView display;
        private ImageButton edit_button;
        private ImageButton delete_button;
        private ImageButton validate_button;
        private Spinner tag_spinner;
        private String[] old_array;

        public PhoneFragment(PhoneNumber phone) {
            this.phone_number = phone;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.edition_phone_fragment, container, false);

            old_array = getResources().getStringArray(R.array.phone_defaults_tags);

            this.display = (TextView) view.findViewById(R.id.phone_number_container);
            if (phone_number.getNumber() != "") {
                this.display.setText(phone_number.getNumber());
            } else {
                this.display.setText(getString(R.string.edit_mail_phone_hint));
            }
            this.display.setOnClickListener(this);

            this.input = (EditText) view.findViewById(R.id.phone_number_input);

            this.edit_button = (ImageButton) view.findViewById(R.id.phone_edit_button);
            this.edit_button.setOnClickListener(this);

            this.delete_button = (ImageButton) view.findViewById(R.id.phone_delete_button);
            this.delete_button.setOnClickListener(this);

            this.validate_button = (ImageButton) view.findViewById(R.id.phone_validate_button);
            this.validate_button.setOnClickListener(this);

            this.tag_spinner = (Spinner) view.findViewById(R.id.tag_spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                    R.array.phone_defaults_tags, R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

            tag_spinner.setAdapter(adapter);
            tag_spinner.setOnItemSelectedListener(this);

            //Setting tag at startup
            Boolean custom_tag = true;
            int tag_to_select = -1;
            for (int i = 0; i < old_array.length; i++) {
                if (old_array[i].equals(phone_number.getTag())) {
                    custom_tag = false;
                    tag_to_select = i;
                }
            }
            if (custom_tag) {
                this.addTag(phone_number.getTag());
            } else {
                tag_spinner.setSelection(tag_to_select);
            }

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
            InputMethodManager imm;
            switch (v.getId()) {
                case R.id.phone_edit_button:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(phone_number.getNumber());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    break;
                case R.id.phone_number_container:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(phone_number.getNumber());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
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
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    break;

            }
        }

        @Override
        public void addTag(String value) {
            String[] new_array = new String[old_array.length + 1];
            for (int i = 0; i < old_array.length; i++) {
                new_array[i] = old_array[i];
            }
            new_array[new_array.length - 1] = value;
            phone_number.setTag(value);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this.getActivity().getApplicationContext(),
                    R.layout.simple_spinner_item, new_array);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            tag_spinner.setAdapter(adapter);
            tag_spinner.setSelection(old_array.length);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String elt = (String) parent.getItemAtPosition(position);

            if (elt.equals(old_array[old_array.length - 1])) {
                TagDialog dialog = new TagDialog(this);
                dialog.show(getActivity().getFragmentManager(), "TagCustomizer");
            } else {
                phone_number.setTag(elt);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class EmailFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, TagFragment {

        private EmailAddress email_address;
        private EditText input;
        private TextView display;
        private ImageButton edit_button;
        private ImageButton delete_button;
        private ImageButton validate_button;
        private Spinner tag_spinner;
        private String[] old_array;

        public EmailFragment(EmailAddress address) {
            this.email_address = address;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.edition_email_fragment, container, false);

            old_array = getResources().getStringArray(R.array.emails_defaults_tags);

            this.display = (TextView) view.findViewById(R.id.email_container);

            if (email_address.getAddress() != "") {
                this.display.setText(email_address.getAddress());
            } else {
                this.display.setText(getString(R.string.edit_mail_phone_hint));
            }
           this.display.setOnClickListener(this);

            this.input = (EditText) view.findViewById(R.id.email_input);

            this.edit_button = (ImageButton) view.findViewById(R.id.email_edit_button);
            this.edit_button.setOnClickListener(this);

            this.delete_button = (ImageButton) view.findViewById(R.id.email_delete_button);
            this.delete_button.setOnClickListener(this);

            this.validate_button = (ImageButton) view.findViewById(R.id.email_validate_button);
            this.validate_button.setOnClickListener(this);

            this.tag_spinner = (Spinner) view.findViewById(R.id.tag_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                    R.array.emails_defaults_tags, R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

            tag_spinner.setAdapter(adapter);
            tag_spinner.setOnItemSelectedListener(this);

            //Setting tag at startup
            Boolean custom_tag = true;
            int tag_to_select = -1;
            for (int i = 0; i < old_array.length; i++) {
                if (old_array[i].equals(email_address.getTag())) {
                    custom_tag = false;
                    tag_to_select = i;
                }
            }
            if (custom_tag) {
                this.addTag(email_address.getTag());
            } else {
                tag_spinner.setSelection(tag_to_select);
            }


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
            InputMethodManager imm;
            switch (v.getId()) {
                case R.id.email_edit_button:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(email_address.getAddress());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    break;
                case R.id.email_container:
                    this.display.setVisibility(View.GONE);
                    this.input.setVisibility(View.VISIBLE);
                    this.input.setText(email_address.getAddress());
                    this.validate_button.setVisibility(View.VISIBLE);
                    this.edit_button.setVisibility(View.GONE);
                    this.delete_button.setVisibility(View.GONE);
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
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
                    imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    break;
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String elt = (String) parent.getItemAtPosition(position);

            if (elt.equals(old_array[old_array.length - 1])) {
                TagDialog dialog = new TagDialog(this);
                dialog.show(getActivity().getFragmentManager(), "TagCustomizer");
            } else {
                email_address.setTag(elt);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void addTag(String value) {
            String[] new_array = new String[old_array.length + 1];
            for (int i = 0; i < old_array.length; i++) {
                new_array[i] = old_array[i];
            }
            new_array[new_array.length - 1] = value;
            email_address.setTag(value);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this.getActivity().getApplicationContext(),
                    R.layout.simple_spinner_item, new_array);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            tag_spinner.setAdapter(adapter);
            tag_spinner.setSelection(old_array.length);


        }
    }

    public class ChoosePictureDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.picture_get_question))
                    .setPositiveButton(getString(R.string.picture_from_camera_response), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openCamera(0);
                        }
                    })
                    .setNegativeButton(getString(R.string.picture_from_gallery_response), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            openGallery(1);
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    public class TagDialog extends DialogFragment {
        TagFragment context;

        EditText tag_value;

        public TagDialog(TagFragment context) {
            this.context = context;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final View view = inflater.inflate(R.layout.tag_dialog, null);
            builder.setView(view)
                    .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            tag_value = (EditText) view.findViewById(R.id.tag_input);
                            context.addTag(tag_value.getText().toString());
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


}
