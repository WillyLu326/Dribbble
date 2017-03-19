package willy.individual.com.dribbble.views.shot_detail;

import android.app.Fragment;

import com.google.gson.reflect.TypeToken;

import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.SingleFragmentActivity;


public class ShotActivity extends SingleFragmentActivity {

    @Override
    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected String getActivityTitle() {
         return ModelUtils.convertToObject(getIntent().getStringExtra(ShotFragment.SHOT_KEY), new TypeToken<Shot>(){}).title;
    }
}
