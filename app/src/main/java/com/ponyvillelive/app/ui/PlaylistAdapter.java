package com.ponyvillelive.app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.NowPlayingStationResponse;
import com.ponyvillelive.app.model.Song;
import com.ponyvillelive.app.net.APIProvider;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by tyr on 22/05/2014.
 */
public class PlaylistAdapter extends BaseAdapter {

    private Context context;
    private List<Song> songs;

    public PlaylistAdapter(Context context) {
        this.context = context;
        Timber.tag("Playlist Adapter");
    }

    public void getData(int stationId) {
        APIProvider.getInstance().getNowPlayingForStation(stationId, new Callback<NowPlayingStationResponse>() {
            @Override
            public void success(NowPlayingStationResponse nowPlayingResponse, Response response) {
                songs = nowPlayingResponse.result.songHistory;
                songs.add(0, nowPlayingResponse.result.currentSong);
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.d(error.getMessage());
            }
        });
    }

    @Override
    public int getCount() {
        return songs == null? 0 : songs.size();
    }

    @Override
    public Song getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return songs.get(position).id.hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView trackText;
        TextView artistText;

        if(convertView == null) {
            convertView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.view_playlist_list_item, parent, false);
            trackText = (TextView) convertView.findViewById(R.id.playlist_title);
            artistText = ((TextView) convertView.findViewById(R.id.playlist_artist));

            convertView.setTag(R.id.playlist_title, trackText);
            convertView.setTag(R.id.playlist_artist, artistText);
        } else {
            trackText = ((TextView) convertView.getTag(R.id.playlist_title));
            artistText = ((TextView) convertView.getTag(R.id.playlist_artist));
        }

        Song track = getItem(position);
        trackText.setText(track.title);
        artistText.setText(track.artist);

        return convertView;
    }
}
