package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {
    private LinearLayout alreadyLinearLayout;
    TabLayout tabLayout;
    ViewPager viewPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_register);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.alreadyLinearLayout = (LinearLayout) findViewById(R.id.alreadyLinearLayout);
        setupViewPager(this.viewPager);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.alreadyLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(268468224);
                RegisterActivity.this.startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PhoneFragment(), "Phone number");
        viewPagerAdapter.addFragment(new EmailFragment(), "email address");
        viewPager2.setAdapter(viewPagerAdapter);
    }
}
