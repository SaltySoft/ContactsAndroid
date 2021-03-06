package com.mti.saltycontacts.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class EmailAddress implements Parcelable {
    private long id;
    private String _address;
    private String _tag;

    public EmailAddress() {
        this._address = "";
        this._tag = "";
        this.id = 0;
    }

    public EmailAddress(String address, String tag) {
        this._address = address;
        this._tag = tag;
    }

    public void setAddress(String address) {
        this._address = address;
    }

    public void setTag(String tag) {
        this._tag = tag;
    }

    public String getAddress() {
        return this._address;
    }

    public String getTag() {
        return this._tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EmailAddress(Parcel in) {
        this.getFromParcel(in);
    }

    //On va ici hydrater notre objet à partir du Parcel
    public void getFromParcel(Parcel in) {
        this._address = in.readString();
        this._tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("EmailAddressParcel", "writeToParcel..." + flags);
        dest.writeString(this._address);
        dest.writeString(this._tag);
    }

    public static final Creator<EmailAddress> CREATOR = new Creator<EmailAddress>() {
        @Override
        public EmailAddress createFromParcel(Parcel source) {
            return new EmailAddress(source);
        }

        @Override
        public EmailAddress[] newArray(int size) {
            return new EmailAddress[size];
        }
    };
}
