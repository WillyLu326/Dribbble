package willy.individual.com.dribbble.views.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Bucket;


public class BucketAdapter extends RecyclerView.Adapter {

    private List<Bucket> buckets;

    public BucketAdapter(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bucketItemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.bucket_item, parent, false);
        return new BucketViewHolder(bucketItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bucket bucket = buckets.get(position);

    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }

}
