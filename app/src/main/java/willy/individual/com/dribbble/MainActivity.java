package willy.individual.com.dribbble;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;


import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.login.LoginActivity;
import willy.individual.com.dribbble.views.auth.Auth;
import willy.individual.com.dribbble.views.bucket_list.BucketListFragment;
import willy.individual.com.dribbble.views.shot_list.ShotListFragment;

public class MainActivity extends AppCompatActivity {

    public static final int SHOT_LIST_POPULAR_TYPE = 0;
    public static final int SHOT_LIST_LIKE_TYPE = 1;

    public static final int CHOOSE_BUCKET_TYPE = 2;
    public static final int UNCHOOSE_BUCKET_TYPE = 3;

    public static final int BUCKET_SHOT_LIST_TYPE = 4;

    public static final String USER_KEY = "user_key";

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

        headerView.findViewById(R.id.drawer_header_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.clearAccessToken(getApplicationContext());
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(drawerToggle);

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
                        setTitle(R.string.home_title);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_POPULAR_TYPE);
                        break;
                    case R.id.drawer_menu_like :
                        setTitle(R.string.favoriate_title);
                        fragment = ShotListFragment.newInstance(SHOT_LIST_LIKE_TYPE);
                        break;
                    case R.id.drawer_menu_bucket :
                        setTitle(R.string.bucket_title);
                        fragment = BucketListFragment.newInstance(UNCHOOSE_BUCKET_TYPE, Dribbble.BUCKET_AUTH_USER_URL);
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

    private class LoadAuthUser extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            try {
                return Dribbble.getAuthUser();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            ModelUtils.save(getApplicationContext(), USER_KEY, new TypeToken<User>(){ });
            ((TextView) headerView.findViewById(R.id.drawer_header_username)).setText(user.name);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            ((SimpleDraweeView) headerView.findViewById(R.id.drawer_header_image)).setController(controller);
        }
    }

}
