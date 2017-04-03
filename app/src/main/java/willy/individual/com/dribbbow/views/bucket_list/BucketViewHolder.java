package willy.individual.com.dribbbow.views.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class BucketViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bucket_item_name) TextView bucketNameTv;
    @BindView(R.id.bucket_shot_count) TextView bucketShotCountTv;
    @BindView(R.id.bucket_item_chosen) CheckBox bucketCheckBox;
    @BindView(R.id.bucket_layout) View bucketView;

    public BucketViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
