package com.ponyvillelive.app.ui;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.PlaybackStartedEvent;
import com.ponyvillelive.app.media.PlayerService;
import com.ponyvillelive.app.model.Station;
import com.squareup.otto.Subscribe;


public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        StationFragment.OnFragmentInteractionListener,
        BottomDrawerFragment.OnFragmentInteractionListener,
        ServiceConnection {

    /**
     * Fragment managing the behaviours, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Fragment managing the bahaviours, interactions and presentations of the bottom drawer.
     */
    private BottomDrawerFragment mBottomDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Use this to keep track of the Fragment currently framed, that way we don't end up replacing
     * fragments we don't need to.
     */
    private int mNavPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mBottomDrawerFragment = (BottomDrawerFragment)
                getFragmentManager().findFragmentById(R.id.bottom_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);

        super.onResume();
    }

    @Override
    protected void onPause() {
        unbindService(this);

        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments, noop-ing if we're not transitioning
        if(position == mNavPosition) return;
        Fragment fragment;
        switch(position) {
            case 0:
            default:
                fragment = StationFragment.newInstance(Station.STATION_TYPE_AUDIO);
                break;
            case 1:
                fragment = StationFragment.newInstance(Station.STATION_TYPE_VIDEO);
                break;
            case 2:
                fragment = ShowFragment.newInstance();
                break;
            case 3:
                fragment = RequestFragment.newInstance();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_radio);
                break;
            case 2:
                mTitle = getString(R.string.title_video);
                break;
            case 3:
                mTitle = getString(R.string.title_shows);
                break;
            case 4:
                mTitle = getString(R.string.title_requests);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ((PlayerService.PlayerServiceBinder) service).setEventBus(BusProvider.getBus());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // noop
    }

    /**
     * Implements the callbacks for {@link com.ponyvillelive.app.ui.StationFragment}
     * @param station The station that's been selected
     */
    @Override
    public void onFragmentInteraction(Station station) {
        // TODO: noop, maybe remove?
    }

    @Override
    public void onFragmentInteraction(int event) {
        // TODO: noop, maybe remove?
    }

    @Subscribe
    public void handlePlaybackStarted(PlaybackStartedEvent event) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
