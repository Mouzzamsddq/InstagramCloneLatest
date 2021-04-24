package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class EmailFragment extends Fragment {
    /* access modifiers changed from: private */
    public EditText emailEditText;
    private Button nextButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        this.emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        this.nextButton = (Button) view.findViewById(R.id.emailNextButton);
        this.emailEditText.requestFocus();
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ValidationClass.validateEmail(EmailFragment.this.emailEditText)) {
                    String email = EmailFragment.this.emailEditText.getText().toString();
                    Intent intent = new Intent(EmailFragment.this.getContext(), PasswordActivity.class);
                    intent.putExtra("email", email);
                    EmailFragment.this.startActivity(intent);
                }
            }
        });
        return view;
    }
}
