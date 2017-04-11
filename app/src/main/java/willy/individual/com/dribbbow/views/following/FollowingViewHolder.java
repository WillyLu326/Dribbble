package willy.individual.com.dribbbow.views.following;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class FollowingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_item_image) SimpleDraweeView userItemImage;
    @BindView(R.id.user_item_name) TextView userItemName;
    @BindView(R.id.user_item_location) TextView userItemLocation;
    @BindView(R.id.user_item_shots_count) TextView userItemShotsCount;
    @BindView(R.id.user_item_view) View userItemView;

    public FollowingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}