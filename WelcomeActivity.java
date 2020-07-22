/**
 * Author: Shiv Bhushan Tripathi.
 * Date Started: 07/ 02/ 2017.
 * Description: Class that handle welcome and login Screen.
 * @copyright iOne: A company of Ikai.
 */

package com.ione.ione;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WelcomeActivity extends Activity implements
        View.OnClickListener, BeforeRegisterPopUp.Communicator{

    private EditText email = null;
    private EditText password = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            getActionBar().hide();
        } catch (NullPointerException e) {
            Log.d("Shiv", "Null Pointer Exception: " + e);
        }
        setContentView(R.layout.activity_welcome);

        // Getting the respective XML into java objects
        Button registerBttn, logInBttn;
        TextView forgotText;
        registerBttn = (Button) findViewById(R.id.registerButton);
        logInBttn = (Button) findViewById(R.id.logInButton);
        forgotText = (TextView) findViewById(R.id.forgotPassword);
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

        // Setting onclick listeners to every clickable object.
        registerBttn.setOnClickListener(this);
        logInBttn.setOnClickListener(this);
        forgotText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registerButton) {
            BeforeRegisterPopUp registerDialog = new BeforeRegisterPopUp();
            registerDialog.show(getFragmentManager(), "BeforeRegisterPopUp");
        }
        if(v.getId() == R.id.logInButton) {
            handleLoginIssues(email, password);
        }
        if(v.getId() == R.id.forgotPassword) {
            // Make forgot password UI.
        }
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

    /**
     *
     * @param email_str: Email string entered by user.
     * @function_desciption: Helper function of "handleLoginIssues()"
     *                      which check validity of email string entered
     *                      by user.
     */
    private int checkValidityOfEmail(String email_str) {
        boolean numeric = false;
        boolean alphanumeric = false;
        int numeric_count = 0;
        int alpha_count = 0;
        int dot_count = 0;
        int alpha_position = 0;
        int dot_position = 0;
        int other_char = 0;
        for (int i = 0; i < email_str.length(); i++) {
            char temp = email_str.charAt(i);
            if (((int)temp >= 48) && ((int)temp <= 57)) {
                numeric_count++;
            } else if (temp == '@') {
                alpha_count++;
                alpha_position = i;
            } else if (temp == '.') {
                dot_count++;
                dot_position = i;
            } else if(((int)temp < 97) || ((int)temp > 122)){
                other_char++;
            }

        }

        if ((numeric_count == email_str.length()) && (numeric_count == 10)) {
            numeric = true;
        } else if ((alpha_count == 1) &&
                    (dot_count >= 1) &&
                    (alpha_position >= 2) &&
                    (other_char == 0) &&
                    (checkDotAndAlphaPosition(alpha_position, dot_position
                        , email_str))){
            alphanumeric = true;
        }
        int result = 0;
        if(numeric) {
            result = 1;
        } else if (alphanumeric) {
            result = 2;
        }
        return result;
    }

    /**
     *
     * @param alpha_pos: position of '@' symbol in email
     * @param dot_pos: position of '.' symbol in password
     * @param email_str: email string entered by user.
     * @function_desciption: Helper function of "handleLoginIssues()"
     *                      which check '@' and '.' positions.
     */
    private boolean checkDotAndAlphaPosition (int alpha_pos, int dot_pos, String email_str) {
        boolean result = true;
        for(int i = alpha_pos + 1; i < dot_pos; i++) {
            if (email_str.charAt(i) == '.') {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     *
     * @param email: Contains email view
     * @param password: Contains password view
     * @function_desciption: This handle all login issues when login
     *                      button will be clicked. And retrieve data
     *                      related to the respective user.
     */
    private void handleLoginIssues(EditText email, EditText password) {
        String email_str = email.getText().toString();
        String pass_str = password.getText().toString();
        if((email_str.isEmpty()) && (pass_str.isEmpty())) {
            showDialogMessage("Please enter registered email address/ Mobile number and " +
                    "password.");
        } else if(email_str.isEmpty()) {
            showDialogMessage("Please enter registered email address/ mobile number.");
        } else if(pass_str.isEmpty()) {
            showDialogMessage("Please enter password.");
        } else {
            int result = checkValidityOfEmail(email_str);
            if (result == 0) {
                showDialogMessage("Please enter registered email address/ Mobile Number.");
            } else{
                // Make connection to server and find the relevant data.
                // if result = 1 then mobile number.
                // else then email id.
                if(result == 1) {
                    Log.d("Shiv", "It's phone number");
                } else {
                    Log.d("Shiv", "It's email id");
                }
            }
        }
    }

    /**
     * @function_desciption: Acts as an interface between "BeforeRegisterPopUp"
     *                      dialog and "WelcomeActivity" activity.
     */
    @Override
    public void communication() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
