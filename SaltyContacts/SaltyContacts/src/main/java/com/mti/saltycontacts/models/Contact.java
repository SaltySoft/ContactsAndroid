package com.mti.saltycontacts.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.mti.saltycontacts.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class Contact implements Parcelable {

    private long id;
    private String android_id;
    private String _firstname;
    private String _lastname;
    private String _postalAddress;
    private String _picture_url;
    private ArrayList<PhoneNumber> _phoneNumbers;
    private ArrayList<EmailAddress> _emailsAddress;

    private boolean _modified = false;

    public Contact() {
        this.id = 0;
        this._firstname = "";
        this._lastname = "";
        this._postalAddress = "";
        this._picture_url = "";
        this._phoneNumbers = new ArrayList<PhoneNumber>();
        this._emailsAddress = new ArrayList<EmailAddress>();
    }

    public Contact(String firstname, String lastname, String postalAddress, String pictureUrl) {
        this.id = 0;
        this._firstname = firstname;
        this._lastname = lastname;
        this._postalAddress = postalAddress;
        this._picture_url = pictureUrl;
        this._phoneNumbers = new ArrayList<PhoneNumber>();
        this._emailsAddress = new ArrayList<EmailAddress>();
    }

    public String getFullName() {
        return this._firstname + " " + this._lastname;
    }

    public String getFirstName() {
        return this._firstname;
    }

    public String getLastName() {
        return this._lastname;
    }

    public String getPostalAddress() {
        return this._postalAddress;
    }

    public String getPictureUrl() {
        return this._picture_url;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return this._phoneNumbers;
    }

    public List<EmailAddress> getEmailListAddress() {
        return this._emailsAddress;
    }

    public void setFirstName(String firstName) {
        this._firstname = firstName;
    }

    public void setLastName(String lastName) {
        this._lastname = lastName;
    }

    public void setPostalAddress(String postalAddress) {
        this._postalAddress = postalAddress;
    }

    public void setPictureUrl(String pictureUrl) {
        this._picture_url = pictureUrl;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setPhoneNumbers(ArrayList<PhoneNumber> _phoneNumbers) {
        this._phoneNumbers = _phoneNumbers;
    }

    public ArrayList<EmailAddress> getEmailsAddress() {
        return _emailsAddress;
    }

    public void setEmailsAddress(ArrayList<EmailAddress> _emailsAddress) {
        this._emailsAddress = _emailsAddress;
    }

    public boolean isModified() {
        return _modified;
    }

    public void setModified(boolean _modified) {
        this._modified = _modified;
    }

    public String getAndroidId() {
        return android_id;
    }

    public void setAndroidId(String android_id) {
        this.android_id = android_id;
    }

    public boolean addPhoneNumber(PhoneNumber phoneNumber) {
        if (this._phoneNumbers != null) {
            this._phoneNumbers.add(phoneNumber);
            return true;
        }

        return false;
    }

    public boolean removePhoneNumber(PhoneNumber phoneNumber) {
        return this._phoneNumbers.remove(phoneNumber);
    }

    public boolean addEmailAddress(EmailAddress emailAddress) {
        if (this._emailsAddress != null) {
            this._emailsAddress.add(emailAddress);
            return true;
        }

        return false;
    }

    public boolean removeEmailAddress(EmailAddress emailAddress) {
        return this._emailsAddress.remove(emailAddress);
    }

    public Contact(Parcel in) {
        this.id = 0;
        this.getFromParcel(in);
    }

    //On va ici hydrater notre objet à partir du Parcel
    public void getFromParcel(Parcel in) {
        this.id = in.readLong();
        this._firstname = in.readString();
        this._lastname = in.readString();
        this._postalAddress = in.readString();
        this._picture_url = in.readString();
        this._phoneNumbers = new ArrayList<PhoneNumber>();
        this._emailsAddress = new ArrayList<EmailAddress>();
        in.readList(this._phoneNumbers, getClass().getClassLoader());
        in.readList(this._emailsAddress, getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("ContactParcel", "writeToParcel..." + flags);
        dest.writeLong(this.id);
        dest.writeString(this._firstname);
        dest.writeString(this._lastname);
        dest.writeString(this._postalAddress);
        dest.writeString(this._picture_url);
        dest.writeList(this._phoneNumbers);
        dest.writeList(this._emailsAddress);
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public void copy(Contact c) {
        c._firstname = this._firstname;
        c._lastname = this._lastname;
        c._postalAddress = this._postalAddress;
        c._phoneNumbers = this._phoneNumbers;
        c._emailsAddress = this._emailsAddress;
        c.android_id = this.android_id;
        c._picture_url = this._picture_url;
        c.id = this.id;
    }
}
