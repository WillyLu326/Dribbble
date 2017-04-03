package willy.individual.com.dribbbow.views.base;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class BucketListSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public BucketListSpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        if (parent.indexOfChild(view) == 0) {
            outRect.top = space;
        }
    }
}
