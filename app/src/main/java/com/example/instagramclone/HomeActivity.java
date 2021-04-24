package com.example.instagramclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.instagramclone.Fragment.HomeFragment;
import com.example.instagramclone.Fragment.SearchFragment;
import com.example.instagramclone.Fragment.likeFragment;
import com.example.instagramclone.Fragment.profileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.addButton /*2131361929*/:
                    HomeActivity.this.startActivity(new Intent(HomeActivity.this.getApplicationContext(), AddPostActivity.class));
                    break;
                case R.id.heartButton /*2131362129*/:
                    HomeActivity.this.selectedFragment = new likeFragment();
                    break;
                case R.id.homeButton /*2131362134*/:
                    HomeActivity.this.selectedFragment = new HomeFragment();
                    break;
                case R.id.profileButton /*2131362286*/:
                    HomeActivity.this.selectedFragment = new profileFragment();
                    SharedPreferences.Editor editor = HomeActivity.this.getSharedPreferences("PREFS", 0).edit();
                    editor.putString("profileUID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();
                    break;
                case R.id.serachButton /*2131362356*/:
                    HomeActivity.this.selectedFragment = new SearchFragment();
                    break;
            }
            if (HomeActivity.this.selectedFragment == null) {
                return true;
            }
            if (HomeActivity.this.selectedFragment instanceof HomeFragment) {
                HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainert, HomeActivity.this.selectedFragment, "home").commit();
                return true;
            }
            HomeActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainert, HomeActivity.this.selectedFragment).commit();
            return true;
        }
    };
    Fragment selectedFragment = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        BottomNavigationView bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        this.bottomNavigationView = bottomNavigationView2;
        bottomNavigationView2.setOnNavigationItemSelectedListener(this.navigationItemSelectedListener);
        Bundle intent1 = getIntent().getExtras();
        if (intent1 != null) {
            String publisherUid = intent1.getString("publisherUid");
            SharedPreferences.Editor editor = getSharedPreferences("PREFS", 0).edit();
            editor.putString("profileId", publisherUid);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainert, new profileFragment()).commit();
            return;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainert, new HomeFragment(), "home").commit();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
