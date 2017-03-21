package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;

import willy.individual.com.dribbble.views.base.SingleFragmentActivity;

/**
 * Created by zhenglu on 3/21/17.
 */

public class BucketListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return BucketListFragment.newInstance();
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

}
