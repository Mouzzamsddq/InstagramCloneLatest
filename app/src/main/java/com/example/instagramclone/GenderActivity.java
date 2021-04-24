package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class GenderActivity extends AppCompatActivity {
    private ImageView backImageView;
    String date;
    String email;
    /* access modifiers changed from: private */
    public RadioButton femaleRadioButton;
    String gender;
    /* access modifiers changed from: private */
    public Button genderNextButton;
    /* access modifiers changed from: private */
    public RadioButton maleRadioButton;
    String name;
    String password;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_gender);
        this.maleRadioButton = (RadioButton) findViewById(R.id.maleRadioButton);
        this.femaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);
        this.genderNextButton = (Button) findViewById(R.id.genderNextButton);
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        ImageView imageView = (ImageView) findViewById(R.id.backButton);
        this.backImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GenderActivity.this.finish();
            }
        });
        this.progressLinearLayout.setVisibility(8);
        this.genderNextButton.setVisibility(0);
        this.maleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (GenderActivity.this.maleRadioButton.isChecked()) {
                    GenderActivity.this.femaleRadioButton.setChecked(false);
                } else {
                    GenderActivity.this.maleRadioButton.setChecked(false);
                }
            }
        });
        this.femaleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (GenderActivity.this.femaleRadioButton.isChecked()) {
                    GenderActivity.this.maleRadioButton.setChecked(false);
                }
            }
        });
        this.genderNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GenderActivity.this.progressLinearLayout.setVisibility(0);
                GenderActivity.this.genderNextButton.setVisibility(4);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (GenderActivity.this.maleRadioButton.isChecked()) {
                            GenderActivity.this.gender = "Male";
                        } else {
                            GenderActivity.this.gender = "Female";
                        }
                        Intent intent1 = GenderActivity.this.getIntent();
                        GenderActivity.this.name = intent1.getStringExtra("name");
                        GenderActivity.this.email = intent1.getStringExtra("email");
                        GenderActivity.this.password = intent1.getStringExtra("password");
                        GenderActivity.this.date = intent1.getStringExtra("date");
                        Intent intent2 = new Intent(GenderActivity.this, WelcomeActivity.class);
                        intent2.putExtra("name", GenderActivity.this.name);
                        intent2.putExtra("password", GenderActivity.this.password);
                        intent2.putExtra("email", GenderActivity.this.email);
                        intent2.putExtra("date", GenderActivity.this.date);
                        intent2.putExtra("gender", GenderActivity.this.gender);
                        GenderActivity.this.startActivity(intent2);
                    }
                }, 2000);
            }
        });
    }
}
