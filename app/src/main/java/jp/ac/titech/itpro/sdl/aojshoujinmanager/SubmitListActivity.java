package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;

public class SubmitListActivity extends AppCompatActivity {
    // Views
    private TextView loadProgressText;
    private View filterView;
    private SubmitFilter filter;
    private EditText editUserID;
    private Spinner languageSpinner;
    private Spinner statusSpinner;
    private CheckBox dateFromCheckBox;
    private CheckBox dateToCheckBox;
    private Calendar dateFrom;
    private Calendar dateTo;
    private Button filterApplyButton;

    private static final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    // for ListView
    private ArrayList<SubmitInfo> allSubmitList;
    private ArrayList<ChallengeInfo> challengeList;
    private ArrayAdapter<ChallengeInfo> listAdapter;

    // for Transition
    public static final String EXTRA_FILTER = "filter";
    public static final String EXTRA_USER_ID = "userID";

    // methods for init
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_list);

        allSubmitList = new ArrayList<>();
        challengeList = new ArrayList<>();
        filter = new SubmitFilter();

        loadProgressText = findViewById(R.id.loadProgressText);
        editUserID = findViewById(R.id.editUserID);
        filterView = findViewById(R.id.filterView);
        filterApplyButton= findViewById(R.id.filterApplyButton);
        languageSpinner = findViewById(R.id.languageSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        dateFromCheckBox = findViewById(R.id.dateFromCheckBox);
        dateToCheckBox = findViewById(R.id.dateToCheckBox);

        loadProgressText.setVisibility(View.GONE);
        filterView.setVisibility(View.GONE);
        initListView();
        initLanguageSpinner();
        initStatusSpinner();
        initDateFrom();
        initDateTo();

    }

    private void initListView() {
        ListView listView = findViewById(R.id.submitProblemList);

        this.listAdapter = new SolvedProblemListAdapter(this,0,
                new ArrayList<ChallengeInfo>());
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        ChallengeInfo challenge = challengeList.get(pos);
                        Intent intent = new Intent(getApplication(), ProblemSubmitActivity.class);
                        intent.putExtra(ProblemSubmitActivity.EXTRA_CHALLENGE, challenge);
                        startActivity(intent);
                    }
                };
        listView.setOnItemClickListener(clickListener);
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
    // end methods for init

    // methods for button event
    public void onClickSearch(View v){
        loadSubmitList();
    }

    public void onClickFilterToggle(View v){
        ToggleButton button = (ToggleButton) v;
        if(button.isChecked()){
            filterView.setVisibility(View.VISIBLE);
        } else {
            filterView.setVisibility(View.GONE);
        }
    }

    public void onClickEditDateFrom(View v){
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateFrom.set(year, month, dayOfMonth);
                        filter.dateFrom = dateFrom.getTimeInMillis();
                        dateFromCheckBox.setText(sdf.format(dateFrom.getTime()));
                        dateFromCheckBox.setChecked(true);
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
                        filter.dateTo = dateTo.getTimeInMillis();
                        dateTo.add(Calendar.DAY_OF_MONTH, -1);
                        dateToCheckBox.setText(sdf.format(dateTo.getTime()));
                        dateToCheckBox.setChecked(true);
                    }
                },
                dateTo.get(Calendar.YEAR),
                dateTo.get(Calendar.MONTH),
                dateTo.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickFilterApply(View v){
        startUpdateView();
    }
    // end methods for button event

    // for methods for Http communication
    private void loadSubmitList() {
        String userID = editUserID.getText().toString().trim();
        if(userID.isEmpty()){
            Toast.makeText(this, "Please input UserID", Toast.LENGTH_LONG).show();
            return;
        }

        allSubmitList.clear();
        challengeList.clear();
        listAdapter.clear();
        filterApplyButton.setEnabled(false);
        loadProgressText.setText(String.format(Locale.US,
                "Loading data : Load %d submits",0));
        loadProgressText.setVisibility(View.VISIBLE);
        new TaskRequestSubmits(this, userID).execute();
    }

    public void updateLoadProgress(int progress){
        loadProgressText.setText(String.format(Locale.US,
                "Loading data : Load %d submits",progress));
    }

    public void onFinishLoad(Collection<SubmitInfo> result){
        filterApplyButton.setEnabled(true);
        loadProgressText.setVisibility(View.GONE);
        allSubmitList.addAll(result);
        startUpdateView();
    }

    private void startUpdateView(){
        challengeList.clear();
        listAdapter.clear();
        new TaskGenChallenge(this, allSubmitList, filter).execute();
    }

    public void updateView(ChallengeInfo challenge) {
        challengeList.add(challenge);
        listAdapter.add(challenge);
        listAdapter.notifyDataSetChanged();
    }
    // end methods for Http communication



    // listeners classes
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

    private class SolvedProblemListAdapter extends ArrayAdapter<ChallengeInfo> {
        SolvedProblemListAdapter
                (@NonNull Context context, int resource, @NonNull List<ChallengeInfo> objects) {
            super(context, resource, objects);
        }

        @Override @NonNull
        public View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(android.R.layout.simple_list_item_1,
                        parent, false);
            }
            ChallengeInfo challenge = getItem(pos);
            if (challenge != null) {
                TextView titleText = view.findViewById(android.R.id.text1);
                titleText.setText(String.format("%s:%s",
                        challenge.problem.problemID, challenge.problem.problemName));
            }
            return view;
        }
    }

}
