package com.mti.saltycontacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.PhoneNumber;
import com.mti.saltycontacts.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lefebv_b on 20/11/13.
 */
public class PhoneNumbersAdapter extends ArrayAdapter<PhoneNumber> {
    Context _context;
    int _ressource;
    List<PhoneNumber> _adapterData;

    public PhoneNumbersAdapter(Context context, int resource, PhoneNumber[] data) {
        super(context, resource);
        this._context = context;
        this._ressource = resource;
        this._adapterData = new ArrayList<PhoneNumber>();
        Collections.addAll(this._adapterData, data);
    }

    @Override
    public int getCount() {
        return this._adapterData.size();
    }

    @Override
    public PhoneNumber getItem(int position) {
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
            holder.textViewTag = (TextView) convertView.findViewById(R.id.phone_numbers_tag_list_text);
            holder.textViewNumber = (TextView) convertView.findViewById(R.id.phone_numbers_list_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhoneNumber phoneNumber = this._adapterData.get(position);
        holder.textViewTag.setText(phoneNumber.getTag().getName());
        holder.textViewNumber.setText(phoneNumber.getNumber());

        return convertView;
    }

    private class ViewHolder {
        TextView textViewTag;
        TextView textViewNumber;
    }
}
