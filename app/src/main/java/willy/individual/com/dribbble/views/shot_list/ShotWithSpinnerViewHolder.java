package willy.individual.com.dribbble.views.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/12/17.
 */

public class ShotWithSpinnerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_item_with_spinner_image) ImageView shotWithSpinnerImage;
    @BindView(R.id.shot_item_with_spinner_view_count) TextView shotWithSpinnerViewsCountTv;
    @BindView(R.id.shot_item_with_spinner_like) TextView shotWithSpinnerLikesCountTv;
    @BindView(R.id.shot_item_with_spinner_bucket) TextView shotWithSpinnerBucketsCountTv;

    public ShotWithSpinnerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
