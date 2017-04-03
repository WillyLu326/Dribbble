package willy.individual.com.dribbbow.views.profile;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Shot;
import willy.individual.com.dribbbow.models.User;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;
import willy.individual.com.dribbbow.views.shot_detail.ShotActivity;
import willy.individual.com.dribbbow.views.shot_detail.ShotFragment;


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
    private ProfileInfoViewHolder profileInfoViewHolder;

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
            profileInfoViewHolder = (ProfileInfoViewHolder) holder;

            AsyncTaskCompat.executeParallel(new CheckUserFollowing(user.username));

            // Setup User Avatar
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

            profileInfoViewHolder.profileStatusBtn.setElevation(20);

            // Setup User Info
            profileInfoViewHolder.profileUsername.setText(user.name);
            profileInfoViewHolder.profileLocation.setText(user.location == null ? "No Location" : user.location);
            profileInfoViewHolder.profileDescription.setText(Html.fromHtml(user.bio));
            profileInfoViewHolder.profileLikesCount.setText("Likes  " + String.valueOf(user.likes_count));
            profileInfoViewHolder.profileFollowersCount.setText("Followers  " + String.valueOf(user.followers_count));

        } else if (getItemViewType(position) == PROFILE_SHOT_TYPE) {
            final Shot shot = profileShots.get(position - 1);
            shot.user = user;

            final ProfileShotViewHolder profileShotViewHolder = (ProfileShotViewHolder) holder;
            profileShotViewHolder.viewsCountTv.setText(String.valueOf(shot.views_count));
            profileShotViewHolder.likesCountTv.setText(String.valueOf(shot.likes_count));
            profileShotViewHolder.bucketsCountTv.setText(String.valueOf(shot.buckets_count));
            profileShotViewHolder.commentsCountTv.setText(String.valueOf(shot.comments_count));

            if (!shot.animated) {
                profileShotViewHolder.gifIv.setVisibility(View.INVISIBLE);
            }

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(shot.getImageUrl())
                    .setAutoPlayAnimations(true)
                    .build();
            profileShotViewHolder.image.setController(controller);

            profileShotViewHolder.cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(profileFragment.getContext(), ShotActivity.class);
                    intent.putExtra(ShotFragment.SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>() {
                    }));
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


    private class FollowUser extends DribbbleTask<Void, Void, Void> {

        private String username;

        public FollowUser(String username) {
            this.username = username;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.followUser(username);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {

        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(profileFragment.getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class UnfollowUser extends  DribbbleTask<Void, Void, Void> {

        private String username;

        public UnfollowUser(String username) {
            this.username = username;
        }

        @Override
        protected Void doJob(Void... params) throws DribbbleException {
            Dribbble.unfollowUser(username);
            return null;
        }

        @Override
        protected void onSuccess(Void aVoid) {

        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(profileFragment.getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private class CheckUserFollowing extends DribbbleTask<Void, Void, Boolean> {

        private String username;

        public CheckUserFollowing(String username) {
            this.username = username;
        }

        @Override
        protected Boolean doJob(Void... params) throws DribbbleException {
            return Dribbble.isFollowingUser(username);
        }

        @Override
        protected void onSuccess(Boolean isFollowing) {
            user.isFollowing = isFollowing;
            if (user.isFollowing) {
                profileInfoViewHolder.profileStatusBtn
                        .setText(profileFragment.getResources().getString(R.string.following));
                profileInfoViewHolder.profileStatusBtn
                        .setTextColor(profileFragment.getResources().getColor(R.color.following_btn_text_color, null));
                profileInfoViewHolder.profileStatusBtn
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sentiment_very_satisfied_white_18dp, 0, 0, 0);
                profileInfoViewHolder.profileStatusBtn
                        .setBackground(profileFragment.getResources().getDrawable(R.drawable.following_btn, null));
            } else {
                profileInfoViewHolder.profileStatusBtn
                        .setText(profileFragment.getResources().getString(R.string.follow));
                profileInfoViewHolder.profileStatusBtn
                        .setTextColor(profileFragment.getResources().getColor(R.color.follow_btn_text_color, null));
                profileInfoViewHolder.profileStatusBtn
                        .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_add_white_18dp, 0, 0, 0);
                profileInfoViewHolder.profileStatusBtn
                        .setBackground(profileFragment.getResources().getDrawable(R.drawable.follow_btn, null));
            }

            profileInfoViewHolder.profileStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.isFollowing = !user.isFollowing;
                    if (user.isFollowing) {
                        AsyncTaskCompat.executeParallel(new FollowUser(user.username));
                        profileInfoViewHolder.profileStatusBtn
                                .setText(profileFragment.getResources().getString(R.string.following));
                        profileInfoViewHolder.profileStatusBtn
                                .setTextColor(profileFragment.getResources().getColor(R.color.following_btn_text_color, null));
                        profileInfoViewHolder.profileStatusBtn
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sentiment_very_satisfied_white_18dp, 0, 0, 0);
                        profileInfoViewHolder.profileStatusBtn
                                .setBackground(profileFragment.getResources().getDrawable(R.drawable.following_btn, null));
                    } else {
                        AsyncTaskCompat.executeParallel(new UnfollowUser(user.username));
                        profileInfoViewHolder.profileStatusBtn
                                .setText(profileFragment.getResources().getString(R.string.follow));
                        profileInfoViewHolder.profileStatusBtn
                                .setTextColor(profileFragment.getResources().getColor(R.color.follow_btn_text_color, null));
                        profileInfoViewHolder.profileStatusBtn
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_group_add_white_18dp, 0, 0, 0);
                        profileInfoViewHolder.profileStatusBtn
                                .setBackground(profileFragment.getResources().getDrawable(R.drawable.follow_btn, null));
                    }
                }
            });
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(profileFragment.getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}