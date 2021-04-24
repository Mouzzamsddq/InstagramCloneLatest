package com.example.instagramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BirthdayInfoActivity extends AppCompatActivity {
    private ImageView closeImageView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_birthday_info);
        ImageView imageView = (ImageView) findViewById(R.id.closeImageView);
        this.closeImageView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BirthdayInfoActivity.this.finish();
            }
        });
    }
}
