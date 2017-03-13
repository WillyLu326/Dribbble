package willy.individual.com.dribbble.views.shot_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.shot_detail.ShotActivity;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int SHOT_TYPE = 0;
    private static final int SPINNER_TYPE = 1;

    private List<Shot> shotList;
    private OnLoadingMoreListener onLoadingMoreListener;
    private boolean isShowingSpinner;

    public ShotAdapter(@NonNull List<Shot> shotList, OnLoadingMoreListener onLoadingMoreListener) {
        this.shotList = shotList;
        this.onLoadingMoreListener = onLoadingMoreListener;
        this.isShowingSpinner = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SHOT_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shot_item, parent, false);
            return new ShotViewHolder(view);
        } else if (viewType == SPINNER_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shot_spinner, parent, false);
            return new ShotSpinnerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == SHOT_TYPE) {
            final Shot shot = shotList.get(position);
            final ShotViewHolder shotViewHolder = (ShotViewHolder) holder;

            shotViewHolder.viewsCountTv.setText(String.valueOf(shot.views_count));
            shotViewHolder.likesCountTv.setText(String.valueOf(shot.likes_count));
            shotViewHolder.bucketsCountTv.setText(String.valueOf(shot.butckets_count));
            shotViewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = shotViewHolder.itemView.getContext();
                    Intent intent = new Intent(context, ShotActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (getItemViewType(position) == SPINNER_TYPE) {
            final ShotSpinnerViewHolder shotSpinnerViewHolder = (ShotSpinnerViewHolder) holder;

            onLoadingMoreListener.onLoadingMore();
        }
    }

    @Override
    public int getItemCount() {
        return isShowingSpinner ? shotList.size() + 1 : shotList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == shotList.size()) {
            return SPINNER_TYPE;
        }
        return SHOT_TYPE;
    }

    public void append(List<Shot> moreData) {
        this.shotList.addAll(moreData);
        notifyDataSetChanged();
    }

    public void toggleSpinner(boolean showSpinner) {
        this.isShowingSpinner = showSpinner;
        notifyDataSetChanged();
    }

    public interface OnLoadingMoreListener {
        void onLoadingMore();
    }
}
