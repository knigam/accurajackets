package com.accurajackets.patientidentification.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.accurajackets.patientidentification.R;
import com.accurajackets.patientidentification.helper.HttpHelper;
import com.accurajackets.patientidentification.model.Patient;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends Activity {

    private CreatePatientTask mPatientTask = null;
    private Patient mPatient;

    private EditText firstName;
    private EditText lastName;
    private Spinner sex;
    private EditText dateOfBirth;
    private EditText height;
    private EditText weight;
    private EditText eyeColor;
    private EditText age;
    private EditText cellNo;
    private EditText homeNo;
    private EditText driverLicenseNo;
    private EditText email;
    private EditText emergencyContactName;
    private EditText emergencyContactPhone;
    private EditText address;
    private Button createButton;

    private View createPatientView;
    private View createPatientStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mPatient = new Patient();
        firstName = (EditText) findViewById(R.id.su_firstname);
        lastName = (EditText) findViewById(R.id.su_lastname);
        sex = (Spinner) findViewById(R.id.su_sex);
        dateOfBirth = (EditText) findViewById(R.id.su_dateofbirth);
        height = (EditText) findViewById(R.id.su_height);
        weight = (EditText) findViewById(R.id.su_weight);
        eyeColor = (EditText) findViewById(R.id.su_eyecolor);
        age = (EditText) findViewById(R.id.su_age);
        cellNo = (EditText) findViewById(R.id.su_cellphone);
        homeNo = (EditText) findViewById(R.id.su_homephone);
        driverLicenseNo = (EditText) findViewById(R.id.su_driverlicense);
        email = (EditText) findViewById(R.id.su_email);
        emergencyContactName = (EditText) findViewById(R.id.su_emergencycontactname);
        emergencyContactPhone = (EditText) findViewById(R.id.su_emergencycontactphone);
        address = (EditText) findViewById(R.id.su_address);
        createButton = (Button) findViewById(R.id.su_create_button);

        createPatientView = findViewById(R.id.su_login_form);
        createPatientStatusView = findViewById(R.id.su_login_progress);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPatient();
            }
        });
    }

    protected void createPatient(){
        if (mPatientTask != null) {
            return;
        }
        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the event creation event
            showProgress(true);
            mPatientTask = new CreatePatientTask();
            mPatientTask.execute((Void) null);
        }
    }
    /**
     * Shows the progress UI and hides the new event form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            createPatientStatusView.setVisibility(View.VISIBLE);
            createPatientStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            createPatientStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            createPatientView.setVisibility(View.VISIBLE);
            createPatientView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            createPatientView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            createPatientStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            createPatientView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous task used to send the new event info
     * to the backend
     */
    public class CreatePatientTask extends AsyncTask<Void, Void, Boolean> {
        final String URI = getString(R.string.conn) + getString(R.string.patient_create);
        String message = "Error connecting to network";

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject json = new JSONObject();
            try {
                if(!TextUtils.isEmpty(firstName.getText())) {
                    json.put("first_name", firstName.getText());
                }
                if(!TextUtils.isEmpty(lastName.getText())){
                    json.put("last_name", lastName.getText());
                }
                if(!TextUtils.isEmpty(sex.getSelectedItem().toString())){
                    json.put("sex", sex.getSelectedItem().toString());
                }
                if(!TextUtils.isEmpty(dateOfBirth.getText())){
                    json.put("dob", dateOfBirth.getText());
                }
                if(!TextUtils.isEmpty(height.getText())){
                    json.put("height", Integer.valueOf(height.getText().toString()));
                }
                if(!TextUtils.isEmpty((weight.getText()))){
                    json.put("weight", Integer.valueOf(weight.getText().toString()));
                }
                if(!TextUtils.isEmpty(eyeColor.getText())){
                    json.put("eye_color", eyeColor.getText());
                }
                if(!TextUtils.isEmpty(age.getText())){
                    json.put("age", Integer.valueOf(age.getText().toString()));
                }
                if(!TextUtils.isEmpty(cellNo.getText())){
                    json.put("cell_no", Integer.valueOf(cellNo.getText().toString()));
                }
                if(!TextUtils.isEmpty((homeNo.getText()))){
                    json.put("home_no", Integer.valueOf(homeNo.getText().toString()));
                }
                if(!TextUtils.isEmpty(driverLicenseNo.getText())){
                    json.put("driver_license_no", Integer.valueOf((driverLicenseNo.getText().toString())));
                }
                if(!TextUtils.isEmpty(email.getText())){
                    json.put("email", email.getText());
                }
                if(!TextUtils.isEmpty(emergencyContactName.getText())){
                    json.put("emergency_contact", emergencyContactName.getText());
                }
                if(!TextUtils.isEmpty((emergencyContactPhone.getText()))){
                    json.put("emergency_contact_no", Integer.valueOf(emergencyContactPhone.getText().toString()).intValue());
                }
                if(!TextUtils.isEmpty((address.getText()))){
                    json.put("address", address.getText());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

            try {
                json = HttpHelper.httpPost(URI, json);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            try {
                if (json.getBoolean("success")) {
                    mPatient.setId(json.getInt("patient_id"));
                    return true;
                } else {
                    message = json.getString("message");
                    return false;
                }
            }  catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPatientTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(SignupActivity.this, PatientViewActivity.class);
                intent.putExtra("patientId", mPatient.getId());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mPatientTask = null;
            showProgress(false);
        }
    }
}
