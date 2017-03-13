package willy.individual.com.dribbble.views.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;


public class BucketAdapter extends RecyclerView.Adapter {

    private static final int BUCKET_TYPE = 0;
    private static final int BUCKET_WITH_SPINNER_TYPE = 1;

    private List<Bucket> buckets;

    public BucketAdapter(List<Bucket> buckets) {
        this.buckets = buckets;
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
        } else if (getItemViewType(position) == BUCKET_WITH_SPINNER_TYPE) {

        }
    }

    @Override
    public int getItemCount() {
        return buckets.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == buckets.size()) {
            return BUCKET_WITH_SPINNER_TYPE;
        }
        return BUCKET_TYPE;
    }
}
