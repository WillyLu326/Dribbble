package willy.individual.com.dribbble.views.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/5/17.
 */

public class ShotListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_item_image) ImageView image;
    @BindView(R.id.shot_item_view_count) TextView viewsCountTv;
    @BindView(R.id.shot_item_like) TextView likesCountTv;
    @BindView(R.id.shot_item_bucket) TextView bucketsCountTv;

    public ShotListViewHolder(View itemView) {
        super(itemView);
    }
}
