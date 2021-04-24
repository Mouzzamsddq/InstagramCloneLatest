package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        /* access modifiers changed from: protected */
        public void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(LoginActivity.this, "User Logged out", 0).show();
            } else {
                LoginActivity.this.loadUserProfile(currentAccessToken);
            }
        }
    };
    /* access modifiers changed from: private */
    public EditText emailEditText;
    LoginButton fbLoginButton;
    private LinearLayout forgottenPasswordLinearLayout;
    /* access modifiers changed from: private */
    public Button loginButton;
    CallbackManager mCallBackManager;
    /* access modifiers changed from: private */
    public EditText passwordEditText;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;
    private LinearLayout signUpLinearLayout;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);
        this.emailEditText = (EditText) findViewById(R.id.emailEditText);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.signUpLinearLayout = (LinearLayout) findViewById(R.id.linear2);
        this.forgottenPasswordLinearLayout = (LinearLayout) findViewById(R.id.linear1);
        this.fbLoginButton = (LoginButton) findViewById(R.id.fbLoginButton);
        this.mCallBackManager = CallbackManager.Factory.create();
        this.fbLoginButton.setReadPermissions((List<String>) Arrays.asList(new String[]{"email", "public_profile"}));
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        this.signUpLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginOptionActivity.class);
                intent.addFlags(268468224);
                LoginActivity.this.startActivity(intent);
            }
        });
        this.forgottenPasswordLinearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this.getApplicationContext(), LoginHelpActivity.class));
            }
        });
        this.fbLoginButton.registerCallback(this.mCallBackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Success", 0).show();
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Some Error", 0).show();
            }
        });
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ValidationClass.validateEmail(LoginActivity.this.emailEditText)) {
                    LoginActivity.this.progressLinearLayout.setVisibility(0);
                    LoginActivity.this.loginButton.setVisibility(4);
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(LoginActivity.this.emailEditText.getText().toString(), LoginActivity.this.passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Successfully Logged in", 0).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(268468224);
                                LoginActivity.this.startActivity(intent);
                                return;
                            }
                            LoginActivity.this.progressLinearLayout.setVisibility(8);
                            LoginActivity.this.loginButton.setVisibility(0);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Wrong password...", 0).show();
                            }
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(LoginActivity.this, "email not found", 0).show();
                            }
                        }
                    });
                }
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
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("name", first_name + " " + last_name + "\n" + email);
                    intent.addFlags(268468224);
                    LoginActivity.this.startActivity(intent);
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
