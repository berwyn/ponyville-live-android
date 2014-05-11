package com.ponyvillelive.app.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.app.BuildConfig;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.model.StationResponse;
import com.ponyvillelive.app.net.APIProvider;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tyr on 10/05/2014.
 */
public class StationAdapter extends BaseAdapter {

    private static final String TAG = "PVL StationAdapter";

    private Station[] stations;
    private Context context;

    public StationAdapter(Context context, String mode) {
        this.context = context;
        stations = new Station[0];

        APIProvider.getInstance().getStationList(mode, new Callback<StationResponse>() {

            @Override
            public void success(StationResponse stationResponse, Response response) {
                Log.d(TAG, "Response came back!");
                StationAdapter.this.stations = stationResponse.result;
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, retrofitError.getMessage());
            }
        });
    }

    @Override
    public int getCount() { return stations == null? 0 : stations.length; }

    @Override
    public Station getItem(int position) { return stations[position]; }

    @Override
    public long getItemId(int position) { return stations[position].id; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView icon;
        TextView titleText;
        TextView genreText;
        TextView trackText;
        TextView artistText;

        if(convertView != null) {
            icon = (ImageView) convertView.getTag(R.id.icon_station_logo);
            titleText = (TextView) convertView.getTag(R.id.text_station_name);
            genreText = (TextView) convertView.getTag(R.id.text_station_description);
            trackText = (TextView) convertView.getTag(R.id.text_station_track_name);
            artistText = (TextView) convertView.getTag(R.id.text_station_artist_name);
        } else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_station_list_item, parent, false);

            assert convertView != null;
            icon = (ImageView) convertView.findViewById(R.id.icon_station_logo);
            titleText = (TextView) convertView.findViewById(R.id.text_station_name);
            genreText = (TextView) convertView.findViewById(R.id.text_station_description);
            trackText = (TextView) convertView.findViewById(R.id.text_station_track_name);
            artistText = (TextView) convertView.findViewById(R.id.text_station_artist_name);

            convertView.setTag(R.id.icon_station_logo, icon);
            convertView.setTag(R.id.text_station_name, titleText);
            convertView.setTag(R.id.text_station_description, genreText);
            convertView.setTag(R.id.text_station_track_name, trackText);
            convertView.setTag(R.id.text_station_artist_name, artistText);
        }

        titleText.setText(getItem(position).name);
        genreText.setText(getItem(position).genre);
        trackText.setText("[DEBUG]");
        artistText.setText("[DEBUG]");
        Picasso.with(context).setDebugging(BuildConfig.DEBUG);
        Picasso.with(context)
                .load(getItem(position).imageUrl)
                .placeholder(R.drawable.square_logo)
                .into(icon);

        return convertView;
    }
}
