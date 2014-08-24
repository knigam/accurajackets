package com.accurajackets.patientidentification.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.accurajackets.patientidentification.R;
import com.accurajackets.patientidentification.model.Patient;

public class PatientViewActivity extends Activity {

    private int patientId;
    private Patient mPatient;

    private TextView firstName;
    private TextView lastName;
    private TextView sex;
    private TextView dateOfBirth;
    private TextView height;
    private TextView weight;
    private TextView eyeColor;
    private TextView age;
    private TextView cellNo;
    private TextView homeNo;
    private TextView driverLicenseNo;
    private TextView email;
    private TextView emergencyContactName;
    private TextView emergencyContactPhone;
    private Button verifyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        patientId = intent.getIntExtra("patientId", -1);
        mPatient = new Patient();
        mPatient.setId(patientId);

        final String URI = getString(R.string.conn) + getString(R.string.patient_show) + "/" + patientId + ".json";

        new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                mPatient = mPatient.getPatient(URI);
                if(mPatient == null)
                    return false;
                else
                    return true;
            }
            @Override
            protected void onPostExecute(final Boolean success){
                if(success){
                    setContentView(R.layout.patient_view);
                    lastName = (TextView) findViewById(R.id.pv_lastname);
                    age = (TextView) findViewById(R.id.pv_age);
                    sex = (TextView) findViewById(R.id.pv_sex);
                    verifyBtn = (Button) findViewById(R.id.pv_create_button);
                    lastName.setText(lastName.getText() + " " + mPatient.getLastName());
                    age.setText(age.getText() + " " + mPatient.getAge());
                    sex.setText(sex.getText() + " " + mPatient.getSex());

                    verifyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setContentView(R.layout.patient_confidential);
                        }
                    });
                }
                else {
                    if(mPatient == null)
                        Toast.makeText(PatientViewActivity.this, "Can't connect to network", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.execute(null, null, null);
    }

}
