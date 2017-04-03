package willy.individual.com.dribbbow.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.R;


public class ShotImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_detail_image) SimpleDraweeView shotDetailDv;

    public ShotImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
