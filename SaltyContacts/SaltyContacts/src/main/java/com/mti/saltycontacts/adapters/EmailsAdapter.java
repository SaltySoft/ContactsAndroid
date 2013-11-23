package com.mti.saltycontacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mti.saltycontacts.R;
import com.mti.saltycontacts.models.EmailAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by lefebv_b on 22/11/13.
 */
public class EmailsAdapter extends ArrayAdapter<EmailAddress> {
    Context _context;
    int _ressource;
    List<EmailAddress> _adapterData;

    public EmailsAdapter(Context context, int resource, EmailAddress[] data) {
        super(context, resource);
        this._context = context;
        this._ressource = resource;
        this._adapterData = new ArrayList<EmailAddress>();
        Collections.addAll(this._adapterData, data);
    }

    @Override
    public int getCount() {
        return this._adapterData.size();
    }

    @Override
    public EmailAddress getItem(int position) {
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
            holder.textViewTag = (TextView) convertView.findViewById(R.id.emails_tag_list_text);
            holder.textViewAddress = (TextView) convertView.findViewById(R.id.emails_list_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EmailAddress emailAddress = this._adapterData.get(position);
        holder.textViewTag.setText(emailAddress.getTag());
        holder.textViewAddress.setText(emailAddress.getAddress());

        return convertView;
    }

    private class ViewHolder {
        TextView textViewTag;
        TextView textViewAddress;
    }
}
