package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ponyvillelive.app.BuildConfig;
import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.PlayRequestedEvent;
import com.ponyvillelive.app.event.PlaybackStoppedEvent;
import com.ponyvillelive.app.event.StopRequestedEvent;
import com.ponyvillelive.app.model.Station;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A basic fragment to handle the "now playing" drawer present in the main activity.
 */
public class BottomDrawerFragment extends Fragment {

    @InjectView(R.id.icon_station_logo)
    ImageView stationIcon;
    @InjectView(R.id.text_station_name)
    TextView stationName;
    @InjectView(R.id.text_station_description)
    TextView stationTag;
    @InjectView(R.id.btn_drawer_cancel)
    ImageButton cancelButton;

    private Station station;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BottomDrawerFragment.
     */
    public static BottomDrawerFragment newInstance() {
        BottomDrawerFragment fragment = new BottomDrawerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BottomDrawerFragment() {
        // Required empty public constructor
        BusProvider.getBus().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_drawer, container, false);

        ButterKnife.inject(this, view);
        cancelButton.setOnClickListener((clickedView) -> BusProvider.getBus().post(new StopRequestedEvent()));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Subscribe
    public void handleStationRequest(PlayRequestedEvent event) {
        if(event.station == null) return;
        this.station = event.station;

        View view = this.getView();
        if(view != null) {
            stationName.setText(this.station.name);
            stationTag.setText(this.station.genre);
            Picasso.with(getActivity()).setDebugging(BuildConfig.DEBUG);
            Picasso.with(getActivity())
                    .load(this.station.imageUrl)
                    .into(stationIcon);
            view.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void handlePlaybackStopped(PlaybackStoppedEvent event) {
        View view = this.getView();
        if(view != null) {
            view.setVisibility(View.GONE);
        }
    }

}
