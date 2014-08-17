package com.ponyvillelive.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;

/**
 * Created by berwyn on 17/08/14.
 */
public class StationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private      Station[] stations;

    public StationAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.stations = new Station[0];
    }

    public void setStations(Station[] stations) {
        this.stations = stations;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return stations.length;
    }

    @Override
    public Station getItem(int position) {
        return stations[position];
    }

    @Override
    public long getItemId(int position) {
        return stations[position].id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView != null) {
            return convertView;
        }
        return inflater.inflate(R.layout.fragment_station_list, null);
    }
}
