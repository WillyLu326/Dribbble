package willy.individual.com.dribbble.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/12/17.
 */

public class ShotInfoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_detail_info_view_count) TextView shotInfoViewCountTv;
    @BindView(R.id.shot_detail_info_like_count) TextView shotInfoLikeCountTv;
    @BindView(R.id.shot_detail_info_bucket_count) TextView shotInfoBucketCountTv;

    public ShotInfoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
