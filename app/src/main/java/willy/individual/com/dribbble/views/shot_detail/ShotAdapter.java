package willy.individual.com.dribbble.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int TYPE_SHOT_IMAGE = 0;
    private static final int TYPE_SHOT_INFO = 1;

    private Shot shot;

    public ShotAdapter(Shot shot) {
        this.shot = shot;
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
            ShotImageViewHolder viewHolder = (ShotImageViewHolder) holder;

        } else if (viewType == TYPE_SHOT_INFO) {
            ShotInfoViewHolder viewHolder = (ShotInfoViewHolder) holder;
            viewHolder.shotInfoViewCountTv.setText(String.valueOf(shot.views_count));
            viewHolder.shotInfoLikeCountTv.setText(String.valueOf(shot.likes_count));
            viewHolder.shotInfoBucketCountTv.setText(String.valueOf(shot.butckets_count));
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
}
