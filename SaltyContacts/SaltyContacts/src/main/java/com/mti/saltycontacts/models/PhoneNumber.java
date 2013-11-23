package com.mti.saltycontacts.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.mti.saltycontacts.R;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class PhoneNumber implements Parcelable {
    private long id;
    private String _number;
    private String _tag;

    public PhoneNumber(String number, String tag) {
        this._number = number;
        this._tag = tag;
    }

    public PhoneNumber() {

    }

    public void setNumber(String number) {
        this._number = number;
    }

    public void setTag(String tag) {
        this._tag = tag;
    }

    public String getNumber() {
        return this._number;
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

    public PhoneNumber(Parcel in) {
        this.getFromParcel(in);
    }

    //On va ici hydrater notre objet à partir du Parcel
    public void getFromParcel(Parcel in) {
        this._number = in.readString();
        this._tag = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("PhoneNumberParcel", "writeToParcel..." + flags);
        dest.writeString(this._number);
        dest.writeString(this._tag);
    }

    public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
        @Override
        public PhoneNumber createFromParcel(Parcel source) {
            return new PhoneNumber(source);
        }

        @Override
        public PhoneNumber[] newArray(int size) {
            return new PhoneNumber[size];
        }
    };
}
