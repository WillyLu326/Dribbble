package willy.individual.com.dribbble.views.following;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class FollowingSpinnerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.following_spinner) ProgressBar followingProgressBar;

    public FollowingSpinnerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
