package com.jumarmartin.powerreport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static com.jumarmartin.powerreport.R.id.school_spinner;


public class ReportActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_report);
        super.onCreate(savedInstanceState);
        addSchoolsToSpinner();
    }

    private void addSchoolsToSpinner() {
        Spinner spinner = (Spinner) findViewById(school_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.school_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }



}


