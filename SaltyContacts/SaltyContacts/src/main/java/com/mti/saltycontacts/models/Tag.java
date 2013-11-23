package com.mti.saltycontacts.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by lefebv_b on 19/11/13.
 */
public class Tag implements Parcelable {
    private long id;
    private String _name;
    private int type;

    public static enum Types {
        EMAIL(0), PHONE(1);

        private int numVal;

        Types(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }

    public Tag(String name) {
        this._name = name;
        this.type = 0;
    }
    public Tag() {
        this.type = 0;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the type of the tag (email or phone). Email by default.
     * @return Types
     */
    public Types getType() {
        switch (type) {
            case 0:
                return Types.EMAIL;
            case 1:
                return Types.PHONE;
        }
        return Types.EMAIL;
    }

    /**
     * Sets the type of the tag (Tag.Types.EMAIL || Tag.Types.PHONE).
     * @param Tag.Types type
     */
    public void setType(Types type) {
        this.type = type.getNumVal();
    }

    public Tag(Parcel in) {
        this.getFromParcel(in);
    }

    //On va ici hydrater notre objet à partir du Parcel
    public void getFromParcel(Parcel in) {
        this._name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("TagParcel", "writeToParcel..." + flags);
        dest.writeString(this._name);
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel source) {
            return new Tag(source);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };
}
