package com.mti.saltycontacts.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.mti.saltycontacts.models.Contact;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class ContactsListAdapter extends ArrayAdapter<Contact> {
    Context _context;
    int _ressource;
    Contact[] _adapterData;

    public ContactsListAdapter(Context context, int resource, Contact[] data) {
        super(context, resource);
        this._context = context;
        this._ressource = resource;
        this._adapterData = data;
    }

    @Override
    public int getCount() {
        return this._adapterData.length;
    }

    @Override
    public Contact getItem(int position) {
        return this._adapterData[position];
    }
}
