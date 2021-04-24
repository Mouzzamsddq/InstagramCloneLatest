package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.activity_main);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), LoginOptionActivity.class);
                intent.addFlags(268468224);
                MainActivity.this.startActivity(intent);
            }
        }, 2000);
    }
}
