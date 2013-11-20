package com.mti.saltycontacts.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.mti.saltycontacts.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class Contact implements Parcelable {
    private String _firstname;
    private String _lastname;
    private String _postalAddress;
    private String _picture_url;
    private List<PhoneNumber> _phoneNumbers;
    private List<EmailAddress> _emailsAddress;

    public Contact() {
        this._firstname = "";
        this._lastname = "";
        this._postalAddress = "";
        this._picture_url = "";
        this._phoneNumbers = new ArrayList<PhoneNumber>();
        this._emailsAddress = new ArrayList<EmailAddress>();
    }

    public Contact(String firstname, String lastname, String postalAddress, String pictureUrl) {
        this._firstname = firstname;
        this._lastname = lastname;
        this._postalAddress = postalAddress;
        this._picture_url = pictureUrl;
        this._phoneNumbers = new ArrayList<PhoneNumber>();
        this._emailsAddress = new ArrayList<EmailAddress>();
    }

    public String getFullName() {
        return this._lastname + " " + this._firstname;
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

    public List<EmailAddress> getEmailAddress() {
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
        this.getFromParcel(in);
    }

    //On va ici hydrater notre objet à partir du Parcel
    public void getFromParcel(Parcel in) {
        this._firstname = in.readString();
        this._lastname = in.readString();
        this._postalAddress = in.readString();
        this._picture_url = in.readString();
        this._phoneNumbers = new ArrayList<PhoneNumber>();
//        this._phoneNumbers.clear();
        for (int i = 0; i < this._phoneNumbers.size(); i++) {
            PhoneNumber phoneNumber = in.readParcelable(PhoneNumber.class.getClassLoader());
            this._phoneNumbers.add(phoneNumber);
        }
        this._emailsAddress = new ArrayList<EmailAddress>();
//        this._emailsAddress.clear();
        for (int i = 0; i < this._emailsAddress.size(); i++) {
            EmailAddress emailsAddress = in.readParcelable(EmailAddress.class.getClassLoader());
            this._emailsAddress.add(emailsAddress);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("ContactParcel", "writeToParcel..." + flags);
        dest.writeString(this._firstname);
        dest.writeString(this._lastname);
        dest.writeString(this._postalAddress);
        dest.writeString(this._picture_url);
        for (int i = 0; i < this._phoneNumbers.size(); i++) {
            dest.writeParcelable(this._phoneNumbers.get(i), flags);
        }
        for (int i = 0; i < this._emailsAddress.size(); i++) {
            dest.writeParcelable(this._emailsAddress.get(i), flags);
        }
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
}
