package com.ione.ione;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener, BirthDatePickerPopUp.Communicator,
        TextWatcher{

    private TextView strength_info, strength;
    private EditText birth_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        // Initialization of objects.
        EditText password = (EditText) findViewById(R.id.password_field);
        ImageView calender_symbol = (ImageView) findViewById(R.id.calender_symbol);
        birth_date = (EditText) findViewById(R.id.date_of_birth);
        strength_info = (TextView) findViewById(R.id.strength_text);
        strength = (TextView) findViewById(R.id.strength);

        // Setting click listener
        calender_symbol.setOnClickListener(this);
        password.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.calender_symbol) {
            BirthDatePickerPopUp datePicker = new BirthDatePickerPopUp();
            datePicker.show(getFragmentManager(), "BirthDate");
        }
    }

    @Override
    public void communication(int year, int month, int dayOfMonth) {
        StringBuilder birthDate = new StringBuilder().append(dayOfMonth).append("/ ")
                .append(month + 1).append("/ ").append(year);
        birth_date.setText(birthDate.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try{
            String password = editable.toString();
            int pass_length = password.length();
            if (pass_length < 6) {
                String message = "Password must be at least 6 character long.";
                showWarningPasswordStatus(strength_info, message);
            } else if (pass_length >= 6){
                boolean contains_digit = false;
                for (int i = 0; i < pass_length; i++) {
                    if ((((int)password.charAt(i)) >= 48) && (((int)password.charAt(i)) <= 57)) {
                        contains_digit = true;
                        break;
                    }

                }
                if (!contains_digit) {
                    String message = "Password must contain at least one number";
                    showWarningPasswordStatus(strength_info, message);

                } else {
                    strength_info.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {}
    }

    /**
     *
     * @param message: String that contains message to be displayed
     *               in message dialog.
     *@function_desciption: Helper function of "handleLoginIssues()"
     *                     to create "EmailIssuesPopUp".
     */
    private void showDialogMessage (String message) {
        EmailIssuesPopUp msg_dialog = new EmailIssuesPopUp();
        Bundle bundle = new Bundle();
        bundle.putString("Message", message);
        msg_dialog.setArguments(bundle);
        msg_dialog.show(getFragmentManager(), "Message");
    }

    private void showWarningPasswordStatus(TextView strength_info, String message) {
        strength_info.setVisibility(View.VISIBLE);
        strength_info.setTextColor(Color.RED);
        strength_info.setText(message);
    }
}
