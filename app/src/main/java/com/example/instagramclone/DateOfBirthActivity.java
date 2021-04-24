package com.example.instagramclone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.appevents.AppEventsConstants;
import java.util.Calendar;
import java.util.regex.Pattern;

public class DateOfBirthActivity extends AppCompatActivity {
    private static final Pattern Date_Pattern = Pattern.compile("^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$");
    private LinearLayout alreadyLinearLayout;
    int cday;
    int cmonth;
    Calendar currentDate;
    int cyear;
    String date;
    /* access modifiers changed from: private */
    public EditText dateEditText;
    /* access modifiers changed from: private */
    public Button nextButton;
    /* access modifiers changed from: private */
    public LinearLayout progressLinearLayout;
    String resultMessage;
    Integer userAge;
    private TextView whyTextView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_date_of_birth);
        this.whyTextView = (TextView) findViewById(R.id.whyTextView);
        this.dateEditText = (EditText) findViewById(R.id.dateEditText);
        this.nextButton = (Button) findViewById(R.id.dateNextButton);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.alreadyLinearLayout);
        this.alreadyLinearLayout = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DateOfBirthActivity.this.getApplicationContext(), LoginActivity.class);
                intent.addFlags(268468224);
                DateOfBirthActivity.this.startActivity(intent);
            }
        });
        this.progressLinearLayout = (LinearLayout) findViewById(R.id.progressLinearLayout);
        this.nextButton.setVisibility(0);
        this.progressLinearLayout.setVisibility(8);
        this.nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateOfBirthActivity dateOfBirthActivity = DateOfBirthActivity.this;
                dateOfBirthActivity.date = dateOfBirthActivity.dateEditText.getText().toString().trim();
                if (DateOfBirthActivity.this.validateDate()) {
                    DateOfBirthActivity.this.progressLinearLayout.setVisibility(0);
                    DateOfBirthActivity.this.nextButton.setVisibility(4);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = DateOfBirthActivity.this.getIntent();
                            String name = intent.getStringExtra("name");
                            String password = intent.getStringExtra("password");
                            String email = intent.getStringExtra("email");
                            String date = DateOfBirthActivity.this.dateEditText.getText().toString();
                            Intent intent1 = new Intent(DateOfBirthActivity.this, GenderActivity.class);
                            intent1.putExtra("name", name);
                            intent1.putExtra("password", password);
                            intent1.putExtra("email", email);
                            intent1.putExtra("date", date);
                            DateOfBirthActivity.this.startActivity(intent1);
                        }
                    }, 2000);
                }
            }
        });
        this.whyTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DateOfBirthActivity.this, BirthdayInfoActivity.class);
                intent.addFlags(65536);
                DateOfBirthActivity.this.startActivity(intent);
            }
        });
        this.dateEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != 1 || event.getRawX() < ((float) (DateOfBirthActivity.this.dateEditText.getRight() - DateOfBirthActivity.this.dateEditText.getCompoundDrawables()[2].getBounds().width()))) {
                    return false;
                }
                new DatePickerDialog(DateOfBirthActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int gyear, int gmonth, int gday) {
                        int gmonth2 = gmonth + 1;
                        if (gday < 10 || gmonth2 < 10) {
                            String gdays = String.valueOf(gday);
                            String gmonths = String.valueOf(gmonth2);
                            if (gday < 10) {
                                gdays = AppEventsConstants.EVENT_PARAM_VALUE_NO + gdays;
                            }
                            if (gmonth2 < 10) {
                                gmonths = AppEventsConstants.EVENT_PARAM_VALUE_NO + gmonths;
                            }
                            DateOfBirthActivity.this.resultMessage = gdays + "/" + gmonths + "/" + gyear;
                            DateOfBirthActivity.this.dateEditText.setText(DateOfBirthActivity.this.resultMessage);
                        } else {
                            DateOfBirthActivity.this.dateEditText.setText(gday + "/" + gmonth2 + "/" + gyear);
                        }
                        if (gyear != 0 && gmonth2 != 0 && gday != 0 && DateOfBirthActivity.this.cyear > gyear) {
                            DateOfBirthActivity.this.userAge = Integer.valueOf(DateOfBirthActivity.this.cyear - gyear);
                        }
                    }
                }, DateOfBirthActivity.this.cyear, DateOfBirthActivity.this.cmonth - 1, DateOfBirthActivity.this.cday).show();
                return true;
            }
        });
        Calendar instance = Calendar.getInstance();
        this.currentDate = instance;
        this.cday = instance.get(5);
        this.cmonth = this.currentDate.get(2) + 1;
        this.cyear = this.currentDate.get(1);
    }

    public boolean validateDate() {
        String dateInput = this.dateEditText.getText().toString().trim();
        if (!this.date.isEmpty() && Date_Pattern.matcher(dateInput).matches()) {
            Integer valueOf = Integer.valueOf(this.cyear - Integer.parseInt(this.date.split("/")[2]));
            this.userAge = valueOf;
            Log.i("UserAge", String.valueOf(valueOf));
        }
        if (dateInput.isEmpty()) {
            this.dateEditText.setError("Field can't be empty");
            return false;
        } else if (!Date_Pattern.matcher(dateInput).matches()) {
            this.dateEditText.setError("Date must be in this pattern : dd/mm/yyyy");
            return false;
        } else if (this.userAge.intValue() < 12) {
            this.dateEditText.setError("User must be above 12!");
            return false;
        } else {
            this.dateEditText.setError((CharSequence) null);
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.progressLinearLayout.setVisibility(8);
        this.nextButton.setVisibility(0);
    }
}
