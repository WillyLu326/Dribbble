package willy.individual.com.dribbble.views.shot_detail;

import android.app.Fragment;

import willy.individual.com.dribbble.views.base.SingleFragmentActivity;


public class ShotActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected String getActivityTitle() {
        return "Shot";
    }
}
