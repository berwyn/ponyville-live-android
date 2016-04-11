package com.ponyvillelive.pvlmobile.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.model.Station;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A fragment representing a list of Items.
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 */
public class StationFragment extends Fragment {

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
     * of {@link com.ponyvillelive.pvlmobile.model.Station}s
     *
     * @param stationType {@link com.ponyvillelive.pvlmobile.model.Station#STATION_TYPE_AUDIO}
     *                    or {@link com.ponyvillelive.pvlmobile.model.Station#STATION_TYPE_VIDEO}
     * @return A new {@link com.ponyvillelive.pvlmobile.ui.StationFragment} instance
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
        listView.setHasFixedSize(true);

        listView.addOnItemTouchListener(new StationListTouchListener());

//        final ItemClickSupport itemClickSupport = ItemClickSupport.addTo(listView);
//        itemClickSupport.setOnItemClickListener((parent, child, position, id) -> {
//            listener.handleStationSelected(adapter.getItem(position));
//        });

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

    /**
     * The interaction contract that all {@link android.app.Activity}s embedding a
     * {@link com.ponyvillelive.pvlmobile.ui.StationFragment} must implement
     */
    public interface StationFragmentListener {
        /**
         * Handle a station being selected inside of the {@link com.ponyvillelive.pvlmobile.ui.StationFragment}
         *
         * @param station The {@link com.ponyvillelive.pvlmobile.model.Station} that was selected
         */
        public boolean handleStationSelected(Station station);
    }

    /*
     *============================================================================
     * The following is lovingly borrowed from Lucas Roca's excellent TwoWay-View:
     * https://github.com/lucasr/twoway-view
     *============================================================================
     */

    private final class StationListTouchListener implements RecyclerView.OnItemTouchListener {

        private final GestureDetectorCompat gestureDetector;

        StationListTouchListener() {
            gestureDetector = new GestureDetectorCompat(listView.getContext(), new StationListGestureListener());
        }

        private boolean isAttachedToWindow(RecyclerView hostView) {
            if (Build.VERSION.SDK_INT >= 19) {
                return hostView.isAttachedToWindow();
            } else {
                return (hostView.getHandler() != null);
            }
        }

        private boolean hasAdapter(RecyclerView hostView) {
            return (hostView.getAdapter() != null);
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            if(isAttachedToWindow(rv) && hasAdapter(rv)) {
                gestureDetector.onTouchEvent(e);
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            // This is a noop, since our gesture detector actually handles
            // the event. We just use this class to snoop :)
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            // Also a noop
        }
    }

    private final class StationListGestureListener extends GestureDetector.SimpleOnGestureListener {
        private View touchTarget;
        private boolean btnTouched;

        public void handleTap(MotionEvent e) {
            if(touchTarget != null) {
                onSingleTapUp(e);
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            final int x = (int) e.getX();
            final int y = (int) e.getY();

            touchTarget = listView.findChildViewUnder(x, y);

            if(touchTarget == null) {
                btnTouched = false;
            } else {
                View btn = touchTarget.findViewById(R.id.station_menu);
                Rect hitRect = new Rect();
                btn.getHitRect(hitRect);
                btnTouched = hitRect.contains(x, y);
            }

            return touchTarget != null && !btnTouched;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if(touchTarget != null) {
                touchTarget.setPressed(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            boolean handled = false;

            if(btnTouched) {
                return false;
            }

            if(touchTarget != null) {
                touchTarget.setPressed(false);
                int pos = listView.getChildPosition(touchTarget);
                handled = listener.handleStationSelected(adapter.getItem(pos));
                touchTarget = null;
            }

            return handled;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(touchTarget != null) {
                touchTarget.setPressed(false);
                touchTarget = null;
                return true;
            }
            return false;
        }
    }
}
