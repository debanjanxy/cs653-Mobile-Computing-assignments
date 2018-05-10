package com.example.debanjan.a173050069_hw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;



public class RecordActivity extends AppCompatActivity {

    Toolbar myToolbar;
    Spinner mySpinner;
    String start_time, end_time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Spinner Handling
        myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        mySpinner = (Spinner) findViewById(R.id.spinner2);

        final ArrayAdapter myAdapter = new ArrayAdapter<String>(RecordActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.record_activity));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1:
                        Intent intent_0 = new Intent(RecordActivity.this, MainActivity.class);
                        startActivity(intent_0);
                        finish();
                        break;
                    case 2:
                        Intent intent_1 = new Intent(RecordActivity.this,SensorActivity.class);
                        startActivity(intent_1);
                        finish();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Time stamp show handling
        final TextView showTime4 = (TextView)findViewById(R.id.textView4);
        final TextView showTime8 = (TextView)findViewById(R.id.textView8);
        final TextView showTime7 = (TextView)findViewById(R.id.textView7);
        final TextView showTime6 = (TextView)findViewById(R.id.textView6);
        final TextView showTime5 = (TextView)findViewById(R.id.textView5);
        Switch s = (Switch)findViewById(R.id.switch1);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //new variables for shared preferences and modular rotation to update the current time

            public static final String mypreferences = "mypref";
            public static final String showtime4 = "showTime4";
            public static final String showtime8 = "showTime8";
            public static final String showtime7 = "showTime7";
            public static final String showtime6 = "showTime6";
            public static final String showtime5 = "showTime5";
            SharedPreferences sp = getSharedPreferences(mypreferences, Context.MODE_PRIVATE);

            public void saveSharedPreferences(SharedPreferences sp, String str, String key){
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(key,str);
                editor.commit();
            }

            int i = 0;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Date startTime = Calendar.getInstance().getTime();
                    start_time = startTime.toString();
                }
                else{
                    if(showTime4.getText().toString().equals("TextView")){
                        Date startTime = Calendar.getInstance().getTime();
                        end_time = startTime.toString();
                        showTime4.setText(start_time+"--"+end_time);
                        saveSharedPreferences(sp, start_time+"--"+end_time, showtime4);
                    }
                    else if(showTime8.getText().toString().equals("TextView")){
                        Date startTime = Calendar.getInstance().getTime();
                        end_time = startTime.toString();
                        showTime8.setText(start_time+"--"+end_time);
                        saveSharedPreferences(sp, start_time+"--"+end_time, "showTime8");
                    }
                    else if(showTime7.getText().toString().equals("TextView")){
                        Date startTime = Calendar.getInstance().getTime();
                        end_time = startTime.toString();
                        showTime7.setText(start_time+"--"+end_time);
                        saveSharedPreferences(sp, start_time+"--"+end_time, "showTime7");
                    }
                    else if(showTime6.getText().toString().equals("TextView")){
                        Date startTime = Calendar.getInstance().getTime();
                        end_time = startTime.toString();
                        showTime6.setText(start_time+"--"+end_time);
                        saveSharedPreferences(sp, start_time+"--"+end_time, "showTime6");
                    }
                    else if(showTime5.getText().toString().equals("TextView")){
                        Date startTime = Calendar.getInstance().getTime();
                        end_time = startTime.toString();
                        showTime5.setText(start_time+"--"+end_time);
                        saveSharedPreferences(sp, start_time+"--"+end_time, "showTime5");
                    }
                    //28.1.18 written code. //
                    else{
                        i = i%5;
                        if(i==0){
                            Date startTime = Calendar.getInstance().getTime();
                            end_time = startTime.toString();
                            showTime4.setText(start_time+"--"+end_time);
                            i = i+1;
                            saveSharedPreferences(sp, start_time+"--"+end_time, "showTime4");
                        }
                        else if(i==1){
                            Date startTime = Calendar.getInstance().getTime();
                            end_time = startTime.toString();
                            showTime8.setText(start_time+"--"+end_time);
                            i = i+1;
                            saveSharedPreferences(sp, start_time+"--"+end_time, "showTime8");
                        }
                        else if(i==2){
                            Date startTime = Calendar.getInstance().getTime();
                            end_time = startTime.toString();
                            showTime7.setText(start_time+"--"+end_time);
                            i = i+1;
                            saveSharedPreferences(sp, start_time+"--"+end_time, "showTime7");
                        }
                        else if(i==3){
                            Date startTime = Calendar.getInstance().getTime();
                            end_time = startTime.toString();
                            showTime6.setText(start_time+"--"+end_time);
                            i = i+1;
                            saveSharedPreferences(sp, start_time+"--"+end_time, "showTime6");
                        }
                        else if(i==4){
                            Date startTime = Calendar.getInstance().getTime();
                            end_time = startTime.toString();
                            showTime5.setText(start_time+"--"+end_time);
                            i = i+1;
                            saveSharedPreferences(sp, start_time+"--"+end_time, "showTime5");
                        }

                    }

                }
            }
        });
    }

    public void onClickImageButtonRecord(View v){
        Toast.makeText(this, "You are already in Record Activity", Toast.LENGTH_LONG).show();
    }
}
