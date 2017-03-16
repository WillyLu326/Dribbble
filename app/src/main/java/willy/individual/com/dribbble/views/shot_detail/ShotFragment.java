package willy.individual.com.dribbble.views.shot_detail;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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


public class ShotFragment extends Fragment {

    public static final String SHOT_KEY = "shot_key";

    private Shot shot;

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
        shot = ModelUtils.convertToObject(getArguments().getString(SHOT_KEY), new TypeToken<Shot>(){});
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(shot, this));
    }


    public void like(Shot shot) {
        AsyncTaskCompat.executeParallel(new LikeShotTask(shot));
    }

    public void unlike(Shot shot) {
        AsyncTaskCompat.executeParallel(new UnlikeShotTask(shot));
    }

    private class LikeShotTask extends AsyncTask<Void, Void, Void> {

        private Shot shot;

        public LikeShotTask(Shot shot) {
            this.shot = shot;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.likeShot(shot.id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setResult(shot);
        }
    }

    private class UnlikeShotTask extends AsyncTask<Void, Void, Void> {

        private Shot shot;

        public UnlikeShotTask(Shot shot) {
            this.shot = shot;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Dribbble.unlikeShot(shot.id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getAdapter().notifyDataSetChanged();
            setResult(shot);
        }
    }

    public void setResult(Shot shot) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOT_KEY, ModelUtils.convertToString(shot, new TypeToken<Shot>(){}));
        this.getActivity().setResult(Activity.RESULT_OK, resultIntent);
    }
}
