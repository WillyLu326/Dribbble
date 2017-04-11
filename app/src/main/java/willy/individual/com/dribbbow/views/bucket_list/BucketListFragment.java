package willy.individual.com.dribbbow.views.bucket_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Bucket;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.BucketListSpaceItemDecoration;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;


public class BucketListFragment extends Fragment {

    @BindView(R.id.bucket_list_recycler_view) RecyclerView bucketRecyclerView;
    @BindView(R.id.bucket_fab) FloatingActionButton bucketFab;
    @BindView(R.id.bucket_swipe_container) SwipeRefreshLayout swipeContainer;

    public static int BUCKET_CRUD_REQ_CODE = 200;

    public static final String CHOOSEN_BUCKET_IDS_KEY = "choosen_bucket_ids_key";
    public static final String TYPE_KEY = "type_key";
    private static final String SHOT_BUCKET_URL_KEY = "shot_bucket_url_key";
    private static final String COLLECTED_BUCKET_IDS_KEY = "cllected_bucket_ids_key";


    private BucketAdapter bucketAdapter;

    public static BucketListFragment newInstance(int type, String shotBucketUrl, ArrayList<Integer> collectedBucketIds) {
        BucketListFragment bucketListFragment = new BucketListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        args.putString(SHOT_BUCKET_URL_KEY, shotBucketUrl);
        args.putIntegerArrayList(COLLECTED_BUCKET_IDS_KEY, collectedBucketIds);
        bucketListFragment.setArguments(args);
        return bucketListFragment;
    }

    public static BucketListFragment newMainInstance(int type, String shotBucketUrl) {
        BucketListFragment bucketListFragment = new BucketListFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_KEY, type);
        args.putString(SHOT_BUCKET_URL_KEY, shotBucketUrl);
        bucketListFragment.setArguments(args);
        return bucketListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BUCKET_CRUD_REQ_CODE && resultCode == Activity.RESULT_OK) {
            int bucketId = data.getIntExtra(BucketCrudActivity.BUCKET_DEKETE_KEY, -1);
            if (bucketId != -1) {
                AsyncTaskCompat.executeParallel(new BucketDeleteTask(bucketId));
            } else {
                Bucket bucket = ModelUtils.convertToObject(data.getStringExtra(BucketCrudActivity.BUCKET_KEY), new TypeToken<Bucket>() {
                });
                if (bucket.id == 0) {
                    AsyncTaskCompat.executeParallel(new BucketCreatedTask(bucket.name, bucket.description));
                } else {
                    AsyncTaskCompat.executeParallel(new BucketUpdateTask(bucket.id, bucket.name, bucket.description));
                }
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        int type = getArguments().getInt(TYPE_KEY, -1);
        if (type == MainActivity.CHOOSE_BUCKET_TYPE) {
            inflater.inflate(R.menu.bucket_choosen_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bucket_choosen_save_btn) {
            List<Integer> choosenBucketIds = new ArrayList<>();
            for(Bucket bucket : bucketAdapter.getData()) {
                if (bucket.isChoosing) {
                    choosenBucketIds.add(bucket.id);
                }
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(CHOOSEN_BUCKET_IDS_KEY, ModelUtils.convertToString(choosenBucketIds, new TypeToken<List<Integer>>(){}));
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);

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
        ArrayList<Integer> collectedBucketIds = getArguments().getIntegerArrayList(COLLECTED_BUCKET_IDS_KEY);

        setupSwipeContainer(bucketType);

        bucketRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bucketRecyclerView.addItemDecoration(new BucketListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));

        if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
            setupBucketFab();

            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType, false));
                }
            }, bucketType, collectedBucketIds);

        } else if (bucketType == MainActivity.UNCHOOSE_BUCKET_TYPE) {
            bucketFab.setVisibility(View.GONE);
            bucketAdapter = new BucketAdapter(new ArrayList<Bucket>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType, getArguments().get(SHOT_BUCKET_URL_KEY).toString(), false));
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

    private void setupSwipeContainer(final int bucketType) {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType, true));
                } else {
                    AsyncTaskCompat.executeParallel(new BucketLoadTask(bucketType, getArguments().get(SHOT_BUCKET_URL_KEY).toString(), true));
                }
            }
        });

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private class BucketLoadTask extends DribbbleTask<Void, Void, List<Bucket>> {

        private int page;
        private int bucketType;
        private String url;
        private boolean refresh;

        public BucketLoadTask(int bucketType, boolean refresh) {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
            this.bucketType = bucketType;
            this.refresh = refresh;
        }

        public BucketLoadTask(int bucketType, String url, boolean refresh) {
            this.page = bucketAdapter.getBuckets().size() / 12 + 1;
            this.bucketType = bucketType;
            this.url = url;
            this.refresh = refresh;
        }

        @Override
        protected List<Bucket> doJob(Void... params) throws DribbbleException {
            if (bucketType == MainActivity.CHOOSE_BUCKET_TYPE) {
                return Dribbble.getBuckets(page);
            } else {
                return Dribbble.getShotBuckets(url, page);
            }
        }

        @Override
        protected void onSuccess(List<Bucket> buckets) {
            if (refresh) {
                bucketAdapter.clearAllBuckets();
                bucketAdapter.append(buckets);
                swipeContainer.setRefreshing(false);
            } else {
                bucketAdapter.append(buckets);
                bucketAdapter.toggleBucketSpinner(bucketAdapter.getBuckets().size() / 12 >= page);
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class BucketCreatedTask extends DribbbleTask<Void, Void, Bucket> {

        private String bucketName;
        private String bucketDescription;

        public BucketCreatedTask(String bucketName, String bucketDescription) {
            this.bucketName = bucketName;
            this.bucketDescription = bucketDescription;
        }

        @Override
        protected Bucket doJob(Void... params) throws DribbbleException {
            return Dribbble.postNewBucket(bucketName, bucketDescription);
        }

        @Override
        protected void onSuccess(Bucket bucket) {
            List<Bucket> list = new ArrayList<>();
            list.add(bucket);
            bucketAdapter.append(list);
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class BucketUpdateTask extends DribbbleTask<Void, Void, Bucket> {

        private int bucketId;
        private String bucketName;
        private String bucketDescription;

        public BucketUpdateTask(int bucketId, String bucketName, String bucketDescription) {
            this.bucketId = bucketId;
            this.bucketName = bucketName;
            this.bucketDescription = bucketDescription;
        }

        @Override
        protected Bucket doJob(Void... params) throws DribbbleException {
            return Dribbble.putExistBucket(bucketId, bucketName, bucketDescription);
        }

        @Override
        protected void onSuccess(Bucket bucket) {
            List<Bucket> buckets = bucketAdapter.getData();
            for (int i = 0; i < buckets.size(); ++i) {
                if (bucket.id == buckets.get(i).id) {
                    buckets.set(i, bucket);
                    break;
                }
            }
            bucketAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class BucketDeleteTask extends DribbbleTask<Void, Void, Void> {

        private int bucketId;

        public BucketDeleteTask(int bucketId) {
            this.bucketId = bucketId;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.deleteExistBucket(bucketId);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {
            List<Bucket> buckets = bucketAdapter.getData();
            for (Bucket bucket : buckets) {
                if (bucket.id == bucketId) {
                    buckets.remove(bucket);
                    break;
                }
            }
            bucketAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

}