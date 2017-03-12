package willy.individual.com.dribbble.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;

/**
 * Created by zhenglu on 3/12/17.
 */

public class ShotImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_detail_image) ImageView shotDetailIv;

    public ShotImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
