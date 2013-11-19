package com.mti.saltycontacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mti.saltycontacts.models.Contact;

import com.mti.saltycontacts.R;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class ContactsListAdapter extends ArrayAdapter<Contact> {
    Context _context;
    int _ressource;
    List<Contact> _adapterData;

    public ContactsListAdapter(Context context, int resource, Contact[] data) {
        super(context, resource);
        this._context = context;
        this._ressource = resource;
        this._adapterData = new ArrayList<Contact>();
        Collections.addAll(this._adapterData, data);
    }

    @Override
    public int getCount() {
        return this._adapterData.size();
    }

    @Override
    public Contact getItem(int position) {
        return this._adapterData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this._ressource, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.list_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = this._adapterData.get(position);
        holder.textView.setText(contact.getFullName());
        //holder.imageView.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }
//
    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
