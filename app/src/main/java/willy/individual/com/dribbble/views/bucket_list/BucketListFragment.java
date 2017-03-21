package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.views.base.BucketListSpaceItemDecoration;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class BucketListFragment extends Fragment {

    @BindView(R.id.bucket_list_recycler_view) RecyclerView bucketRecyclerView;
    @BindView(R.id.bucket_fab) FloatingActionButton bucketFab;

    private BucketAdapter bucketAdapter;

    public static BucketListFragment newInstance() {
        return new BucketListFragment();
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

        setupBucketFab();

        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bucketRecyclerView.addItemDecoration(new BucketListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new BucketLoadTask());
            }
        });
        bucketRecyclerView.setAdapter(bucketAdapter);
    }


    private void setupBucketFab() {
        this.bucketFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class BucketLoadTask extends AsyncTask<Void, Void, List<Bucket>> {

        private int page;

        public BucketLoadTask() {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            return Dribbble.getBuckets(page);
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            super.onPostExecute(buckets);
            bucketAdapter.append(buckets);
            bucketAdapter.toggleBucketSpinner(bucketAdapter.getBuckets().size() / 12 >= page);
        }
    }

}
