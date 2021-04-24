package com.example.instagramclone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.hbb20.CountryCodePicker;

public class PhoneFragment extends Fragment {
    private CountryCodePicker countryCodePicker;
    private Button nextButton;
    /* access modifiers changed from: private */
    public EditText phoneEditText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        this.phoneEditText = (EditText) view.findViewById(R.id.phoneEditText);
        this.countryCodePicker = (CountryCodePicker) view.findViewById(R.id.ccp);
        Button button = (Button) view.findViewById(R.id.phoneNextButton);
        this.nextButton = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ValidationClass.validatePhone(PhoneFragment.this.phoneEditText)) {
                    Intent intent = new Intent(PhoneFragment.this.getContext(), PasswordActivity.class);
                    intent.putExtra("mobile", PhoneFragment.this.phoneEditText.getText().toString());
                    intent.putExtra("isPhone", true);
                    PhoneFragment.this.startActivity(intent);
                }
            }
        });
        this.phoneEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        this.phoneEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != 1 || event.getRawX() < ((float) (PhoneFragment.this.phoneEditText.getRight() - PhoneFragment.this.phoneEditText.getCompoundDrawables()[2].getBounds().width()))) {
                    return false;
                }
                PhoneFragment.this.phoneEditText.setText("");
                PhoneFragment.this.phoneEditText.requestFocus();
                ((InputMethodManager) PhoneFragment.this.getActivity().getSystemService("input_method")).toggleSoftInput(2, 1);
                return true;
            }
        });
        return view;
    }
}
