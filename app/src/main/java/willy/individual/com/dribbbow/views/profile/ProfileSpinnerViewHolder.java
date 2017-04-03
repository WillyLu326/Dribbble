package willy.individual.com.dribbbow.views.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class ProfileSpinnerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.profile_spinner) ProgressBar profileProgressBar;

    public ProfileSpinnerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
