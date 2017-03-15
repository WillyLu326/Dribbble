package willy.individual.com.dribbble.views.shot_detail;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.dribbble.Dribbble;
import willy.individual.com.dribbble.views.shot_list.ShotListFragment;


public class ShotFragment extends Fragment {

    public static final String SHOT_KEY = "shot_key";

    @BindView(R.id.shot_list_recycler_view) RecyclerView recyclerView;

    public static Fragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(getShotFromShotListFragment(), this));
    }

    private Shot getShotFromShotListFragment() {
        return ModelUtils.convertToObject(getActivity().getIntent().getStringExtra(SHOT_KEY), new TypeToken<Shot>(){});
    }


    public void like(int id) {
        AsyncTaskCompat.executeParallel(new LikeShotTask(id));
    }

    public void unlike(int id) {
        AsyncTaskCompat.executeParallel(new UnlikeShotTask(id));
    }

    private class LikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public LikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.likeShot(id);
            return null;
        }


    }

    private class UnlikeShotTask extends AsyncTask<Void, Void, Void> {

        private int id;

        public UnlikeShotTask(int id) {
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.unlikeShot(id);
            return null;
        }
    }
}
