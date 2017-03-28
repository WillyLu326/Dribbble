package willy.individual.com.dribbble.views.profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.models.Shot;
import willy.individual.com.dribbble.models.User;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.base.OnLoadingMoreListener;


public class ProfileFragment extends Fragment {

    private static final String USER_STRING_KEY = "user_string_key";

    private ProfileAdapter profileAdapter;

    @BindView(R.id.profile_recycler_view) RecyclerView recyclerView;


    public static ProfileFragment newInstance(String userString) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(USER_STRING_KEY, userString);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = ModelUtils.convertToObject(getArguments().getString(USER_STRING_KEY), new TypeToken<User>(){});

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileAdapter = new ProfileAdapter(user, this, fakeData(), new OnLoadingMoreListener() {
            @Override
            public void onLoadingMore() {
                Toast.makeText(getContext(), "CLick", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(profileAdapter);
    }

    private List<Shot> fakeData() {
        List<Shot> shots = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            shots.add(new Shot());
        }
        return shots;
    }
}

