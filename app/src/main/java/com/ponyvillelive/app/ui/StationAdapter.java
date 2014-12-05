package com.ponyvillelive.app.ui;

import android.content.res.Resources;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ponyvillelive.app.PvlApp;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.net.API;
import com.ponyvillelive.app.util.PaletteTransformation;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * An implementation of {@link android.widget.BaseAdapter}
 * for {@link com.ponyvillelive.app.model.Station}s
 */
public class StationAdapter extends BaseAdapter {

    @Inject
    Picasso picasso;
    @Inject
    API     api;

    private LayoutInflater inflater;
    private Station[]      stations;
    private Subscription   apiSub;
    private Resources      res;

    public StationAdapter(LayoutInflater inflater, String type) {
        this.inflater = inflater;
        this.stations = new Station[0];
        this.res = inflater.getContext().getResources();
        PvlApp.get(inflater.getContext()).inject(this);
        apiSub = api
                .getStationList(type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((stationResponse) -> {
                    this.stations = stationResponse.result;
                    notifyDataSetChanged();
                    apiSub.unsubscribe();
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
            vh.baseView = convertView;
            ButterKnife.inject(vh, convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Station station = getItem(position);
        vh.name.setText(station.name);
        vh.genre.setText(station.genre);
        picasso
                .load(station.imageUrl)
                .transform(new PaletteTransformation(station.shortcode, vh))
                .placeholder(R.drawable.ic_launcher)
                .into(vh.icon);

        return convertView;
    }

    public class ViewHolder implements PaletteTransformation.Callback {
        @InjectView(R.id.station_icon)
        ImageView icon;
        @InjectView(R.id.station_name)
        TextView  name;
        @InjectView(R.id.station_genre)
        TextView  genre;
        View    baseView;
        Palette colourPalette;

        @OnClick(R.id.station_menu)
        public void menuClicked(View v) {
            PopupMenu popupMenu = new PopupMenu(StationAdapter.this.inflater.getContext(), v);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.view_station, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getItemId() == R.id.action_change_stream) {
                    // TODO: Switch streams here
                    Toast.makeText(inflater.getContext(), "Change streams pressed", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }

        @Override
        public Palette getPalette() {
            return this.colourPalette;
        }

        @Override
        public void setPalette(Palette palette) {
            this.colourPalette = palette;
        }

        @Override
        public void colorise() {
            Palette.Swatch swatch = colourPalette.getLightMutedSwatch();
            baseView.post(() -> {
                if(swatch != null) {
                    baseView.setBackgroundColor(swatch.getRgb());
                    name.setTextColor(swatch.getTitleTextColor());
                    genre.setTextColor(swatch.getBodyTextColor());
                }
            });
        }

    }
}
