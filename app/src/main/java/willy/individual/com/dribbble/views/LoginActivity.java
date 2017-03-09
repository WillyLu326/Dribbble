package willy.individual.com.dribbble.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import willy.individual.com.dribbble.R;
import willy.individual.com.dribbble.views.auth.AuthActivity;

public class LoginActivity extends AppCompatActivity {

    private static int CODE_REQ = 100;


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
        if (requestCode == CODE_REQ && resultCode == Activity.RESULT_OK) {
            String code = data.getStringExtra(AuthActivity.KEY_CODE);
            Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();
        }
    }

    private void setupLoginUI() {
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                startActivityForResult(intent, CODE_REQ);
            }
        });
    }
}
