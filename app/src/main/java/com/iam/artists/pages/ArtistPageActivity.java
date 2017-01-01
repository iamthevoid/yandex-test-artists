package com.iam.artists.pages;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iam.artists.R;
import com.iam.artists.entities.Artist;
import com.iam.artists.utils.Decorator;

import org.json.JSONException;
import org.json.JSONObject;

public class ArtistPageActivity extends AppCompatActivity {

    /**
     * artistJsonString get artist json for recreate it when orientation changed
     */
    private String artistJsonString;

    /**
     * data for customizing views
     */
    private Artist artist;
//    private DisplayImageOptions imageOptions; // UniversalImageLoader lib for increase app performance

    /**
     * Views
     */

    ImageView image;
    TextView genres;
    TextView tracks;
    TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_page_activity);
//        imageOptions = Decorator.imageOptions();

        Decorator.setStatusBarColor(this);

        /**
         * check devise rotated or activity opened first time
         * first case - rotated, creating Artist entity from saved json
         */
        if (savedInstanceState != null) {
            artistJsonString = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.selected_artist_json), "\\{\\}");
            try {
                artist = new Artist(new JSONObject(artistJsonString));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        /**
         * second case - first time loaded, getted parcelable entity from intent
         */
        } else {
            artist = getIntent().getParcelableExtra(getString(R.string.selected_artist));
        }
        Decorator.customizeActionBar(this, artist.name());
        customizeViews();


    }

    private void customizeViews() {
        // Customize logo - binding with xml, setting height based on screen width and aspect ratio, and loading image into it
        image = (ImageView) findViewById(R.id.image);
        Decorator.setHeight(image, Decorator.heightByWidthUsesAspectRatio(Decorator.screenWidth(this), 16, 9));
        Glide.with(this).load(artist.cover().small()).into(image);
//        ImageLoader.getInstance().displayImage(artist.cover().big(), image, imageOptions);

        // customize text views
        genres = (TextView) findViewById(R.id.genres);
        genres.setText(artist.genres());

        tracks = (TextView) findViewById(R.id.tracks);
        tracks.setText(artist.tracks());

        description = (TextView) findViewById(R.id.description);
        description.setText(artist.description());
    }
}
