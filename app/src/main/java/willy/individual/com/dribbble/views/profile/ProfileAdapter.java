package willy.individual.com.dribbble.views.profile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.shot_detail.ShotActivity;
import willy.individual.com.dribbble.views.shot_detail.ShotFragment;
import willy.individual.com.dribbble.views.shot_list.ShotListFragment;


public class ProfileAdapter extends RecyclerView.Adapter {

    public static final int PROFILE_SHOT_REQ = 114;

    private static final int PROFILE_INFO_TYPE = 0;
    private static final int PROFILE_SHOT_TYPE = 1;
    private static final int PROFILE_SPINNER = 2;

    private User user;
    private List<Shot> profileShots = new ArrayList<>();
    private ProfileFragment profileFragment;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean showingSpinner;

    public ProfileAdapter(User user,
                          ProfileFragment profileFragment,
                          List<Shot> profileShots,
                          OnLoadingMoreListener onLoadingMoreListener) {
        this.user = user;
        this.profileFragment = profileFragment;
        this.profileShots = profileShots;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.showingSpinner = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PROFILE_INFO_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_info, parent, false);
            return new ProfileInfoViewHolder(view);
        } else if (viewType == PROFILE_SPINNER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_spinner, parent, false);
            return new ProfileSpinnerViewHolder(view);
        } else if (viewType == PROFILE_SHOT_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.profile_shot_item, parent, false);
            return new ProfileShotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == PROFILE_INFO_TYPE) {
            ProfileInfoViewHolder profileInfoViewHolder = (ProfileInfoViewHolder) holder;
            profileInfoViewHolder.profileUsername.setText(user.name);
            profileInfoViewHolder.profileLocation.setText(user.location);
            profileInfoViewHolder.profileDescription.setText(Html.fromHtml(user.bio, 0));
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            profileInfoViewHolder.profileAvatar.setController(controller);

            Glide.with(profileFragment.getContext())
                    .load(user.avatar_url)
                    .centerCrop()
                    .bitmapTransform(new BlurTransformation(profileFragment.getContext()))
                    .into(profileInfoViewHolder.profileIv);
            profileInfoViewHolder.profileShotsCount.setText("Shots " + String.valueOf(user.shots_count));
            profileInfoViewHolder.profileLikesCount.setText("Likes " + String.valueOf(user.likes_count));
            profileInfoViewHolder.profileFollowersCount.setText("Followers " + String.valueOf(user.followers_count));

        } else if (getItemViewType(position) == PROFILE_SHOT_TYPE) {
            final Shot shot = profileShots.get(position - 1);
            shot.user = user;

            final ProfileShotViewHolder profileShotViewHolder = (ProfileShotViewHolder) holder;
            profileShotViewHolder.viewsCountTv.setText(String.valueOf(shot.views_count));
            profileShotViewHolder.likesCountTv.setText(String.valueOf(shot.likes_count));
            profileShotViewHolder.bucketsCountTv.setText(String.valueOf(shot.buckets_count));
            profileShotViewHolder.commentsCountTv.setText(String.valueOf(shot.comments_count));

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(shot.getImageUrl())
                    .setAutoPlayAnimations(true)
                    .build();
            profileShotViewHolder.image.setController(controller);

            profileShotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(profileFragment.getContext(), ShotActivity.class);
                    intent.putExtra(ShotFragment.SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
                    profileFragment.startActivityForResult(intent, PROFILE_SHOT_REQ);
                }
            });

        } else {
            onLoadingMoreListener.onLoadingMore();
        }
    }

    @Override
    public int getItemCount() {
        return showingSpinner ? 2 + this.profileShots.size() : 1 + this.profileShots.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return PROFILE_INFO_TYPE;
        } else if (position == profileShots.size() + 1) {
            return PROFILE_SPINNER;
        }
        return PROFILE_SHOT_TYPE;
    }

    public void append(List<Shot> shots) {
        this.profileShots.addAll(shots);
        notifyDataSetChanged();
    }

    public void toggleSpinner(boolean isShowingSpinner) {
        this.showingSpinner = isShowingSpinner;
        notifyDataSetChanged();
    }

    public List<Shot> getData() {
        return this.profileShots;
    }

}
