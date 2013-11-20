package com.mti.saltycontacts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.models.Contact;

/**
 * Created by lefebv_b on 20/11/13.
 */
public class ContactShow extends Activity implements View.OnClickListener {

    private Contact _contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edition);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            this._contact = (Contact) bundle.getParcelable("CONTACT");
        }
        Button go_edit_button = (Button) findViewById(R.id.show_edit_button);
        go_edit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_edit_button:
                Intent intent = new Intent(ContactShow.this, ContactEdition.class);
                intent.putExtra("CONTACT", this._contact);
                startActivity(intent);
                break;
        }
    }
}
