package willy.individual.com.dribbble.views.shot_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;
import willy.individual.com.dribbble.views.base.ShotListSpaceItemDecoration;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.shot_detail.ShotFragment;


public class ShotListFragment extends Fragment{

    public static final int SHOTLIST_FRAGMENT_REQ_CODE = 100;
    private static final int COUNT_PER_PAGE = 12;
    private static final String SHOT_LIST_TYPE = "shot_list_type";

    @BindView(R.id.shot_list_swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.shot_list_recycler_view) RecyclerView shotListRecyclerView;

    private ShotListAdapter adapter;

    public static ShotListFragment newInstance(int shotListType) {
        Bundle args = new Bundle();
        args.putInt(SHOT_LIST_TYPE, shotListType);
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
        setupSwipeContainer();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int shotListType = getArguments().getInt(SHOT_LIST_TYPE);

        shotListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotListRecyclerView.addItemDecoration(new ShotListSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_space)));
        adapter = new ShotListAdapter(new ArrayList<Shot>(), this, new OnLoadingMoreListener() {
                @Override
                public void onLoadingMore() {
                AsyncTaskCompat.executeParallel(new LoadShotTask(shotListType, adapter.getItemCount() / COUNT_PER_PAGE + 1));
            }
        });
        shotListRecyclerView.setAdapter(adapter);
    }

    private void setupSwipeContainer() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncTaskCompat.executeParallel(new LoadRefreshShotTask());
            }
        });

        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorPrimary, null));
    }

    private class LoadShotTask extends AsyncTask<Void, Void, List<Shot>> {

        private int shotListType;
        private int page;

        public LoadShotTask(int shotListType, int page) {
            this.shotListType = shotListType;
            this.page = page;
        }

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                if (shotListType == MainActivity.SHOT_LIST_POPULAR_TYPE) {
                    return Dribbble.getPopularShots(page);
                } else if (shotListType == MainActivity.SHOT_LIST_LIKE_TYPE) {
                    return Dribbble.getLikeShots(page);
                }
                return Dribbble.getPopularShots(page);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shotList) {
            super.onPostExecute(shotList);
            adapter.append(shotList);
            adapter.toggleSpinner(adapter.getItemCount() / COUNT_PER_PAGE >= page);
        }
    }

    private class LoadRefreshShotTask extends AsyncTask<Void, Void, List<Shot>> {

        @Override
        protected List<Shot> doInBackground(Void... params) {
            try {
                int listType = getArguments().getInt(SHOT_LIST_TYPE);
                if (listType == MainActivity.SHOT_LIST_POPULAR_TYPE) {
                    return Dribbble.getPopularShots(1);
                } else if (listType == MainActivity.SHOT_LIST_LIKE_TYPE) {
                    return Dribbble.getLikeShots(1);
                }
                return new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Shot> shotList) {
            super.onPostExecute(shotList);
            adapter.clearAllShots();
            adapter.addNewShots(shotList);
            swipeContainer.setRefreshing(false);
        }
    }
}
