package willy.individual.com.dribbble.views.base;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhenglu on 3/7/17.
 */

public class ShotListSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ShotListSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.top = space;

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = 0;
        }
    }
}
