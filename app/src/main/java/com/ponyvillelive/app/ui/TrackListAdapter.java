package com.ponyvillelive.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Song;
import com.ponyvillelive.app.model.SongWrapper;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by berwyn on 21/08/14.
 */
public class TrackListAdapter extends BaseAdapter {

    private List<SongWrapper> songs;

    public TrackListAdapter() {
        this.songs = Collections.emptyList();
    }

    public void setSongs(List<SongWrapper> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public void addSongs(List<SongWrapper> songs) {
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Song getItem(int position) {
        return songs.get(position).song;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.view_track_list_item, parent, false);
            vh = new ViewHolder();
            ButterKnife.inject(vh, convertView);
            convertView.setTag(vh);
        } else {
            vh = ((ViewHolder) convertView.getTag());
        }

        Song song = getItem(position);
        vh.title.setText(song.title == null? "[NO DATA]" : song.title);
        vh.artist.setText(song.artist == null? "[NO DATA]": song.artist);

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.track_title)
        TextView title;
        @InjectView(R.id.track_artist)
        TextView artist;
    }
}
