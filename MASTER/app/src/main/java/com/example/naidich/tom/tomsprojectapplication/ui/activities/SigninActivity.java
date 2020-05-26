package com.example.naidich.tom.tomsprojectapplication.ui.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.naidich.tom.tomsprojectapplication.R;

public class SigninActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnSignin;
    private final int setTimer = 3000;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnSignin = findViewById(R.id.btnSignin);



        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SigninActivity.this, R.string.emptyError, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userName.isEmpty()) {
                    Toast.makeText(SigninActivity.this, R.string.emptyErrorName, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty()) {
                    Toast.makeText(SigninActivity.this, R.string.emptyErrorPass, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userName.equals(getString(R.string.NAME)) && password.equals(getString(R.string.PASSWORD))) {
                    btnSignin.setEnabled(false);

                    progressbar = findViewById(R.id.progressBar);
                    progressbar.setVisibility(ProgressBar.VISIBLE);

                    new Handler().postDelayed(new Runnable(){

                        @Override
                        public void run() {
                            Intent intent = new Intent(SigninActivity.this, com.example.naidich.tom.tomsprojectapplication.ui.activities.MainActivity.class);
                            finish();
                            startActivity(intent);

                            progressbar.setVisibility(ProgressBar.GONE);
                            btnSignin.setEnabled(true);
                        }
                    }, setTimer);
                } else {
                    Toast.makeText(SigninActivity.this, R.string.errorMatch, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
