package willy.individual.com.dribbble;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.views.LoginActivity;
import willy.individual.com.dribbble.views.auth.Auth;
import willy.individual.com.dribbble.views.shot_list.ShotListFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;

    @BindView(R.id.navigation_drawer) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.my_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setupDrawer();

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance())
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

        View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);

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
                        Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
                        setTitle(R.string.home_title);
                        fragment = ShotListFragment.newInstance();
                        break;
                    case R.id.drawer_menu_like :
                        Toast.makeText(getApplicationContext(), "Like Clicked", Toast.LENGTH_SHORT).show();
                        setTitle(R.string.favoriate_title);
                        fragment = ShotListFragment.newInstance();
                        break;
                    case R.id.drawer_menu_bucket :
                        Toast.makeText(getApplicationContext(), "Bucket Clicked", Toast.LENGTH_SHORT).show();
                        setTitle(R.string.bucket_title);
                        fragment = ShotListFragment.newInstance();
                        break;
                }

                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

}
