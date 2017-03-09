package willy.individual.com.dribbble.views.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

        String url = "https://dribbble.com/oauth/authorize?client_id=76048d257d97a98958efb5bdf0ccbc521b793af5725dbab3b67c55a672080bf4&redirect_uri=http://www.zhenglu326.com&scope=public+write&state=willylu";


        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(url);
    }
}
