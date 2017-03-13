package willy.individual.com.dribbble.views.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class ShotViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_item_image) public ImageView image;
    @BindView(R.id.shot_item_view_count) public TextView viewsCountTv;
    @BindView(R.id.shot_item_like) public TextView likesCountTv;
    @BindView(R.id.shot_item_bucket) public TextView bucketsCountTv;

    View itemView;

    public ShotViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }
}
