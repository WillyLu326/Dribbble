package willy.individual.com.dribbble.views.shot_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;



public class ShotCommentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shot_detail_comment_image) SimpleDraweeView commentUserImage;
    @BindView(R.id.shot_detail_comment_name) TextView commentNameTv;
    @BindView(R.id.shot_detail_comment_content) TextView commentContentTv;
    @BindView(R.id.shot_detail_comment_time) TextView commentDateTv;
    @BindView(R.id.shot_detail_comment_like) ImageView commentLikeIv;

    public View itemView;

    public ShotCommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
    }
}
