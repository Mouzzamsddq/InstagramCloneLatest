package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginOptionActivity extends AppCompatActivity {
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        /* access modifiers changed from: protected */
        public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginOptionActivity.this.emailTextView.setText("");
                Toast.makeText(LoginOptionActivity.this, "User Logged out", 0).show();
                return;
            }
            LoginOptionActivity.this.loadUserProfile(currentAccessToken);
        }
    };
    private LinearLayout alreadyLinearLayout;
    /* access modifiers changed from: private */
    public TextView emailTextView;
    private LoginButton fbLoginButton;
    private CallbackManager mCallBackManager;
    private TextView signUpTextView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login_option);
        checkUserStatus();
        MyApp.printHashKey(this);
        this.emailTextView = (TextView) findViewById(R.id.emailTextView);
        this.fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        this.alreadyLinearLayout = (LinearLayout) findViewById(R.id.alreadyLinearLayout);
        this.signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        this.mCallBackManager = CallbackManager.Factory.create();
        this.fbLoginButton.setReadPermissions((List<String>) Arrays.asList(new String[]{"email", "public_profile"}));
        this.signUpTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginOptionActivity.this.startActivity(new Intent(LoginOptionActivity.this, RegisterActivity.class));
            }
        });
        this.fbLoginButton.registerCallback(this.mCallBackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginOptionActivity.this, "Success", 0).show();
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
                Toast.makeText(LoginOptionActivity.this, "Some Error", 0).show();
            }
        });
        this.alreadyLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginOptionActivity.this, LoginActivity.class);
                intent.addFlags(268468224);
                LoginOptionActivity.this.startActivity(intent);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mCallBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: private */
    public void loadUserProfile(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String str = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=normal";
                    Intent intent = new Intent(LoginOptionActivity.this, HomeActivity.class);
                    intent.putExtra("name", first_name + " " + last_name + "\n" + email);
                    intent.addFlags(268468224);
                    LoginOptionActivity.this.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString(GraphRequest.FIELDS_PARAM, "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void checkUserStatus() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(65536);
            startActivity(intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        checkUserStatus();
        if (isLoggedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(65536);
            startActivity(intent);
        }
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }
}
