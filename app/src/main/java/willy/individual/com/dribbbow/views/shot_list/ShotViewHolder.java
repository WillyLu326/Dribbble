package willy.individual.com.dribbbow.views.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class ShotViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_item_cover) View cover;
    @BindView(R.id.shot_item_image) SimpleDraweeView image;
    @BindView(R.id.shot_item_view_count) TextView viewsCountTv;
    @BindView(R.id.shot_item_like) TextView likesCountTv;
    @BindView(R.id.shot_item_comment) TextView commentsCountTv;
    @BindView(R.id.shot_item_bucket) TextView bucketsCountTv;
    @BindView(R.id.shot_item_gif) ImageView gifIv;

    View itemView;

    public ShotViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }
}
