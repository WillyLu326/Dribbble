package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;
import android.view.Menu;

import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.views.base.SingleFragmentActivity;
import willy.individual.com.dribbble.views.shot_detail.ShotAdapter;


public class BucketListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return BucketListFragment
                .newInstance(getIntent().getIntExtra(ShotAdapter.BUCKET_KEY, -1),
                        getIntent().getStringExtra(ShotAdapter.SHOT_BUCKET_URL_KEY));
    }

    @Override
    protected String getActivityTitle() {
        if (getIntent().getIntExtra(ShotAdapter.BUCKET_KEY, -1) == MainActivity.UNCHOOSE_BUCKET_TYPE) {
            return "All Buckets";
        }
        return "Choose Buckets";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
