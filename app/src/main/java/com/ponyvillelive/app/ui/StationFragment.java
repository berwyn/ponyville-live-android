package com.ponyvillelive.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.ponyvillelive.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class StationFragment extends Fragment implements AbsListView.OnItemClickListener {

    private static final String ARG_TYPE = "type";
    private String stationType;

    @InjectView(android.R.id.list)
    AbsListView listView;
    @InjectView(android.R.id.empty)
    ImageView   emptyView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private StationAdapter adapter;

    public static StationFragment newInstance(String stationType) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, stationType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
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

        // Set the adapter
        listView.setAdapter(adapter);
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Handle list/grid click
    }

}
