package com.example.debanjan.a173050069_hw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    Spinner mySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);

        final ArrayAdapter myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.login_activity));
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
                        Intent intent_0 = new Intent(MainActivity.this, SensorActivity.class);
                        startActivity(intent_0);
                        break;
                    case 2:
                        Intent intent_1 = new Intent(MainActivity.this,RecordActivity.class);
                        startActivity(intent_1);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onClickImageButtonMain(View v){
        Intent intent = new Intent(MainActivity.this, RecordActivity.class);
        startActivity(intent);
    }
}
