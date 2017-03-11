package willy.individual.com.dribbble.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.MainActivity;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.utils.ModelUtils;
import willy.individual.com.dribbble.views.auth.Auth;
import willy.individual.com.dribbble.views.auth.AuthActivity;

public class LoginActivity extends AppCompatActivity {

    private static int AUTH_CODE_REQ = 100;

    @BindView(R.id.login_btn) TextView loginTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupLoginUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_CODE_REQ && resultCode == Activity.RESULT_OK) {
            final String code = data.getStringExtra(AuthActivity.KEY_AUTH_CODE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String accessToken = Auth.fetchAccessToken(code);
                    Auth.login(getApplicationContext(), accessToken);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }
    }

    private void setupLoginUI() {

        Auth.init(getApplicationContext());

        if (Auth.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            loginTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                    startActivityForResult(intent, AUTH_CODE_REQ);
                }
            });
        }

    }
}
