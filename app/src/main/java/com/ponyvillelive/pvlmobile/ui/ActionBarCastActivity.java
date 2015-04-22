package com.ponyvillelive.pvlmobile.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ponyvillelive.pvlmobile.PvlApp;
import com.ponyvillelive.pvlmobile.R;
import com.ponyvillelive.pvlmobile.util.ResourceHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import timber.log.Timber;

/**
 * Created by berwyn on 14/03/15.
 */
public abstract class ActionBarCastActivity extends AppCompatActivity {

    public static final int DELAY_MILLIS = 1000;

    private static final String TAG = ActionBarCastActivity.class.getSimpleName();

    // TODO: Add the Cast manager here

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Optional
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Optional
    @InjectView(R.id.drawer_list)
    ListView drawerList;

    private MenuItem              mediaRouteMenuItem;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerMenuContents    drawerMenuContents;
    private MediaControllerCompat mediaController;

    private boolean toolbarInitialised;
    private int itemToOpenWhenDrawerCloses = -1;

    private DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (drawerToggle != null) drawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if (drawerToggle != null) drawerToggle.onDrawerOpened(drawerView);
            getSupportActionBar().setTitle(R.string.app_name);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            if (drawerToggle != null) drawerToggle.onDrawerClosed(drawerView);
            int position = itemToOpenWhenDrawerCloses;
            if (position >= 0) {
                Bundle extras = ActivityOptionsCompat.makeCustomAnimation(
                        ActionBarCastActivity.this, R.anim.fade_in, R.anim.fade_out).toBundle();

                Class activityClass = drawerMenuContents.getActivity(position);
                ActivityCompat.startActivity(ActionBarCastActivity.this,
                        new Intent(ActionBarCastActivity.this, activityClass), extras);
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (drawerToggle != null) drawerToggle.onDrawerStateChanged(newState);
        }
    };

    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener = this::updateDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d(TAG, "Activity OnCreate");

        PvlApp.get(this).inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!toolbarInitialised) {
            throw new IllegalStateException(
                    "You must run super#initializeToolbar in your onCreate method");
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item != null && item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        toolbar.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        toolbar.setTitle(titleId);
    }

    public MediaControllerCompat getMediaControllerCompat() {
        return mediaController;
    }

    public void setMediaControllerCompat(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }

    protected void initializeToolbar() {
        ButterKnife.inject(this);
        if (toolbar == null) {
            throw new IllegalStateException(
                    "Your layout must include a Toolbar with id `toolbar`");
        }
        toolbar.inflateMenu(R.menu.main);

        if (drawerLayout != null) {
            if (drawerList == null) {
                throw new IllegalStateException(
                        "A layout with a drawer must include a list with id `drawer_list");
            }

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                    toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(drawerListener);
            drawerLayout.setStatusBarBackgroundColor(
                    ResourceHelper.getThemeColor(this, R.attr.colorPrimary, android.R.color.black));
            populateDrawerItems();
            setSupportActionBar(toolbar);
            updateDrawerToggle();
        } else {
            setSupportActionBar(toolbar);
        }

        toolbarInitialised = true;
    }

    private void populateDrawerItems() {
        drawerMenuContents = new DrawerMenuContents(this);

        final int selectedPosition = drawerMenuContents.getPosition(this.getClass());
        final int unselectedColor = getResources().getColor(android.R.color.white);
        final int selectedColor = getResources().getColor(R.color.drawer_item_selected_background);

        SimpleAdapter adapter = new SimpleAdapter(this, drawerMenuContents.getItems(),
                R.layout.drawer_list_item,
                new String[]{DrawerMenuContents.FIELD_TITLE, DrawerMenuContents.FIELD_ICON},
                new int[]{R.id.drawer_item_title, R.id.drawer_item_icon}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                int color = unselectedColor;
                if (position == selectedPosition) {
                    color = selectedColor;
                }
                view.setBackgroundColor(color);

                return view;
            }
        };

        drawerList.setOnItemClickListener((parent, view, position, id) -> {
            if (position != selectedPosition) {
                view.setBackgroundColor(getResources().getColor(
                        R.color.drawer_item_selected_background));
                itemToOpenWhenDrawerCloses = position;
            }
            drawerLayout.closeDrawers();
        });
        drawerList.setAdapter(adapter);
    }

    protected void updateDrawerToggle() {
        if (drawerToggle == null) {
            return;
        }

        boolean isRoot = getFragmentManager().getBackStackEntryCount() == 0;
        drawerToggle.setDrawerIndicatorEnabled(isRoot);
        getSupportActionBar().setDisplayShowHomeEnabled(!isRoot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(!isRoot);
        getSupportActionBar().setHomeButtonEnabled(!isRoot);

        if (isRoot) {
            drawerToggle.syncState();
        }
    }

    /**
     * The first time the app opens, we wanna
     * highlight that sexy cast button
     */
    private void showFtu() {
        Menu menu = toolbar.getMenu();
        View view = menu.findItem(R.id.action_cast).getActionView();
        if (view != null && view instanceof MediaRouteButton) {
            // TODO: ShowcaseView
        }
    }
}
