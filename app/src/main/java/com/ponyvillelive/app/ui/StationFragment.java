package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 */
public class StationFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_TYPE = "type";

    @InjectView(android.R.id.list)
    RecyclerView listView;
//    @InjectView(android.R.id.empty)
//    ImageView    emptyView;

    private String                     stationType;
    private StationFragmentListener    listener;
    private StationAdapter             adapter;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
    }

    /**
     * A basic {@link android.support.v4.app.Fragment} containing a {@link android.widget.AbsListView}
     * of {@link com.ponyvillelive.app.model.Station}s
     *
     * @param stationType {@link com.ponyvillelive.app.model.Station#STATION_TYPE_AUDIO}
     *                    or {@link com.ponyvillelive.app.model.Station#STATION_TYPE_VIDEO}
     * @return A new {@link com.ponyvillelive.app.ui.StationFragment} instance
     */
    public static StationFragment newInstance(String stationType) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, stationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.stationType = getArguments().getString(ARG_TYPE, "audio");
        }
        adapter = new StationAdapter(getLayoutInflater(savedInstanceState), this.stationType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        ButterKnife.inject(this, view);

        layoutManager = new LinearLayoutManager(getActivity());

        // Set the adapter
        listView.setAdapter(adapter);
        listView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof StationFragmentListener)) {
            throw new RuntimeException("Activities must implement StationFragmentListener");
        } else {
            listener = (StationFragmentListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            listener.handleStationSelected(adapter.getItem(position));
        }
    }

    /**
     * The interaction contract that all {@link android.app.Activity}s embedding a
     * {@link com.ponyvillelive.app.ui.StationFragment} must implement
     */
    public interface StationFragmentListener {
        /**
         * Handle a station being selected inside of the {@link com.ponyvillelive.app.ui.StationFragment}
         *
         * @param station The {@link com.ponyvillelive.app.model.Station} that was selected
         */
        public void handleStationSelected(Station station);
    }
}
