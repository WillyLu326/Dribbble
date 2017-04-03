package willy.individual.com.dribbbow.views.bucket_list;

import android.app.Fragment;

import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.views.base.SingleFragmentActivity;
import willy.individual.com.dribbbow.views.shot_detail.ShotAdapter;
import willy.individual.com.dribbbow.views.shot_detail.ShotFragment;


public class BucketListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return BucketListFragment
                .newInstance(getIntent().getIntExtra(ShotAdapter.BUCKET_KEY, -1),
                        getIntent().getStringExtra(ShotAdapter.SHOT_BUCKET_URL_KEY),
                        getIntent().getIntegerArrayListExtra(ShotFragment.COLLECTED_BUCKET_IDS_KEY));
    }

    @Override
    protected String getActivityTitle() {
        if (getIntent().getIntExtra(ShotAdapter.BUCKET_KEY, -1) == MainActivity.UNCHOOSE_BUCKET_TYPE) {
            return "All Buckets";
        }
        return "Choose Buckets";
    }

}
