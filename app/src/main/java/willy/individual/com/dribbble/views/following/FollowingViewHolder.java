package willy.individual.com.dribbble.views.following;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/25/17.
 */

public class FollowingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_item_image) SimpleDraweeView userItemImage;
    @BindView(R.id.user_item_name) TextView userItemName;
    @BindView(R.id.user_item_location) TextView userItemLocation;
    @BindView(R.id.user_item_following) Button userItemFollowingBtn;
    @BindView(R.id.user_item_view) View userItemView;

    public FollowingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
