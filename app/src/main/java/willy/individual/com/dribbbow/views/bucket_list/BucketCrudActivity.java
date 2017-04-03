package willy.individual.com.dribbbow.views.bucket_list;

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
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Bucket;
import willy.individual.com.dribbbow.utils.ModelUtils;


public class BucketCrudActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar) Toolbar myToolbar;
    @BindView(R.id.bucket_name) EditText bucketNameEt;
    @BindView(R.id.bucket_description) EditText bucketDescriptionEt;
    @BindView(R.id.bucket_curd_btn) Button bucketCrudButton;
    @BindView(R.id.bucket_delete_btn) Button bucketDeleteButton;

    public static String BUCKET_KEY = "bucket_key";
    public static String BUCKET_DEKETE_KEY = "bucket_delete_key";

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
            setupAddBucketUI();
        } else {
            setTitle("Update Bucket");
            setupEditBucketUI();
        }

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

    private void setupEditBucketUI() {
        bucketNameEt.setText(bucket.name);
        bucketDescriptionEt.setText(bucket.description);
        bucketCrudButton.setText("UPDATE");
        bucketCrudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSaveAndExit();
            }
        });
        bucketDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSaveAndExit();
            }
        });
    }

    private void setupAddBucketUI() {
        bucketDeleteButton.setVisibility(View.GONE);
        bucketCrudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSaveAndExit();
            }
        });
    }

    private Bucket getBucket() {
        return ModelUtils.convertToObject(getIntent().getStringExtra(BucketAdapter.BUCKET_INFO_KEY), new TypeToken<Bucket>(){});
    }

    private void addSaveAndExit() {
        String bucketName = bucketNameEt.getText().toString();
        if (TextUtils.equals(bucketName, "")) {
            Toast.makeText(getApplicationContext(), "Please input shot name", Toast.LENGTH_SHORT).show();
        } else {
            String bucketDescription = bucketDescriptionEt.getText().toString();
            Intent resultIntent = new Intent();
            bucket = new Bucket();
            bucket.name = bucketName;
            bucket.description = bucketDescription;
            resultIntent.putExtra(BUCKET_KEY, ModelUtils.convertToString(bucket, new TypeToken<Bucket>(){}));
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    private void editSaveAndExit() {
        String bucketName = bucketNameEt.getText().toString();
        if (TextUtils.equals(bucketName, "")) {
            Toast.makeText(getApplicationContext(), "Please input shot name", Toast.LENGTH_SHORT).show();
        } else {
            String bucketDescription = bucketDescriptionEt.getText().toString();
            Intent resultIntent = new Intent();
            bucket.name = bucketName;
            bucket.description = bucketDescription;
            resultIntent.putExtra(BUCKET_KEY, ModelUtils.convertToString(bucket, new TypeToken<Bucket>(){}));
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    private void deleteSaveAndExit() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(BUCKET_DEKETE_KEY, bucket.id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
