package willy.individual.com.dribbbow.views.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class ProfileInfoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.profile_avatar) SimpleDraweeView profileAvatar;
    @BindView(R.id.profile_username) TextView profileUsername;
    @BindView(R.id.profile_location) TextView profileLocation;
    @BindView(R.id.profile_description) TextView profileDescription;
    @BindView(R.id.profile_status_btn) TextView profileStatusBtn;
    @BindView(R.id.profile_background) ImageView profileIv;
    @BindView(R.id.profile_info_content) ViewGroup profileContent;
    @BindView(R.id.profile_likes_count) TextView profileLikesCount;
    @BindView(R.id.profile_followers_count) TextView profileFollowersCount;

    public ProfileInfoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
