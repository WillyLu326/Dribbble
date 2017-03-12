package willy.individual.com.dribbble.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/12/17.
 */

public class ShotAdapter extends RecyclerView.Adapter {

    private static final int TYPE_SHOT_IMAGE = 0;
    private static final int TYPE_SHOT_INFO = 1;

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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_SHOT_IMAGE) {
            ShotImageViewHolder viewHolder = (ShotImageViewHolder) holder;

        } else if (viewType == TYPE_SHOT_INFO) {
            ShotInfoViewHolder viewHolder = (ShotInfoViewHolder) holder;
            viewHolder.shotInfoViewCountTv.setText("100");
            viewHolder.shotInfoLikeCountTv.setText("100");
            viewHolder.shotInfoBucketCountTv.setText("100");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
