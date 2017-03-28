package willy.individual.com.dribbble.views.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/28/17.
 */

public class ProfileSpinnerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.profile_spinner) ProgressBar profileProgressBar;

    public ProfileSpinnerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
