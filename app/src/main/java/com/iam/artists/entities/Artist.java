package com.iam.artists.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.iam.artists.utils.Corrector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * entity artist implements parcelable for put it in intent opens artist page activity
 */

public class Artist implements Parcelable {

    private String jsonString;
    private long id;
    private String name;
    private String[] genres;
    private int tracks;
    private int albums;
    private String description;
    private String url;
    private Cover cover;

    public Artist (JSONObject json) {
        jsonString = json.toString();
        try {
            id = json.getLong("id");
            name = json.getString("name");
            JSONArray jGenres = json.getJSONArray("genres");
            int l = jGenres.length();
            genres = new String[l];
            for (int i = 0; i < l; i++) {
                genres[i] = jGenres.getString(i);
            }
            tracks = json.getInt("tracks");
            albums = json.getInt("albums");
            description = json.getString("description");
            cover = new Cover(json.getJSONObject("cover"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            url = json.getString("link");
        } catch (JSONException e) {
            url = "";
        }
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        genres = in.createStringArray();
        tracks = in.readInt();
        albums = in.readInt();
        description = in.readString();
        url = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    public Cover cover() { return cover; }
    public String tracks() { return
            albums + " " +
                    Corrector.albumsCountInGenetive(albums) + ", " +
                    tracks + " " + Corrector.songsCountInGenetive(tracks);
    }
    public String name() { return name; }
    public String genres() {
        String genres = Arrays.toString(this.genres);
        genres = genres.substring(1, genres.length() - 1);
        return genres;
    }
    public String description() { return description; }

    public String json() {
        return jsonString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeStringArray(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeParcelable(cover, flags);
    }
}
