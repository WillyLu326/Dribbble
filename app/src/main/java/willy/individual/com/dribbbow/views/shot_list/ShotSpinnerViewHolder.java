package willy.individual.com.dribbbow.views.shot_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class ShotSpinnerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.loading_spinner) ProgressBar spinner;
    View itemView;

    public ShotSpinnerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }

}
