package willy.individual.com.dribbble.views.shot_detail;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Comment;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int TYPE_SHOT_IMAGE = 0;
    private static final int TYPE_SHOT_INFO = 1;
    private static final int TYPE_SHOT_COMMENTS = 2;
    private static final int TYPE_SHOT_SPINNER = 3;

    private Shot shot;
    private ShotFragment shotFragment;
    private List<Comment> comments;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean isShowingSpinner;

    private ShotCommentViewHolder shotCommentViewHolder;


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
                    Toast.makeText(shotFragment.getContext(), "Image Click", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (viewType == TYPE_SHOT_INFO) {
            final ShotInfoViewHolder shotInfoViewHolder = (ShotInfoViewHolder) holder;

            shotInfoViewHolder.shotInfoViewCountTv.setText(String.valueOf(shot.views_count));
            shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
            shotInfoViewHolder.shotInfoBucketCountTv.setText(String.valueOf(shot.buckets_count));

            if (shot.isLike) {
                shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
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
                        shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
                        shot.isLike = !shot.isLike;

                        // do unlike async
                        shotFragment.unlike(shot.id);
                    } else {
                        // like this shot
                        shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
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


            shotInfoViewHolder.shotInfoUsername.setText(shot.user.name);
            shotInfoViewHolder.shotInfoUserInfo.setText(shot.user.username);
            shotInfoViewHolder.shotInfoUserDescription.setText(
                    Html.fromHtml(shot.description == null ? "No Description" : shot.description, 1));

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
            shotCommentViewHolder.commentContentTv.setText(Html.fromHtml(comment.body, 1));
            shotCommentViewHolder.commentDateTv.setText(comment.updated_at.toString() + "  |  ");

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

}
