package com.example.debanjan.a173050069_hw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SensorActivity extends AppCompatActivity {

    Toolbar myToolbar;
    Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        mySpinner = (Spinner) findViewById(R.id.spinner1);

        final ArrayAdapter myAdapter = new ArrayAdapter<String>(SensorActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sensor_activity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*
                  Toast.makeText(MainActivity.this,
                  mySpinner.getSelectedItem().toString(),
                  Toast.LENGTH_SHORT).show();
                  */

                switch(position){
                    case 1:
                        Intent intent_0 = new Intent(SensorActivity.this, MainActivity.class);
                        startActivity(intent_0);
                        finish();
                        break;
                    case 2:
                        Intent intent_1 = new Intent(SensorActivity.this,RecordActivity.class);
                        startActivity(intent_1);
                        finish();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClickImageButtonSensor(View v){
        Intent intent = new Intent(SensorActivity.this, RecordActivity.class);
        startActivity(intent);
        finish();
    }
}
