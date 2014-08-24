package com.accurajackets.patientidentification.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.accurajackets.patientidentification.R;
import com.accurajackets.patientidentification.helper.HttpHelper;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        HttpHelper.getInstance().initialize(getApplicationContext()); //initialize singalton httphelper object
        Button signUpBtn = (Button) findViewById(R.id.wp_sign_up_button);
        Button searchBtn = (Button) findViewById(R.id.wp_search_button);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
