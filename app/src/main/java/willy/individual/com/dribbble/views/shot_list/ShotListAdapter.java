package willy.individual.com.dribbble.views.shot_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;

/**
 * Created by zhenglu on 3/5/17.
 */

public class ShotListAdapter extends RecyclerView.Adapter {

    private List<Shot> shotList;

    public ShotListAdapter(@NonNull List<Shot> shotList) {
        this.shotList = shotList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_item, parent, false);

        return new ShotListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Shot shot = shotList.get(position);

        ShotListViewHolder shotViewHolder = (ShotListViewHolder) holder;

        shotViewHolder.viewsCountTv.setText(String.valueOf(shot.views_count));
        shotViewHolder.likesCountTv.setText(String.valueOf(shot.likes_count));
        shotViewHolder.bucketsCountTv.setText(String.valueOf(shot.butckets_count));
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }
}
