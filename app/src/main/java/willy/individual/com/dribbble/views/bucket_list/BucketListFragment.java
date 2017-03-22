package willy.individual.com.dribbble.views.bucket_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.BucketListSpaceItemDecoration;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class BucketListFragment extends Fragment {

    @BindView(R.id.bucket_list_recycler_view) RecyclerView bucketRecyclerView;
    @BindView(R.id.bucket_fab) FloatingActionButton bucketFab;

    private static int BUCKET_CRUD_REQ_CODE = 200;

    public static final String TYPE_KEY = "type_key";
    private static final String SHOT_BUCKET_URL_KEY = "shot_bucket_url_key";

    private BucketAdapter bucketAdapter;

    public static BucketListFragment newInstance(int type, String shotBucketUrl) {
        BucketListFragment bucketListFragment = new BucketListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        args.putString(SHOT_BUCKET_URL_KEY, shotBucketUrl);
        bucketListFragment.setArguments(args);
        return bucketListFragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BUCKET_CRUD_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(BucketCrudActivity.BUCKET_NAME_KEY);
            String bucketDescription = data.getStringExtra(BucketCrudActivity.BUCKET_DESCRIPTION_KEY);
            AsyncTaskCompat.executeParallel(new BucketCreatedTask(bucketName, bucketDescription));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bucket_recycle_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int bucketType = getArguments().getInt(TYPE_KEY);

        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bucketRecyclerView.addItemDecoration(new BucketListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));

        if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
            setupBucketFab();

            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType));
                }
            }, bucketType);

        } else if (bucketType == MainActivity.UNCHOOSE_BUCKET_TYPE) {
            bucketFab.setVisibility(View.GONE);
            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType, getArguments().get(SHOT_BUCKET_URL_KEY).toString()));
                }
            }, bucketType);
        }

        bucketRecyclerView.setAdapter(bucketAdapter);
    }


    private void setupBucketFab() {
        this.bucketFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BucketCrudActivity.class);
                startActivityForResult(intent, BUCKET_CRUD_REQ_CODE);
            }
        });
    }

    private class BucketLoadTask extends AsyncTask<Void, Void, List<Bucket>> {

        private int page;
        private int bucketType;
        private String url;


        public BucketLoadTask(int bucketType) {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
            this.bucketType = bucketType;
        }

        public BucketLoadTask(int bucketType, String url) {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
            this.bucketType = bucketType;
            this.url = url;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
                return Dribbble.getBuckets(page);
            } else {
                return Dribbble.getShotBuckets(url, page);
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            super.onPostExecute(buckets);
            System.out.println(ModelUtils.convertToString(buckets, new TypeToken<List<Bucket>>(){ }));
            bucketAdapter.append(buckets);
            bucketAdapter.toggleBucketSpinner(bucketAdapter.getBuckets().size() / 12 >= page);
        }
    }

    private class BucketCreatedTask extends AsyncTask<Void, Void, Bucket> {

        private String bucketName;
        private String bucketDescription;

        public BucketCreatedTask(String bucketName, String bucketDescription) {
            this.bucketName = bucketName;
            this.bucketDescription = bucketDescription;
        }

        @Override
        protected Bucket doInBackground(Void... params) {
            return Dribbble.postNewBucket(bucketName, bucketDescription);
        }

        @Override
        protected void onPostExecute(Bucket bucket) {
            super.onPostExecute(bucket);
            List<Bucket> list = new ArrayList<>();
            list.add(bucket);
            bucketAdapter.append(list);
        }
    }

}
