package willy.individual.com.dribbble.views.bucket_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.utils.ModelUtils;


public class BucketCrudActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar) Toolbar myToolbar;
    @BindView(R.id.bucket_name) EditText bucketNameEt;
    @BindView(R.id.bucket_description) EditText bucketDescriptionEt;
    @BindView(R.id.bucket_curd_btn) Button bucketCrudButton;

    public static String BUCKET_NAME_KEY = "bucket_name_key";
    public static String BUCKET_DESCRIPTION_KEY = "bucket_description_key";

    private Bucket bucket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_crud);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bucket = getBucket();
        if (bucket == null) {
            setTitle("Add Bucket");
        } else {
            setTitle("Update Bucket");
        }

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

    private void setupEditBucketUI() {}

    private void setupAddBucketUI() {}

    private Bucket getBucket() {
        return ModelUtils.convertToObject(getIntent().getStringExtra(BucketAdapter.BUCKET_INFO_KEY), new TypeToken<Bucket>(){});
    }

    private void saveAndExit() {
        String bucketName = bucketNameEt.getText().toString();
        if (TextUtils.equals(bucketName, "")) {
            Toast.makeText(getApplicationContext(), "Please input shot name", Toast.LENGTH_SHORT).show();
        } else {
            String bucketDescription = bucketDescriptionEt.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(BUCKET_NAME_KEY, bucketName);
            resultIntent.putExtra(BUCKET_DESCRIPTION_KEY, bucketDescription);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
