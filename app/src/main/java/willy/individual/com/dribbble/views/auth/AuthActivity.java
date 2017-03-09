package willy.individual.com.dribbble.views.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;


public class AuthActivity extends AppCompatActivity {

    @BindView(R.id.my_toolbar) Toolbar toolbar;
    @BindView(R.id.web_view) WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        String url = getIntent().getStringExtra("");

        webView.loadUrl(url);
    }
}
