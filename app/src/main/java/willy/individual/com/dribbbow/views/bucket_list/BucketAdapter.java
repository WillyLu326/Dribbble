package willy.individual.com.dribbbow.views.bucket_list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Bucket;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;


public class BucketAdapter extends RecyclerView.Adapter {

    private static final int BUCKET_TYPE = 0;
    private static final int BUCKET_WITH_SPINNER_TYPE = 1;

    public static final String BUCKET_ID_KEY = "bucket_id";
    public static final String BUCKET_NAME_KEY = "bucket_name";

    public static final String BUCKET_INFO_KEY = "bucket_info_key";

    private List<Bucket> buckets;
    private BucketListFragment bucketListFragment;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean isShowingBucketSpinner;
    private int bucketType;
    private ArrayList<Integer> collectedBucketIds;

    public BucketAdapter(List<Bucket> buckets,
                         BucketListFragment bucketListFragment,
                         OnLoadingMoreListener onLoadingMoreListener, int bucketType) {
        this.buckets = buckets;
        this.bucketListFragment = bucketListFragment;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.bucketType = bucketType;
        this.isShowingBucketSpinner = true;
    }


    public BucketAdapter(List<Bucket> buckets,
                         BucketListFragment bucketListFragment,
                         OnLoadingMoreListener onLoadingMoreListener,
                         int bucketType,
                         ArrayList<Integer> collectedBucketIds) {
        this.buckets = buckets;
        this.bucketListFragment = bucketListFragment;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.bucketType = bucketType;
        this.isShowingBucketSpinner = true;
        this.collectedBucketIds = collectedBucketIds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == BUCKET_TYPE) {
            View bucketItemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bucket_item, parent, false);
            return new BucketViewHolder(bucketItemView);

        } else if (viewType == BUCKET_WITH_SPINNER_TYPE) {
            View bucketSpinnerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bucket_spiner, parent, false);
            return new BucketSpinnerViewHolder(bucketSpinnerView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == BUCKET_TYPE) {

            final Bucket bucket = buckets.get(position);

            final BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            String bucketDescription = bucket.description == null ? "" :  "(" + bucket.description + ")";

            bucketViewHolder.bucketNameTv.setText(bucket.name + " " + bucketDescription);
            bucketViewHolder.bucketShotCountTv.setText(String.valueOf(bucket.shots_count) + " shots");

            if (bucketType == MainActivity.UNCHOOSE_BUCKET_TYPE) {
                bucketViewHolder.bucketCheckBox.setVisibility(View.GONE);
                bucketViewHolder.bucketView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(bucketListFragment.getActivity(), BucketShotListActivity.class);
                        intent.putExtra(BUCKET_ID_KEY, bucket.id);
                        intent.putExtra(BUCKET_NAME_KEY, bucket.name);
                        bucketListFragment.startActivity(intent);
                    }
                });
            } else {
                bucketViewHolder.bucketCheckBox.setVisibility(View.VISIBLE);

                if (collectedBucketIds.contains(bucket.id)) {
                    bucket.isChoosing = true;
                }

                bucketViewHolder.bucketCheckBox.setChecked(bucket.isChoosing);

                bucketViewHolder.bucketCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bucket.isChoosing = !bucket.isChoosing;
                        bucketViewHolder.bucketCheckBox.setChecked(bucket.isChoosing);
                    }
                });

                bucketViewHolder.bucketView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(bucketListFragment.getActivity(), BucketCrudActivity.class);
                        intent.putExtra(BUCKET_INFO_KEY, ModelUtils.convertToString(bucket, new TypeToken<Bucket>(){}));
                        bucketListFragment.startActivityForResult(intent, BucketListFragment.BUCKET_CRUD_REQ_CODE);
                    }
                });
            }
        } else if (getItemViewType(position) == BUCKET_WITH_SPINNER_TYPE) {
            onLoadingMoreListener.onLoadingMore();
        }
    }

    @Override
    public int getItemCount() {
        return isShowingBucketSpinner ? buckets.size() + 1 : buckets.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == buckets.size()) {
            return BUCKET_WITH_SPINNER_TYPE;
        }
        return BUCKET_TYPE;
    }

    public List<Bucket> getData() {
        return buckets;
    }

    public void append(List<Bucket> moreData) {
        this.buckets.addAll(moreData);
        notifyDataSetChanged();
    }

    public void toggleBucketSpinner(boolean showBucketSpinner) {
        this.isShowingBucketSpinner = showBucketSpinner;
        notifyDataSetChanged();
    }

    public void clearAllBuckets() {
        this.buckets.clear();
        notifyDataSetChanged();;
    }

    public List<Bucket> getBuckets() {
        return this.buckets;
    }
}
