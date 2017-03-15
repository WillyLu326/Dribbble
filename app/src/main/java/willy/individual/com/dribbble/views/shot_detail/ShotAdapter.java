package willy.individual.com.dribbble.views.shot_detail;

import android.app.Fragment;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.dribbble.Dribbble;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int TYPE_SHOT_IMAGE = 0;
    private static final int TYPE_SHOT_INFO = 1;

    private Shot shot;
    private Fragment shotFragment;

    private int likes_count = -1;

    public ShotAdapter(Shot shot, Fragment shotFragment) {
        this.shot = shot;
        this.shotFragment = shotFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SHOT_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_image, parent, false);
            return new ShotImageViewHolder(view);
        } else  {
            View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shot_detail_info, parent, false);
            return new ShotInfoViewHolder(view);
        }
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

        } else if (viewType == TYPE_SHOT_INFO) {
            final ShotInfoViewHolder shotInfoViewHolder = (ShotInfoViewHolder) holder;

            shotInfoViewHolder.shotInfoViewCountTv.setText(String.valueOf(shot.views_count));
            shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
            shotInfoViewHolder.shotInfoBucketCountTv.setText(String.valueOf(shot.butckets_count));

            if (shot.isLike) {
                shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
            } else {
                shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_border_black_24dp, 0, 0);
            }

            shotInfoViewHolder.shotInfoLikeCountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int current_count = Integer.parseInt(shotInfoViewHolder.shotInfoLikeCountTv.getText().toString());
                    if (shot.isLike) {
                        // unlike this shot
                        shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_border_black_24dp, 0, 0);
                        likes_count = current_count - 1;
                        AsyncTaskCompat.executeParallel(new UnlikeShotTask(shot.id));
                    } else {
                        // like this shot
                        shotInfoViewHolder.shotInfoLikeCountTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_black_24dp, 0, 0);
                        likes_count = current_count + 1;
                        AsyncTaskCompat.executeParallel(new LikeShotTask(shot.id));
                    }
                    shot.isLike = !shot.isLike;
                    shotInfoViewHolder.shotInfoLikeCountTv.setText(String.valueOf(likes_count));
                }
            });

            shotInfoViewHolder.shotInfoBucketCountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            shotInfoViewHolder.shotInfoUsername.setText(shot.user.name);
            shotInfoViewHolder.shotInfoUserInfo.setText(shot.user.username);
            shotInfoViewHolder.shotInfoUserDescription.setText(
                    Html.fromHtml(shot.description == null ? "" : shot.description, 1));

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(shot.user.avatar_url)
                    .setAutoPlayAnimations(true)
                    .build();
            shotInfoViewHolder.shotInfoUserAvatar.setController(controller);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_SHOT_IMAGE;
        } else {
            return TYPE_SHOT_INFO;
        }
    }

    private class LikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public LikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.likeShot(id);
            return null;
        }
    }

    private class UnlikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public UnlikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.unlikeShot(id);
            return null;
        }
    }
}
