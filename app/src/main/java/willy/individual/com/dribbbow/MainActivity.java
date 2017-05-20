package willy.individual.com.dribbbow;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;


import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.models.User;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;
import willy.individual.com.dribbbow.views.following.FollowingListFragment;
import willy.individual.com.dribbbow.views.login.LoginActivity;
import willy.individual.com.dribbbow.views.auth.Auth;
import willy.individual.com.dribbbow.views.bucket_list.BucketListFragment;
import willy.individual.com.dribbbow.views.shot_list.ShotListFragment;

public class MainActivity extends AppCompatActivity {

    public static final int SHOT_LIST_POPULAR_TYPE = 0;
    public static final int SHOT_LIST_LIKE_TYPE = 1;
    public static final int SHOT_LIST_ANIMATION_TYPE = 10;
    public static final int SHOT_LIST_RECENT_VIEW_TYPE = 11;

    public static final int CHOOSE_BUCKET_TYPE = 2;
    public static final int UNCHOOSE_BUCKET_TYPE = 3;

    public static final int BUCKET_SHOT_LIST_TYPE = 4;

    public static final int FOLLOWING_TYPE = 5;
    public static final int FOLLOWER_TYPE = 6;

    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.navigation_drawer) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;

    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance(SHOT_LIST_POPULAR_TYPE))
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawer() {

        AsyncTaskCompat.executeParallel(new LoadAuthUser());

        headerView = navigationView.inflateHeaderView(R.layout.drawer_header);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.inflateMenu(R.menu.drawer_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;

                switch(item.getItemId()) {
                    case R.id.drawer_menu_home :
                        setTitle(R.string.popular);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_POPULAR_TYPE);
                        break;
                    case R.id.drawer_menu_like :
                        setTitle(R.string.favoriate_title);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_LIKE_TYPE);
                        break;
                    case R.id.drawer_header_view :
                        setTitle(R.string.recent_view);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_RECENT_VIEW_TYPE);
                        break;
                    case R.id.drawer_menu_animation :
                        setTitle(R.string.animation);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_ANIMATION_TYPE);
                        break;
                    case R.id.drawer_menu_bucket :
                        setTitle(R.string.bucket_title);
                        fragment = BucketListFragment.newMainInstance(UNCHOOSE_BUCKET_TYPE, Dribbble.BUCKET_AUTH_USER_URL);
                        break;
                    case R.id.drawer_menu_following :
                        setTitle(R.string.following_title);
                        fragment = FollowingListFragment.newInstance(FOLLOWING_TYPE);
                        break;
                    case R.id.drawer_menu_follower :
                        setTitle(R.string.follower_title);
                        fragment = FollowingListFragment.newInstance(FOLLOWER_TYPE);
                        break;
                    case R.id.drawer_menu_logout :
                        setupExitDialog();
                        break;
                }

                drawerLayout.closeDrawers();

                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void setupExitDialog() {
        new AlertDialog.Builder(this).setMessage("Do you really want to log out Dribbble?")
                .setTitle("Exit")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Auth.clearAccessToken(getApplicationContext());
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

    }

    private class LoadAuthUser extends DribbbleTask<Void, Void, User> {
//
        @Override
        protected User doJob(Void... params) throws DribbbleException {
            return Dribbble.getAuthUser();
        }

        @Override
        protected void onSuccess(User user) {
            if (user == null) {
                Snackbar.make(getWindow().getDecorView(), "No Internet", Snackbar.LENGTH_LONG).show();
                return ;
            }
            Auth.saveAuthUser(getApplicationContext(), user);
            ((TextView) headerView.findViewById(R.id.drawer_header_username)).setText(user.name);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            ((SimpleDraweeView) headerView.findViewById(R.id.drawer_header_image)).setController(controller);
            ((TextView) headerView.findViewById(R.id.drawer_header_profile)).setText(user.location == null ? "No Location" : user.location);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getWindow().getDecorView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

    }
}