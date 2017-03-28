package willy.individual.com.dribbble.views.profile;

import android.app.Fragment;

import willy.individual.com.dribbble.views.base.SingleFragmentActivity;
import willy.individual.com.dribbble.views.following.FollowingListAdapter;


public class ProfileActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return ProfileFragment.newInstance(getIntent().getStringExtra(FollowingListAdapter.FOLLOWEE_TYPE));
    }

    @Override
    protected String getActivityTitle() {
        return getIntent().getStringExtra(FollowingListAdapter.FOLLOWEE_NAME);
    }
}
