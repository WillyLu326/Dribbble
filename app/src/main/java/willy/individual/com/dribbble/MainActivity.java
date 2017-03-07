package willy.individual.com.dribbble;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.views.shot_list.ShotListFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation_drawer) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance())
                    .commit();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }

                Fragment fragment = null;
                switch (item.getItemId()) {
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
                drawerLayout.closeDrawers();

                if (fragment != null) {
                    getFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, fragment)
                            .commit();
                }

                return true;
            }
        });


    }
}
