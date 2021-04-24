package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    public static String FILE_NAME = "";
    private static final String TAG = "PasswordActivity";
    /* access modifiers changed from: private */
    public EditText confirmPasswordEditText;
    FirebaseAuth mAuth;
    /* access modifiers changed from: private */
    public EditText nameEditText;
    /* access modifiers changed from: private */
    public Button nextPasswordButton;
    /* access modifiers changed from: private */
    public EditText passwordEditText;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;
    private CheckBox savePasswordCheckBox;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_password);
        Log.d("kkk", "onPasswordActivity:" + getIntent().getStringExtra("email"));
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        this.nextPasswordButton = (Button) findViewById(R.id.nextPasswordButton);
        this.nameEditText = (EditText) findViewById(R.id.nameEditText);
        this.savePasswordCheckBox = (CheckBox) findViewById(R.id.checkSavePassword);
        this.mAuth = FirebaseAuth.getInstance();
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        this.nextPasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PasswordActivity.this.nameEditText.getText().toString().isEmpty()) {
                    PasswordActivity.this.nameEditText.setError("name must not be empty..!");
                    PasswordActivity.this.nameEditText.requestFocus();
                } else if (!ValidationClass.validatePassword(PasswordActivity.this.passwordEditText, PasswordActivity.this.confirmPasswordEditText)) {
                } else {
                    if (PasswordActivity.this.getIntent().getBooleanExtra("isPhone", false)) {
                        PasswordActivity.this.progressLinearLayout.setVisibility(0);
                        PasswordActivity.this.nextPasswordButton.setVisibility(4);
                        String obj = PasswordActivity.this.passwordEditText.getText().toString();
                        String obj2 = PasswordActivity.this.nameEditText.getText().toString();
                        FirebaseAuth.getInstance();
                        return;
                    }
                    PasswordActivity.this.progressLinearLayout.setVisibility(0);
                    PasswordActivity.this.nextPasswordButton.setVisibility(4);
                    String password = PasswordActivity.this.passwordEditText.getText().toString();
                    String name = PasswordActivity.this.nameEditText.getText().toString();
                    String email = PasswordActivity.this.getIntent().getStringExtra("email");
                    Intent intent2 = new Intent(PasswordActivity.this, DateOfBirthActivity.class);
                    intent2.putExtra("name", name);
                    intent2.putExtra("password", password);
                    intent2.putExtra("email", email);
                    PasswordActivity.this.startActivity(intent2);
                }
            }
        });
    }
}
