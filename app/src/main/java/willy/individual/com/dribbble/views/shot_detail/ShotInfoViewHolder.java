package willy.individual.com.dribbble.views.shot_detail;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class ShotInfoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_detail_info_view_count) TextView shotInfoViewCountTv;
    @BindView(R.id.shot_detail_info_like_count) TextView shotInfoLikeCountTv;
    @BindView(R.id.shot_detail_info_bucket_count) TextView shotInfoBucketCountTv;

    @BindView(R.id.shot_detail_info_user_image) SimpleDraweeView shotInfoUserAvatar;
    @BindView(R.id.shot_detail_info_username) TextView shotInfoUsername;
    @BindView(R.id.shot_detail_info_user_info) TextView shotInfoUserInfo;
    @BindView(R.id.shot_detail_info_user_description) TextView shotInfoUserDescription;


    public ShotInfoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
