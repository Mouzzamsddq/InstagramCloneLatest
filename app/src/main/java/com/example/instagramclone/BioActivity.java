package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BioActivity extends AppCompatActivity {
    ImageView backImageView;
    /* access modifiers changed from: private */
    public EditText bioEditText;
    /* access modifiers changed from: private */
    public Button continueButton;
    String date;
    String email;
    String gender;
    String name;
    String password;
    LinearLayout progressLinearLayout;
    private TextView skipTextView;
    String username;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_bio);
        this.bioEditText = (EditText) findViewById(R.id.bioEditText);
        this.skipTextView = (TextView) findViewById(R.id.skipTextView);
        this.continueButton = (Button) findViewById(R.id.continueButton);
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        ImageView imageView = (ImageView) findViewById(R.id.backButton);
        this.backImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BioActivity.this.finish();
            }
        });
        this.continueButton.setVisibility(0);
        this.progressLinearLayout.setVisibility(8);
        Intent intent = getIntent();
        this.name = intent.getStringExtra("name");
        this.password = intent.getStringExtra("password");
        this.email = intent.getStringExtra("email");
        this.username = intent.getStringExtra("username");
        this.date = intent.getStringExtra("date");
        this.gender = intent.getStringExtra("gender");
        this.continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(BioActivity.this.bioEditText.getText().toString())) {
                    BioActivity.this.bioEditText.setError("bio must not be empty...!");
                    BioActivity.this.bioEditText.requestFocus();
                    return;
                }
                BioActivity.this.progressLinearLayout.setVisibility(0);
                BioActivity.this.continueButton.setVisibility(4);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String bio = BioActivity.this.bioEditText.getText().toString();
                        Intent intent1 = new Intent(BioActivity.this, UploadProfileImageActivity.class);
                        intent1.putExtra("name", BioActivity.this.name);
                        intent1.putExtra("password", BioActivity.this.password);
                        intent1.putExtra("email", BioActivity.this.email);
                        intent1.putExtra("date", BioActivity.this.date);
                        intent1.putExtra("bio", bio);
                        intent1.putExtra("username", BioActivity.this.username);
                        intent1.putExtra("gender", BioActivity.this.gender);
                        BioActivity.this.startActivity(intent1);
                    }
                }, 2000);
            }
        });
        this.skipTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(BioActivity.this, UploadProfileImageActivity.class);
                intent1.putExtra("name", BioActivity.this.name);
                intent1.putExtra("password", BioActivity.this.password);
                intent1.putExtra("email", BioActivity.this.email);
                intent1.putExtra("date", BioActivity.this.date);
                intent1.putExtra("username", BioActivity.this.username);
                BioActivity.this.startActivity(intent1);
            }
        });
    }
}
