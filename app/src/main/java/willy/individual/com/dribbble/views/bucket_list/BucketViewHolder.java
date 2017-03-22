package willy.individual.com.dribbble.views.bucket_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class BucketViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bucket_item_name) TextView bucketNameTv;
    @BindView(R.id.bucket_item_check_box) CheckBox bucketCheckBox;

    public BucketViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
