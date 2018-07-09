package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SubmitSearchActivity extends AppCompatActivity {

    SubmitFilter filter;
    EditText editUserID;
    Spinner languageSpinner;
    Spinner statusSpinner;
    CheckBox dateFromCheckBox;
    CheckBox dateToCheckBox;
    Calendar dateFrom;
    Calendar dateTo;

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_search);

        filter = new SubmitFilter();
        editUserID = findViewById(R.id.editUserID);
        languageSpinner = findViewById(R.id.languageSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        dateFromCheckBox = findViewById(R.id.dateFromCheckBox);
        dateToCheckBox = findViewById(R.id.dateToCheckBox);

        initLanguageSpinner();
        initStatusSpinner();
        initDateFrom();
        initDateTo();
    }

    private void initLanguageSpinner() {
        ArrayAdapter<CharSequence> languageSpinnerAdapter
                = ArrayAdapter.createFromResource(this,
                R.array.language_spinner_items, android.R.layout.simple_spinner_item);
        languageSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageSpinnerAdapter);
        languageSpinner.setOnItemSelectedListener(new LanguageSpinnerListener());
        filter.language = "ALL";
    }

    private void initStatusSpinner() {
        ArrayAdapter<CharSequence> statusSpinnerAdapter
                = ArrayAdapter.createFromResource(this,
                R.array.status_spinner_items, android.R.layout.simple_spinner_item);
        statusSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);
        statusSpinner.setOnItemSelectedListener(new StatusSpinnerListener());
        filter.status = "ALL";
    }

    private void initDateFrom() {
        dateFrom = Calendar.getInstance(Locale.US);
        filter.dateFrom = null;

        dateFromCheckBox.setChecked(false);
        dateFromCheckBox.setText(sdf.format(dateFrom.getTime()));
        dateFromCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateFromCheckBox.isChecked())
                    filter.dateFrom = dateFrom.getTimeInMillis();
                else
                    filter.dateFrom = null;
            }
        });
    }

    private void initDateTo() {
        dateTo = Calendar.getInstance(Locale.US);
        filter.dateTo = null;

        dateToCheckBox.setChecked(false);
        dateToCheckBox.setText(sdf.format(dateTo.getTime()));
        dateToCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateToCheckBox.isChecked())
                    filter.dateTo = dateTo.getTimeInMillis();
                else
                    filter.dateTo = null;
            }
        });
    }

    public void onClickEditDateFrom(View v){
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateFrom.set(year, month, dayOfMonth);
                        dateFromCheckBox.setText(sdf.format(dateFrom.getTime()));
                        dateFromCheckBox.setChecked(true);
                        filter.dateFrom = dateFrom.getTimeInMillis();
                    }
                },
                dateFrom.get(Calendar.YEAR),
                dateFrom.get(Calendar.MONTH),
                dateFrom.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickEditDateTo(View v){
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateTo.set(year, month, dayOfMonth);
                        dateTo.add(Calendar.DAY_OF_MONTH, 1);
                        dateToCheckBox.setText(sdf.format(dateTo.getTime()));
                        dateToCheckBox.setChecked(true);
                        filter.dateTo = dateTo.getTimeInMillis();
                    }
                },
                dateTo.get(Calendar.YEAR),
                dateTo.get(Calendar.MONTH),
                dateTo.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickSearch(View v){
        String userID = editUserID.getText().toString().trim();
        if(userID.isEmpty()){
            Toast.makeText(this, "Please input UserID", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getApplication(), SubmitListActivity.class);
        intent.putExtra(SubmitListActivity.EXTRA_FILTER, filter);
        intent.putExtra(SubmitListActivity.EXTRA_USER_ID, userID);
        startActivity(intent);
    }

    private class LanguageSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filter.language = (String)parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            filter.language = "ALL";
        }
    }

    private class StatusSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filter.status = (String)parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            filter.status = "ALL";
        }
    }


}
