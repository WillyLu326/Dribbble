package willy.individual.com.dribbble.views.profile;

import android.app.Fragment;

import willy.individual.com.dribbble.views.base.SingleFragmentActivity;

/**
 * Created by zhenglu on 3/26/17.
 */

public class ProfileActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return ProfileFragment.newInstance();
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }
}
