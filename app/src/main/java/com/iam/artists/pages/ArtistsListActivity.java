package com.iam.artists.pages;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iam.artists.R;
import com.iam.artists.entities.Artist;
import com.iam.artists.utils.Decorator;
import com.iam.artists.utils.Web;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ArtistsListActivity extends AppCompatActivity {

    /**
     * bpCount - back press count, prevent app from accidentally finishig when "back" pressed
     */

    int bpCount = 0;

    /**
     * data for recycler view (main app data)
     */

    private ArrayList<Artist> artists = new ArrayList<>();


    /**
     * views
     */

    private ProgressBar progressBar;

    private RecyclerView artistsRecyclerView;
    private RecyclerView.LayoutManager manager;
    private ArtistsRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artists_activity);

        // setting bpcount in 0 on each activity onCreate
        bpCount = 0;

        artistsRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        /**
         * on start get saved data from sp, "" if it is first launch
         */
        String data = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.server_data), "");

        /**
         * check device rotated or app launched
         * first case
         */
        if (savedInstanceState == null) {
            /**
             * at firs time customize activity design
             */
            Decorator.setStatusBarColor(this);
            Decorator.customizeActionBar(this, "Исполнители");

            /**
             * check if first launch - load data from web, second and far - load data from sp
             */
            if (!data.equals("")) {
                fillArtistArray(data);
                setRecyclerData();
            } else {
                new GetTask().execute();
            }
        /**
         * second case - rotated. Don't need reload data from web, get it from sp
         */
        } else {
            fillArtistArray(data);
            setRecyclerData();
        }
    }

    /**
     * function forms artists list from data
     * @param data - string getted from web or sp contains artists json
     */
    private void fillArtistArray(String data) {
        if (artists.isEmpty()) {
            try {
                JSONArray jArtists = new JSONArray(data);
                int l = jArtists.length();
                for (int i = 0; i < l; i++) {
                    artists.add(new Artist(jArtists.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * recycler launch
     */
    private void setRecyclerData() {
        if (manager == null) {
            manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            artistsRecyclerView.setLayoutManager(manager);
        }
        if (adapter == null) {
            adapter = new ArtistsRecyclerAdapter(artists, this);
            artistsRecyclerView.setAdapter(adapter);
        }
    }


    /**
     * adapter for artists recycler. constructor get data list, and context for getting layout inflater
     */
    class ArtistsRecyclerAdapter extends RecyclerView.Adapter<ArtistsRecyclerAdapter.ArtistVH> {

        private ArrayList<Artist> list;
        private int count;
        private LayoutInflater inflater;

        public ArtistsRecyclerAdapter(ArrayList<Artist> list, Context context) {
            this.list = list;
            this.count = list.size();
            this.inflater = ((AppCompatActivity)context).getLayoutInflater();
        }

        @Override
        public ArtistVH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ArtistVH(inflater.inflate(R.layout.artist_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ArtistVH holder, int position) {
            holder.onBind(position);
        }

        @Override
        public int getItemCount() {
            return count;
        }

        class ArtistVH extends RecyclerView.ViewHolder {

            View view;
            ImageView image;
            TextView name;
            TextView genres;
            TextView tracks;
            ProgressBar progressBar;

            public ArtistVH(View itemView) {
                super(itemView);
                this.view = itemView;
                image = (ImageView)itemView.findViewById(R.id.image);
                name = (TextView) itemView.findViewById(R.id.name);
                genres = (TextView) itemView.findViewById(R.id.genres);
                tracks = (TextView) itemView.findViewById(R.id.trackscount);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            }

            public void onBind(final int position) {
                final Artist artist = list.get(position);

                Glide.with(ArtistsListActivity.this)
                        .load(artist.cover().small())
                        .error(new ColorDrawable(0xff000000 + artist.name().hashCode() % 0x1000000))
                        .into(image);
                name.setText(artist.name());
                genres.setText(artist.genres());
                tracks.setText(artist.tracks());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager
                                .getDefaultSharedPreferences(ArtistsListActivity.this)
                                .edit()
                                .putString(getString(R.string.selected_artist_json), artist.json())
                                .apply();
                        Intent intent = new Intent(ArtistsListActivity.this, ArtistPageActivity.class);
                        intent.putExtra(getString(R.string.selected_artist), artist);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     * Async task for get json from web
     */
    class GetTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {
            String sArtists = Web.httpGet(Web.domain);
            return sArtists;
        }

        @Override
        protected void onPostExecute(String s) {

            fillArtistArray(s);
            PreferenceManager
                    .getDefaultSharedPreferences(ArtistsListActivity.this)
                    .edit()
                    .putString(getString(R.string.server_data), s)
                    .apply();
            setRecyclerData();
            hideProgressBar();
        }
    }

    private void showProgressBar () {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar () {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {

        bpCount++;
        switch (bpCount) {
            case 1:
                Toast.makeText(
                        getApplicationContext(),
                        "Для выхода нажмите \"Назад\" ещё раз",
                        Toast.LENGTH_SHORT
                ).show();
                break;
            case 2:
                super.onBackPressed();
                break;
        }
    }

}
