package willy.individual.com.dribbbow.views.shot_detail;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Comment;
import willy.individual.com.dribbbow.models.Shot;
import willy.individual.com.dribbbow.models.User;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.bucket_list.BucketListActivity;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;
import willy.individual.com.dribbbow.views.following.FollowingListAdapter;
import willy.individual.com.dribbbow.views.profile.ProfileActivity;
import willy.individual.com.dribbbow.views.shot_imgae_activity.ShotImageActivity;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int TYPE_SHOT_IMAGE = 0;
    private static final int TYPE_SHOT_INFO = 1;
    private static final int TYPE_SHOT_COMMENTS = 2;
    private static final int TYPE_SHOT_SPINNER = 3;

    public static final String IMAGE_URL_KEY = "image_url";
    public static final String BUCKET_KEY = "bucket_key";
    public static final String SHOT_BUCKET_URL_KEY = "shot_bucket_url_key";


    private Shot shot;
    private ShotFragment shotFragment;
    private List<Comment> comments;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean isShowingSpinner;

    private ShotCommentViewHolder shotCommentViewHolder;
    private ShotInfoViewHolder shotInfoViewHolder;

    public ShotAdapter(Shot shot,
                       @NonNull ShotFragment shotFragment,
                       List<Comment> comments,
                       OnLoadingMoreListener onLoadingMoreListener) {
        this.shot = shot;   // come from shotListFragment
        this.shotFragment = shotFragment;
        this.comments = comments;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.isShowingSpinner = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SHOT_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_image, parent, false);
            return new ShotImageViewHolder(view);
        } else if (viewType == TYPE_SHOT_INFO) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_info, parent, false);
            return new ShotInfoViewHolder(view);
        } else if (viewType == TYPE_SHOT_COMMENTS) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_comment, parent, false);
            return new ShotCommentViewHolder(view);
        } else if (viewType == TYPE_SHOT_SPINNER) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_comment_spinner, parent, false);
            return new ShotCommentSpinnerViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_SHOT_IMAGE) {
            ShotImageViewHolder shotImageViewHolder = (ShotImageViewHolder) holder;
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(shot.getImageUrl())
                    .setAutoPlayAnimations(true)
                    .build();
            shotImageViewHolder.shotDetailDv.setController(controller);

            shotImageViewHolder.shotDetailDv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotFragment.getActivity(), ShotImageActivity.class);
                    intent.putExtra(IMAGE_URL_KEY, shot.getImageUrl());
                    shotFragment.startActivity(intent);
                }
            });

        } else if (viewType == TYPE_SHOT_INFO) {
            shotInfoViewHolder = (ShotInfoViewHolder) holder;

            shotInfoViewHolder.shotInfoViewCountTv.setText(String.valueOf(shot.views_count));
            shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
            shotInfoViewHolder.shotInfoBucketCountTv.setText(String.valueOf(shot.buckets_count));

            AsyncTaskCompat.executeParallel(new IsLikeShot(shot));

            shotInfoViewHolder.shotInfoBucketCountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotFragment.getActivity(), BucketListActivity.class);
                    intent.putExtra(ShotAdapter.BUCKET_KEY, MainActivity.UNCHOOSE_BUCKET_TYPE);
                    intent.putExtra(ShotAdapter.SHOT_BUCKET_URL_KEY, shot.buckets_url);
                    shotFragment.startActivity(intent);
                }
            });

            if (shot.bucketed) {
                shotInfoViewHolder.shotInfoMyBucketTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_move_to_inbox_pink_24dp, 0, 0);
                shotInfoViewHolder.shotInfoMyBucketTv.setTextColor(shotFragment.getResources().getColor(R.color.colorAccent));
            } else {
                shotInfoViewHolder.shotInfoMyBucketTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_move_to_inbox_black_24dp, 0, 0);
                shotInfoViewHolder.shotInfoMyBucketTv.setTextColor(shotFragment.getResources().getColor(R.color.text_color));
            }

            shotInfoViewHolder.shotInfoMyBucketTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shotFragment.bucket();
                }
            });

            shotInfoViewHolder.shotInfoShareTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shotFragment.share();
                }
            });

            shotInfoViewHolder.profileView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(shotFragment.getActivity(), ProfileActivity.class);
                    intent.putExtra(FollowingListAdapter.FOLLOWEE_TYPE, ModelUtils.convertToString(shot.user, new TypeToken<User>(){}));
                    intent.putExtra(FollowingListAdapter.FOLLOWEE_NAME, shot.user.name);
                    shotFragment.startActivity(intent);
                }
            });

            shotInfoViewHolder.shotInfoUsername.setText(shot.user.name);
            shotInfoViewHolder.shotInfoUserInfo.setText(shot.user.username);
            if (shot.description == null) {
                shotInfoViewHolder.shotInfoUserDescription.setText("No Description");
            } else {
                shotInfoViewHolder.shotInfoUserDescription.setText(
                        Html.fromHtml(shot.description));
            }

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(shot.user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            shotInfoViewHolder.shotInfoUserAvatar.setController(controller);

            // Setup Comment Header
            if (shot.comments_count == 0) {
                shotInfoViewHolder.line.setVisibility(View.GONE);
                shotInfoViewHolder.commentResponseTv.setVisibility(View.GONE);
            } else {
                shotInfoViewHolder.commentResponseTv.setText(shot.comments_count + " Response");
            }
        } else if (viewType == TYPE_SHOT_COMMENTS) {
            Comment comment = comments.get(position - 2);

            shotCommentViewHolder = (ShotCommentViewHolder) holder;
            shotCommentViewHolder.commentNameTv.setText(comment.user.name);
            shotCommentViewHolder.commentContentTv.setText(Html.fromHtml(comment.body));
            shotCommentViewHolder.commentDateTv.setText("Update Date: " + comment.updated_at.toString());

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(comment.user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            shotCommentViewHolder.commentUserImage.setController(controller);

        } else if (viewType == TYPE_SHOT_SPINNER) {
            onLoadingMoreListener.onLoadingMore();
        }
    }

    @Override
    public int getItemCount() {
        if (isShowingSpinner) {
            return 3 + this.comments.size();
        } else {
            return 2 + this.comments.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_SHOT_IMAGE;
        } else if (position == 1) {
            return TYPE_SHOT_INFO;
        } else if (position == this.comments.size() + 2) {
            return TYPE_SHOT_SPINNER;
        } else {
            return TYPE_SHOT_COMMENTS;
        }
    }

    public void append(List<Comment> comments) {
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    public List<Comment> getCommentsData() {
        return this.comments;
    }

    public void toggleSpinner(boolean showingSpinner) {
        this.isShowingSpinner = showingSpinner;
        notifyDataSetChanged();
    }


    private class IsLikeShot extends DribbbleTask<Void, Void, Boolean> {

        private Shot shot;

        public IsLikeShot(Shot shot) {
            this.shot = shot;
        }

        @Override
        protected Boolean doJob(Void... params) throws DribbbleException {
            return Dribbble.isLikeShot(shot.id);
        }

        @Override
        protected void onSuccess(Boolean aBoolean) {
            shot.isLike = aBoolean;
            if (shot.isLike) {
                shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
                shotInfoViewHolder.shotInfoLikeCountTv.setTextColor(shotFragment.getResources().getColor(R.color.colorAccent));
            } else {
                shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_border_black_24dp, 0, 0);
            }

            shotInfoViewHolder.shotInfoLikeCountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shot.isLike) {
                        // unlike this shot
                        shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_border_black_24dp, 0, 0);
                        shot.likes_count -= 1;
                        shotInfoViewHolder.shotInfoLikeCountTv.setTextColor(shotFragment.getResources().getColor(R.color.black, null));
                        shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
                        shot.isLike = !shot.isLike;

                        // do unlike async
                        shotFragment.unlike(shot.id);
                    } else {
                        // like this shot
                        shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
                        shotInfoViewHolder.shotInfoLikeCountTv.setTextColor(shotFragment.getResources().getColor(R.color.colorAccent));
                        shot.likes_count += 1;
                        shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
                        shot.isLike = !shot.isLike;

                        // do like async
                        shotFragment.like(shot.id);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(ShotFragment.SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
                    shotFragment.getActivity().setResult(Activity.RESULT_OK, resultIntent);
                }
            });
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(shotFragment.getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

}