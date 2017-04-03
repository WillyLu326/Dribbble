package willy.individual.com.dribbbow.views.about;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar) Toolbar myToolbar;

    @BindView(R.id.about_dribbble) TextView aboutDribbbleTv;
    @BindView(R.id.introduction_dribbble) TextView introDribbbleTv;
    @BindView(R.id.slogan_dribbble) TextView sloganDribbbleTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
