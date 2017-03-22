package willy.individual.com.dribbble.views.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class BucketCrudActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar) Toolbar myToolbar;
    @BindView(R.id.bucket_name) EditText bucketNameEt;
    @BindView(R.id.bucket_description) EditText bucketDescriptionEt;
    @BindView(R.id.bucket_curd_btn) Button bucketCrudButton;

    public static String BUCKET_NAME_KEY = "bucket_name_key";
    public static String BUCKET_DESCRIPTION_KEY = "bucket_description_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_crud);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Bucket");

        bucketCrudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndExit();
            }
        });

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

    private void saveAndExit() {
        String bucketName = bucketNameEt.getText().toString();
        String bucketDescription = bucketDescriptionEt.getText().toString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BUCKET_NAME_KEY, bucketName);
        resultIntent.putExtra(BUCKET_DESCRIPTION_KEY, bucketDescription);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
