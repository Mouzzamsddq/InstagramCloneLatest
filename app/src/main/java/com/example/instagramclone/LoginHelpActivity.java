package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginHelpActivity extends AppCompatActivity {
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        /* access modifiers changed from: protected */
        public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(LoginHelpActivity.this, "User Logged out", 0).show();
            } else {
                LoginHelpActivity.this.loadUserProfile(currentAccessToken);
            }
        }
    };
    /* access modifiers changed from: private */
    public EditText emailEditText;
    private LoginButton fbLoginButton;
    private CallbackManager mCallBackManager;
    /* access modifiers changed from: private */
    public Button passwordResetNextButton;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login_help);
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordResetNextButton = (Button) findViewById(R.id.resetPasswordNextButton);
        this.fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        this.mCallBackManager = CallbackManager.Factory.create();
        this.fbLoginButton.setReadPermissions((List<String>) Arrays.asList(new String[]{"email", "public_profile"}));
        this.passwordResetNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ValidationClass.validateEmail(LoginHelpActivity.this.emailEditText)) {
                    LoginHelpActivity.this.passwordResetNextButton.setVisibility(4);
                    LoginHelpActivity.this.progressLinearLayout.setVisibility(0);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            FirebaseAuth.getInstance().sendPasswordResetEmail(LoginHelpActivity.this.emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginHelpActivity.this.getApplicationContext(), "We have sent an email to  '" + LoginHelpActivity.this.emailEditText.getText().toString() + "'. Please check your email.", 1).show();
                                        Intent intent = new Intent(LoginHelpActivity.this.getApplicationContext(), LoginActivity.class);
                                        intent.addFlags(268468224);
                                        LoginHelpActivity.this.startActivity(intent);
                                        return;
                                    }
                                    LoginHelpActivity.this.progressLinearLayout.setVisibility(8);
                                    LoginHelpActivity.this.passwordResetNextButton.setVisibility(0);
                                    Toast.makeText(LoginHelpActivity.this.getApplicationContext(), "Sorry, There is something went wrong. please try again some time later.", 1).show();
                                }
                            });
                        }
                    }, 2000);
                }
            }
        });
        this.fbLoginButton.registerCallback(this.mCallBackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginHelpActivity.this, "Success", 0).show();
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
                Toast.makeText(LoginHelpActivity.this, "Some Error", 0).show();
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
                    Intent intent = new Intent(LoginHelpActivity.this, HomeActivity.class);
                    intent.putExtra("name", first_name + " " + last_name + "\n" + email);
                    intent.addFlags(268468224);
                    LoginHelpActivity.this.startActivity(intent);
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
}
