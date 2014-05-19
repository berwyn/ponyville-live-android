package com.ponyvillelive.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.PlayRequestedEvent;
import com.ponyvillelive.app.model.Station;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class StationFragment extends Fragment implements AbsListView.OnItemClickListener {

    public static final String BUNDLE_KEY_MODE = "mode";

    private String mode;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView listView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter adapter;

    /**
     * Creates a new instance of {@link com.ponyvillelive.app.ui.StationFragment} in a given mode
     * @param mode one of {@link Station#STATION_TYPE_AUDIO} or {@link Station#STATION_TYPE_VIDEO}
     * @return
     */
    public static StationFragment newInstance(String mode) {
        StationFragment fragment = new StationFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_MODE, mode);
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

        mode = getArguments().getString(BUNDLE_KEY_MODE);
        adapter = new StationAdapter(getActivity(), mode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);

        // Set the adapter
        listView = (AbsListView) view.findViewById(android.R.id.list);
        listView.setEmptyView(listView.getRootView().findViewById(android.R.id.empty));
        listView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);

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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mode.equals(Station.STATION_TYPE_AUDIO)) {
            BusProvider.getBus().post(new PlayRequestedEvent((Station) adapter.getItem(position)));
        } else {
            // TODO: We need to do vidoe playback at some point
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = listView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

}
