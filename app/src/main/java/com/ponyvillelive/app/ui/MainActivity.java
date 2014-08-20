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
import timber.log.Timber;

public class MainActivity extends FragmentActivity implements StationFragment.StationFragmentListener {

    @Inject
    AppContainer appContainer;

    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.view_slideup_panel)
    SlidingUpPanelLayout slideUpPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PvlApp app = PvlApp.get(this);
        app.inject(this);

        long start = System.nanoTime();

        ViewGroup container = appContainer.get(this, app);
        View view = getLayoutInflater().inflate(R.layout.activity_main, container);

        ButterKnife.inject(this, view);

        long diff = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        Timber.d("Took %sms to inject activity", diff);

        pager.setAdapter(new FragmentTabAdapter(getSupportFragmentManager()));
        tabStrip.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        // TODO: Station was selected
        // Inject it into the bottom drawer fragment
        // Fire the intent for the music service to start playing
    }

    public class FragmentTabAdapter extends FragmentPagerAdapter {
        private final String[] titles;
        private final Fragment[] fragments = new Fragment[]{
                StationFragment.newInstance(Station.STATION_TYPE_AUDIO),
                StationFragment.newInstance(Station.STATION_TYPE_VIDEO),
                new Fragment(),
                new Fragment()
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
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }
    }
}
