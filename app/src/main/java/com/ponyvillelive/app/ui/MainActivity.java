package com.ponyvillelive.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ponyvillelive.app.PvlApp;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.model.Station;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icepick;
import timber.log.Timber;

public class MainActivity extends FragmentActivity implements
        StationFragment.StationFragmentListener,
        BottomDrawerFragment.DrawerListener {

    private static final String BUNDLE_KEY_STATION = "bundle_key_station";

    @Inject
    AppContainer appContainer;

    @InjectView(R.id.pager)
    ViewPager            pager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.view_slideup_panel)
    SlidingUpPanelLayout slideUpPanel;
    @InjectView(R.id.drawer_metadata)
    View drawerHandle;

    private Station station;
    private BottomDrawerFragment bottomDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

        PvlApp app = PvlApp.get(this);
        app.inject(this);

        long start = System.nanoTime();

        ViewGroup container = appContainer.get(this, app);
        View view = getLayoutInflater().inflate(R.layout.activity_main, container);

        ButterKnife.inject(this, view);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.d("Took %sms to inject activity", diff);

        bottomDrawer = (BottomDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_drawer);
        pager.setAdapter(new FragmentTabAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(pager);
        slideUpPanel.setDragView(drawerHandle);
        slideUpPanel.hidePanel();
        slideUpPanel.setPanelSlideListener(new ActionbarHideSlidePanelListener(this));

        if(savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_KEY_STATION)) {
            station = savedInstanceState.getParcelable(BUNDLE_KEY_STATION);
            handleStationSelected(station);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
        if(station != null) {
            outState.putParcelable(BUNDLE_KEY_STATION, station);
        }
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
    public void handleStationSelected(Station station) {
        this.station = station;
        bottomDrawer.showStationInfo(station);
        if(slideUpPanel.isPanelHidden()) {
            slideUpPanel.showPanel();
        }
        // TODO: Fire intent for media server
    }

    @Override
    public void handleStationCleared() {
        this.station = null;
        if(!slideUpPanel.isPanelHidden()) {
            slideUpPanel.hidePanel();
        }
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
