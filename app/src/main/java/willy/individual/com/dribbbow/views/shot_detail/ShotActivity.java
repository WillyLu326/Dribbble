package willy.individual.com.dribbbow.views.shot_detail;

import android.app.Fragment;

import com.google.gson.reflect.TypeToken;


import willy.individual.com.dribbbow.models.Shot;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.SingleFragmentActivity;


public class ShotActivity extends SingleFragmentActivity {

    public static final int CHOOSEN_BUCKET_ID_REQ = 201;

    @Override
    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected String getActivityTitle() {
         return ModelUtils.convertToObject(getIntent().getStringExtra(ShotFragment.SHOT_KEY), new TypeToken<Shot>(){}).title;
    }

}
