package com.ponyvillelive.app.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.app.BuildConfig;
import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.DataCache;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.DataCacheChangedEvent;
import com.ponyvillelive.app.model.NowPlayingMeta;
import com.ponyvillelive.app.model.Station;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * The backing Adapter class for a {@link com.ponyvillelive.app.ui.StationFragment} which stores
 * and manages the stations the fragment shows
 */
public class StationAdapter extends BaseAdapter {

    private static final String TAG = "PVL StationAdapter";

    private Station[] stations;
    private Context context;
    private Map<String, NowPlayingMeta> nowPlayingMetaMap;
    private String mode;
    private SharedPreferences prefs;

    public StationAdapter(Context context, String mode) {
        Timber.tag(TAG);

        BusProvider.getBus().register(this);

        this.context = context;
        this.mode = mode;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.stations = DataCache.retrieveStationList(mode);
        notifyDataSetChanged();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(mode.equals(Station.STATION_TYPE_AUDIO)
                && prefs.getBoolean(SettingsActivity.PREF_KEY_HIPOWER_MODE, false)) {
            //TODO: Get now playing data
        }
    }

    @Override
    public int getCount() { return stations == null? 0 : stations.length; }

    @Override
    public Station getItem(int position) { return stations[position]; }

    @Override
    public long getItemId(int position) { return stations[position].id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_station_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Station station = getItem(position);
        holder.titleText.setText(station.name);
        holder.genreText.setText(station.genre);

        if(mode.equals(Station.STATION_TYPE_AUDIO)
                && nowPlayingMetaMap != null
                && prefs.getBoolean(SettingsActivity.PREF_KEY_HIPOWER_MODE, false)) {
            if(nowPlayingMetaMap.get(station.shortcode).currentSong.title != null) {
                holder.trackText.setText(nowPlayingMetaMap.get(station.shortcode).currentSong.title);
                holder.trackText.setVisibility(View.VISIBLE);
            }
            if(nowPlayingMetaMap.get(station.shortcode).currentSong.artist != null) {
                holder.artistText.setText(nowPlayingMetaMap.get(station.shortcode).currentSong.artist);
                holder.artistText.setVisibility(View.VISIBLE);
            }
        }

        Picasso.with(context).setDebugging(BuildConfig.DEBUG);
        Picasso.with(context)
                .load(station.imageUrl)
                .into(holder.icon);

        return convertView;
    }

    @Subscribe
    public void receiveDataChangeEvent(DataCacheChangedEvent event) {
        if(!event.eventType.equals(DataCacheChangedEvent.EVT_KEY_STATION_LIST_CHANGED)) return;

        stations = DataCache.retrieveStationList(mode);
        notifyDataSetChanged();
    }

    public class ViewHolder {

        @InjectView(R.id.icon_station_logo)
        ImageView icon;
        @InjectView(R.id.text_station_name)
        TextView titleText;
        @InjectView(R.id.text_station_description)
        TextView genreText;
        @InjectView(R.id.text_station_track_name)
        TextView trackText;
        @InjectView(R.id.text_station_artist_name)
        TextView artistText;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
