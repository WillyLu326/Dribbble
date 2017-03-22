package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;
import android.text.TextUtils;

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
        return "Choose Buckets";
    }

}
