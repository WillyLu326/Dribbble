package willy.individual.com.dribbble.views.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;


public class BucketViewHolder extends RecyclerView.ViewHolder {

    public BucketViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
