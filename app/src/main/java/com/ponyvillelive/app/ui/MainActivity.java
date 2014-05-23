package com.ponyvillelive.app.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.ponyvillelive.app.BusProvider;
import com.ponyvillelive.app.R;
import com.ponyvillelive.app.event.PlaybackStartedEvent;
import com.ponyvillelive.app.media.PlayerService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    @InjectView(R.id.view_slideup_panel)
    SlidingUpPanelLayout slidingPanel;

    /**
     * Fragment managing the behaviours, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Fragment managing the bahaviours, interactions and presentations of the bottom drawer.
     */
    private BottomDrawerFragment bottomDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;

    /**
     * Use this to keep track of the Fragment currently framed, that way we don't end up replacing
     * fragments we don't need to.
     */
    private int currentNavPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        slidingPanel.setDragView(findViewById(R.id.layout_bottom_drawer_metadata));

        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        bottomDrawerFragment = (BottomDrawerFragment)
                getFragmentManager().findFragmentById(R.id.bottom_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, PlayerService.class);
        startService(intent);
        BusProvider.getBus().register(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        BusProvider.getBus().unregister(this);

        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments, noop-ing if we're not transitioning
        if(position == currentNavPosition) return;
        currentNavPosition = position;
        Fragment fragment = NavigationEnum.values()[position].getFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();
    }

    public void onSectionAttached(int position) {
        String[] navLabels = getResources().getStringArray(R.array.navItems);
        int ordinal = position >= navLabels.length? 0 : position;
        title = navLabels[ordinal];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
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

    @Subscribe
    public void handlePlaybackStarted(PlaybackStartedEvent event) {

    }
}
