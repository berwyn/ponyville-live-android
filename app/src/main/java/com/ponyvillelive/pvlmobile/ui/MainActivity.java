package com.ponyvillelive.pvlmobile.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ponyvillelive.pvlmobile.PvlApp;
import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.model.Station;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        StationFragment.StationFragmentListener,
        BottomDrawerFragment.DrawerListener {

    private static final String BUNDLE_KEY_STATION = "bundle_key_station";

    @Inject
    AppContainer appContainer;

    private boolean tester = false;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PvlApp app = PvlApp.get(this);
        app.inject(this);

        long start = System.nanoTime();

        super.onCreate(savedInstanceState);
        ViewGroup container = appContainer.get(this, app);
        View view = getLayoutInflater().inflate(R.layout.activity_main, container);
        initializeToolbar();

        ButterKnife.inject(this, view);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.d("Took %sms to inject activity", diff);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, StationFragment.newInstance(Station.STATION_TYPE_AUDIO))
                .commit();

        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_STATION)) {
            station = savedInstanceState.getParcelable(BUNDLE_KEY_STATION);
            handleStationSelected(station);
        }
    }

    @Override
    public boolean handleStationSelected(Station station) {
        this.station = station;

        return true;
        // TODO: Fire intent for media server
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Show the menu
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (station != null) {
            outState.putParcelable(BUNDLE_KEY_STATION, station);
        }
    }

    @Override
    protected void onMediaControllerConnected() {
        // TODO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_tester:
                tester = !tester;
                item.setChecked(tester);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleStationCleared() {
        this.station = null;
    }

    public class FragmentTabAdapter extends FragmentPagerAdapter {
        private final String[] titles;
        // Removing Fragments until the actual fragment is ready
        private final Fragment[] fragments = new Fragment[]{
                StationFragment.newInstance(Station.STATION_TYPE_AUDIO),
                StationFragment.newInstance(Station.STATION_TYPE_VIDEO),
        };

        public FragmentTabAdapter(FragmentManager manager) {
            super(manager);
            titles = getResources().getStringArray(R.array.navItems);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }
}
