package com.mti.saltycontacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.models.Contact;
import com.mti.saltycontacts.models.PhoneNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Antoine on 11/20/13.
 */
public class PhoneNumbersEditionAdapter extends ArrayAdapter<PhoneNumber> {
    Context _context;
    int _ressource;
    List<PhoneNumber> _adapterData;

    public PhoneNumbersEditionAdapter(Context context, int resource, PhoneNumber[] data) {
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
            holder.textView = (TextView) convertView.findViewById(R.id.phone_number_container);
            holder.phoneInput = (EditText) convertView.findViewById(R.id.phone_number_input);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhoneNumber phone = this._adapterData.get(position);
        holder.textView.setText(phone.getNumber());
        //holder.imageView.setImageResource(R.drawable.ic_launcher);

        return convertView;
    }
    //
    private class ViewHolder {
        TextView textView;
        ImageView imageView;
        EditText phoneInput;
    }
}
