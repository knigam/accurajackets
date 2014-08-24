package com.accurajackets.patientidentification.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.accurajackets.patientidentification.R;
import com.accurajackets.patientidentification.helper.HttpHelper;
import com.accurajackets.patientidentification.model.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    private SearchPatientTask mPatientTask = null;
    private Patient mPatient;

    private EditText id;
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
    private Button cameraButton;
    private Button nfcButton;
    private ListView resultListView;

    private View searchPatientView;
    private View searchPatientStatusView;

    private JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        results = new JSONArray();

        mPatient = new Patient();
        id = (EditText) findViewById(R.id.al_patientid);
        firstName = (EditText) findViewById(R.id.al_firstname);
        lastName = (EditText) findViewById(R.id.al_lastname);
        sex = (Spinner) findViewById(R.id.al_sex);
        dateOfBirth = (EditText) findViewById(R.id.al_dateofbirth);
        height = (EditText) findViewById(R.id.al_height);
        weight = (EditText) findViewById(R.id.al_weight);
        eyeColor = (EditText) findViewById(R.id.al_eyecolor);
        age = (EditText) findViewById(R.id.al_age);
        cellNo = (EditText) findViewById(R.id.al_cellphone);
        homeNo = (EditText) findViewById(R.id.al_homephone);
        driverLicenseNo = (EditText) findViewById(R.id.al_driverlicense);
        email = (EditText) findViewById(R.id.al_email);
        emergencyContactName = (EditText) findViewById(R.id.al_emergencycontactname);
        emergencyContactPhone = (EditText) findViewById(R.id.al_emergencycontactphone);
        address = (EditText) findViewById(R.id.al_address);
        createButton = (Button) findViewById(R.id.al_email_sign_in_button);
        nfcButton = (Button) findViewById(R.id.use_nfc);

        searchPatientView = findViewById(R.id.al_login_form);
        searchPatientStatusView = findViewById(R.id.al_login_progress);

        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, NFCActivity.class);
                startActivity(intent);
            }
        });
        cameraButton = (Button) findViewById(R.id.use_camera_btn);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CameraView.class);
                startActivity(intent);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPatient();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        if (searchPatientView.getVisibility() == View.GONE)
//            searchPatientView.setVisibility(View.VISIBLE);
//        else
//            finish();
//    }

    protected void searchPatient(){
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
            mPatientTask = new SearchPatientTask();
            mPatientTask.execute((Void) null);
        }
    }
    private void showListView(){
        setContentView(R.layout.search_result);
        resultListView = (ListView) findViewById(R.id.al_result_listview);
        List<String> patientNames = new ArrayList<String>();
        final List<Integer> patientIds = new ArrayList<Integer>();

        for(int i = 0; i < results.length(); i++){
            try {
                JSONObject json = results.getJSONObject(i);
                patientIds.add(json.getInt("id"));
                patientNames.add(json.getString("first_name") + " " + json.getString("last_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //check if activity has already finished
        if(this == null){
            return;
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_list_item_1, patientNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        resultListView.setAdapter(dataAdapter);

        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int patientId = patientIds.get(position);
                Intent intent = new Intent(SearchActivity.this, PatientViewActivity.class);
                intent.putExtra("patientId", patientId);
                startActivity(intent);
            }
        });
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

            searchPatientStatusView.setVisibility(View.VISIBLE);
            searchPatientStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            searchPatientStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            searchPatientView.setVisibility(View.VISIBLE);
            searchPatientView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            searchPatientView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            searchPatientStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            searchPatientView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous task used to send the new event info
     * to the backend
     */
    public class SearchPatientTask extends AsyncTask<Void, Void, Boolean> {
        final String URI = getString(R.string.conn) + getString(R.string.patient_search);
        String message = "Error connecting to network";

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject json = new JSONObject();
            try {
                if(id != null && !TextUtils.isEmpty(id.getText())) {
                    json.put("id", Integer.getInteger(id.getText().toString()));
                }
                if(firstName != null && !TextUtils.isEmpty(firstName.getText())) {
                    json.put("first_name", firstName.getText());
                }
                if(lastName != null && !TextUtils.isEmpty(lastName.getText())){
                    json.put("last_name", lastName.getText());
                }
                if(!TextUtils.isEmpty(sex.getSelectedItem().toString())){
                    json.put("sex", sex.getSelectedItem().toString());
                }
                if(dateOfBirth != null && !TextUtils.isEmpty(dateOfBirth.getText())){
                    json.put("dob", dateOfBirth.getText());
                }
                if(height != null && !TextUtils.isEmpty(height.getText())){
                    json.put("height", Integer.getInteger(height.getText().toString()));
                }
                if(weight != null && !TextUtils.isEmpty((weight.getText()))){
                    json.put("weight", Integer.getInteger(weight.getText().toString()));
                }
                if(eyeColor != null && !TextUtils.isEmpty(eyeColor.getText())){
                    json.put("eye_color", eyeColor.getText());
                }
                if(age != null && !TextUtils.isEmpty(age.getText())){
                    json.put("age", Integer.getInteger(age.getText().toString()));
                }
                if(cellNo != null && !TextUtils.isEmpty(cellNo.getText())){
                    json.put("cell_no", Integer.getInteger(cellNo.getText().toString()));
                }
                if(homeNo != null && !TextUtils.isEmpty((homeNo.getText()))){
                    json.put("home_no", Integer.getInteger(homeNo.getText().toString()));
                }
                if(driverLicenseNo != null && !TextUtils.isEmpty(driverLicenseNo.getText())){
                    json.put("driver_license_no", Integer.getInteger((driverLicenseNo.getText().toString())));
                }
                if(email != null && !TextUtils.isEmpty(email.getText())){
                    json.put("email", email.getText());
                }
                if(emergencyContactName != null && !TextUtils.isEmpty(emergencyContactName.getText())){
                    json.put("emergency_contact", emergencyContactName.getText());
                }
                if(emergencyContactPhone != null && !TextUtils.isEmpty((emergencyContactPhone.getText()))){
                    json.put("emergency_contact_no", Integer.getInteger(emergencyContactPhone.getText().toString()));
                }
                if(address != null && !TextUtils.isEmpty((address.getText()))){
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
                results = json.getJSONArray("patients");
                return true;
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
                showListView();
            } else {
                Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mPatientTask = null;
            showProgress(false);
        }
    }
}
