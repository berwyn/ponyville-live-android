package com.ponyvillelive.app.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ponyvillelive.app.PvlApp;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;
import com.ponyvillelive.app.net.API;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.view_station_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = stations[position];

        holder.name.setText(station.name);
        holder.genre.setText(station.genre);
        picasso
                .load(station.imageUrl)
                .placeholder(R.drawable.ic_launcher)
                .into(holder);
    }

    @Override
    public int getItemCount() {
        return stations.length;
    }

    public Station getItem(int position) {
        return stations[position];
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements Target {

        @InjectView(R.id.station_icon)
        ImageView   icon;
        @InjectView(R.id.station_name)
        TextView    name;
        @InjectView(R.id.station_genre)
        TextView    genre;
        @InjectView(R.id.station_menu)
        ImageButton menuButton;

        CardView baseView;
        Palette  colourPalette;

        public ViewHolder(View itemView) {
            super(itemView);
            baseView = (CardView) itemView;

            ButterKnife.inject(this, baseView);
        }

        @OnClick(R.id.station_menu)
        public void menuClicked(View v) {
            PopupMenu popupMenu = new PopupMenu(StationAdapter.this.inflater.getContext(), v);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.view_station, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_change_stream) {
                    // TODO: Switch streams here
                    Toast.makeText(inflater.getContext(), "Change streams pressed", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }

        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            if (colourPalette == null) {
                Palette.generateAsync(bitmap, 8, (palette) -> baseView.post(() -> {
                    icon.setImageBitmap(bitmap);
                    colourPalette = palette;
                    colourise();
                }));
            } else {
                baseView.post(() -> {
                    icon.setImageBitmap(bitmap);
                    colourise();
                });
            }
        }

        public void colourise() {
           name.setTextColor(colourPalette.getVibrantColor(Color.BLACK));
           genre.setTextColor(colourPalette.getMutedColor(Color.DKGRAY));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            // noop
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            icon.post(() -> icon.setImageDrawable(placeHolderDrawable));
        }
    }
}
