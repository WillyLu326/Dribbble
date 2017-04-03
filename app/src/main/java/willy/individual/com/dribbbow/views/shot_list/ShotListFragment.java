package willy.individual.com.dribbbow.views.shot_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbbow.MainActivity;
import willy.individual.com.dribbbow.R;
import willy.individual.com.dribbbow.models.Shot;
import willy.individual.com.dribbbow.utils.ModelUtils;
import willy.individual.com.dribbbow.views.base.DribbbleException;
import willy.individual.com.dribbbow.views.base.DribbbleTask;
import willy.individual.com.dribbbow.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbbow.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbbow.views.bucket_list.BucketAdapter;
import willy.individual.com.dribbbow.views.dribbble.Dribbble;
import willy.individual.com.dribbbow.views.shot_detail.ShotFragment;


public class ShotListFragment extends Fragment{

    public static final int SHOTLIST_FRAGMENT_REQ_CODE = 100;
    private static final int COUNT_PER_PAGE = 12;

    private static final String SHOT_LIST_TYPE = "shot_list_type";

    private ShotListAdapter adapter;
    private int listType;

    @BindView(R.id.shot_list_swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.shot_list_recycler_view) RecyclerView shotListRecyclerView;

    public static ShotListFragment newInstance(int shotListType) {
        Bundle args = new Bundle();
        args.putInt(SHOT_LIST_TYPE, shotListType);
        ShotListFragment shotListFragment = new ShotListFragment();
        shotListFragment.setArguments(args);
        return shotListFragment;
    }

    public static ShotListFragment newBucketInstance(int shotListType, int bucketId) {
        Bundle args = new Bundle();
        args.putInt(SHOT_LIST_TYPE, shotListType);
        args.putInt(BucketAdapter.BUCKET_ID_KEY, bucketId);
        ShotListFragment shotListFragment = new ShotListFragment();
        shotListFragment.setArguments(args);
        return shotListFragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ShotListFragment.SHOTLIST_FRAGMENT_REQ_CODE && resultCode == Activity.RESULT_OK) {

            Shot updateShot = ModelUtils.convertToObject(data.getStringExtra(ShotFragment.SHOT_KEY), new TypeToken<Shot>(){});

            for (Shot shot : adapter.getData()) {
                if (TextUtils.equals(shot.id + "", updateShot.id + "")) {
                    shot.likes_count = updateShot.likes_count;
                    shot.isLike = updateShot.isLike;
                    adapter.notifyDataSetChanged();
                    return ;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shot_recycle_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listType = getArguments().getInt(SHOT_LIST_TYPE);

        setupSwipeContainer();

        shotListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotListRecyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        adapter = new ShotListAdapter(new ArrayList<Shot>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new LoadShotTask(false));
            }
        });
        shotListRecyclerView.setAdapter(adapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new LoadShotTask(true));
            }
        });

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private class LoadShotTask extends DribbbleTask<Void, Void, List<Shot>> {

        private boolean refresh;
        private int page;

        public LoadShotTask(boolean refresh) {
            this.refresh = refresh;
            this.page = refresh ? 1 : adapter.getData().size() / COUNT_PER_PAGE + 1;
        }

        @Override
        protected List<Shot> doJob(Void... params) throws DribbbleException {
            if (listType == MainActivity.SHOT_LIST_POPULAR_TYPE) {
                return Dribbble.getPopularShots(page);
            } else if (listType == MainActivity.SHOT_LIST_LIKE_TYPE) {
                return Dribbble.getLikeShots(page);
            } else if (listType == MainActivity.SHOT_LIST_ANIMATION_TYPE) {
                return Dribbble.getAnimationShots(page);
            } else if (listType == MainActivity.SHOT_LIST_RECENT_VIEW_TYPE)  {
                return Dribbble.getRecentViewedShots(page);
            } else if (listType == MainActivity.BUCKET_SHOT_LIST_TYPE) {
                return Dribbble.getBucketShots(getArguments().getInt(BucketAdapter.BUCKET_ID_KEY), page);
            }
            return Dribbble.getPopularShots(page);
        }

        @Override
        protected void onSuccess(List<Shot> shotList) {
            if (refresh) {
                adapter.clearAllShots();
                adapter.append(shotList);
                swipeContainer.setRefreshing(false);
            } else {
                adapter.append(shotList);
                adapter.toggleSpinner(adapter.getData().size() / COUNT_PER_PAGE >= page);
            }
        }

        @Override
        protected void onFailed(DribbbleException e) {
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

}
