package com.example.sethu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveConfig(View view){
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        //if(getString(radioGroup.getCheckedRadioButtonId()) == "")
    }

    public void dailyConfig(View view){
        Intent intent = new Intent(MainActivity.this, BasicFetActivity.class);
        startActivity(intent);
    }

    public void advancedConfig(View view){
        Toast.makeText(this, "Coming soon!!!", Toast.LENGTH_SHORT).show();
    }

    public void customConfig(View view){
        Intent intent = new Intent(MainActivity.this, CustomFetActivity.class);
        startActivity(intent);
    }
}
