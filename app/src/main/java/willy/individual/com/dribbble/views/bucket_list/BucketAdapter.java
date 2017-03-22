package willy.individual.com.dribbble.views.bucket_list;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;


public class BucketAdapter extends RecyclerView.Adapter {

    private static final int BUCKET_TYPE = 0;
    private static final int BUCKET_WITH_SPINNER_TYPE = 1;

    private List<Bucket> buckets;
    private BucketListFragment bucketListFragment;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean isShowingBucketSpinner;
    private int bucketType;

    public BucketAdapter(List<Bucket> buckets,
                         BucketListFragment bucketListFragment,
                         OnLoadingMoreListener onLoadingMoreListener, int bucketType) {
        this.buckets = buckets;
        this.bucketListFragment = bucketListFragment;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.bucketType = bucketType;
        this.isShowingBucketSpinner = true;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BUCKET_TYPE) {

            Bucket bucket = buckets.get(position);

            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            bucketViewHolder.bucketNameTv.setText(bucket.name);
            bucketViewHolder.bucketShotCountTv.setText(String.valueOf(bucket.shots_count) + " shots");

            if (bucketType == MainActivity.UNCHOOSE_BUCKET_TYPE) {
                bucketViewHolder.bucketCheckBox.setVisibility(View.GONE);
                bucketViewHolder.bucketView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(bucketListFragment.getActivity(), )
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

    public void append(List<Bucket> moreData) {
        this.buckets.addAll(moreData);
        notifyDataSetChanged();
    }

    public void toggleBucketSpinner(boolean showBucketSpinner) {
        this.isShowingBucketSpinner = showBucketSpinner;
        notifyDataSetChanged();
    }

    public List<Bucket> getBuckets() {
        return this.buckets;
    }
}
