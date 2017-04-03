package willy.individual.com.dribbbow.views.bucket_list;

import android.app.Fragment;

import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.views.base.SingleFragmentActivity;
import willy.individual.com.dribbbow.views.shot_list.ShotListFragment;


public class BucketShotListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return ShotListFragment.newBucketInstance(MainActivity.BUCKET_SHOT_LIST_TYPE, getIntent().getIntExtra(BucketAdapter.BUCKET_ID_KEY, -1));
    }

    @Override
    protected String getActivityTitle() {
        return getIntent().getStringExtra(BucketAdapter.BUCKET_NAME_KEY).toString();
    }
}
