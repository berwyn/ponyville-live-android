package com.ponyvillelive.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.app.PvlApp;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.net.API;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * An implementation of {@link android.widget.BaseAdapter}
 * for {@link com.ponyvillelive.app.model.Station}s
 */
public class StationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Station[]      stations;

    @Inject
    Picasso picasso;
    @Inject
    API api;

    public StationAdapter(LayoutInflater inflater, String type) {
        this.inflater = inflater;
        this.stations = new Station[0];
        PvlApp.get(inflater.getContext()).inject(this);
        api
            .getStationList(type)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((stationResponse) -> {
                this.stations = stationResponse.result;
                notifyDataSetChanged();
            });
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
        ViewHolder vh;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.view_station_list_item, null);
            vh = new ViewHolder();
            ButterKnife.inject(vh, convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Station station = getItem(position);
        vh.name.setText(station.name);
        vh.genre.setText(station.genre);
        picasso
                .load("https:" + station.imageUrl)
                .placeholder(R.drawable.ic_launcher)
                .into(vh.icon);

        return convertView;
    }

    public class ViewHolder {
        @InjectView(R.id.station_icon)
        ImageView icon;
        @InjectView(R.id.station_name)
        TextView name;
        @InjectView(R.id.station_genre)
        TextView genre;
    }
}
