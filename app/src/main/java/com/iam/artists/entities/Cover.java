package com.iam.artists.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * cover implements parcelable for saving data within artist serialization
 */

public class Cover implements Parcelable {
    private String big;
    private String small;

    public Cover (JSONObject json) {
        try {
            big = json.getString("big");
        } catch (JSONException e) {
            big = "";
        }
        try {
            small = json.getString("small");
        } catch (JSONException e) {
            small = "";
        }
    }

    protected Cover(Parcel in) {
        big = in.readString();
        small = in.readString();
    }

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    public String big() {
        return big;
    }

    public String small() {
        return small;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(big);
        dest.writeString(small);
    }
}
