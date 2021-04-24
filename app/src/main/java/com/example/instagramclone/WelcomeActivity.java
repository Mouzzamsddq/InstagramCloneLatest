package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public Button nextButton;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;
    /* access modifiers changed from: private */
    public EditText usernameEditText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_welcome);
        this.usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        this.nextButton = (Button) findViewById(R.id.welcomNextButton);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        this.progressLinearLayout = linearLayout;
        linearLayout.setVisibility(8);
        this.nextButton.setVisibility(0);
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WelcomeActivity welcomeActivity = WelcomeActivity.this;
                if (welcomeActivity.validateUsername(welcomeActivity.usernameEditText)) {
                    WelcomeActivity.this.progressLinearLayout.setVisibility(0);
                    WelcomeActivity.this.nextButton.setVisibility(4);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = WelcomeActivity.this.getIntent();
                            String name = intent.getStringExtra("name");
                            String password = intent.getStringExtra("password");
                            String email = intent.getStringExtra("email");
                            String date = intent.getStringExtra("date");
                            String gender = intent.getStringExtra("gender");
                            Intent intent1 = new Intent(WelcomeActivity.this, BioActivity.class);
                            intent1.putExtra("name", name);
                            intent1.putExtra("password", password);
                            intent1.putExtra("email", email);
                            intent1.putExtra("date", date);
                            intent1.putExtra("username", WelcomeActivity.this.usernameEditText.getText().toString());
                            intent1.putExtra("gender", gender);
                            WelcomeActivity.this.startActivity(intent1);
                        }
                    }, 2000);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean validateUsername(EditText usernameEditText2) {
        if (!usernameEditText2.getText().toString().isEmpty()) {
            return true;
        }
        usernameEditText2.setError("username must not be empty");
        return false;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.progressLinearLayout.setVisibility(8);
        this.nextButton.setVisibility(0);
    }
}
