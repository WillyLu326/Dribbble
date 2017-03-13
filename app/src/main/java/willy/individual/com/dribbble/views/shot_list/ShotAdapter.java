package willy.individual.com.dribbble.views.shot_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.views.shot_detail.ShotActivity;


public class ShotAdapter extends RecyclerView.Adapter {

    private static final int SHOT_TYPE = 0;
    private static final int SHOT_WITH_SPINNER_TYPE = 1;

    private List<Shot> shotList;

    public ShotAdapter(@NonNull List<Shot> shotList) {
        this.shotList = shotList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shot_item, parent, false);

        return new ShotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == shotList.size() - 1) {
            return SHOT_WITH_SPINNER_TYPE;
        }
        return SHOT_TYPE;
    }
}
