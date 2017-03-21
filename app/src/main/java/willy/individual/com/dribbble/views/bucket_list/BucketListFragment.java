package willy.individual.com.dribbble.views.bucket_list;

import android.app.Fragment;
import android.content.Intent;
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
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.views.base.BucketListSpaceItemDecoration;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.shot_detail.ShotAdapter;


public class BucketListFragment extends Fragment {

    @BindView(R.id.bucket_list_recycler_view) RecyclerView bucketRecyclerView;
    @BindView(R.id.bucket_fab) FloatingActionButton bucketFab;

    public static final String TYPE_KEY = "type_key";

    private BucketAdapter bucketAdapter;

    public static BucketListFragment newInstance(int type) {
        BucketListFragment bucketListFragment = new BucketListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        bucketListFragment.setArguments(args);
        return bucketListFragment;
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

        BucketListActivity bla = new BucketListActivity();

        System.out.println("======================");
        System.out.println("======================");
        System.out.println("======================");
        System.out.println(bla.getShotBucketUrl());
        System.out.println("======================");
        System.out.println("======================");
        System.out.println("======================");

        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bucketRecyclerView.addItemDecoration(new BucketListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));

        if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
            setupBucketFab();

            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType));
                }
            });
            bucketRecyclerView.setAdapter(bucketAdapter);

        } else if (bucketType == MainActivity.UNCHOOSE_BUCKET_TYPE) {
            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType));
                }
            });
        }



    }


    private void setupBucketFab() {
        this.bucketFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BucketCrudActivity.class);
                startActivity(intent);
            }
        });
    }

    private class BucketLoadTask extends AsyncTask<Void, Void, List<Bucket>> {

        private int page;
        private int bucketType;

        BucketListActivity bla = new BucketListActivity();



        public BucketLoadTask(int bucketType) {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
            this.bucketType = bucketType;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
                return Dribbble.getBuckets(page);
            } else {
                return Dribbble.getShotBuckets(bla.getShotBucketUrl(), page);
            }
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            super.onPostExecute(buckets);
            bucketAdapter.append(buckets);
            bucketAdapter.toggleBucketSpinner(bucketAdapter.getBuckets().size() / 12 >= page);
        }
    }


}
