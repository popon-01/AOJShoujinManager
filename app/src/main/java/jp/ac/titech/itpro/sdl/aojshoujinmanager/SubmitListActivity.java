package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;

public class SubmitListActivity extends AppCompatActivity {
    private SubmitFilter filter;
    private String userID;
    private ArrayList<SubmitInfo> allSubmitList;
    private ArrayList<ChallengeInfo> challengeList;
    private ArrayAdapter<ChallengeInfo> listAdapter;
    private TextView loadProgressText;

    public static final String EXTRA_FILTER = "filter";
    public static final String EXTRA_USER_ID = "userID";
    private static final String KEY_SAVED_LIST = "saved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_list);

        Intent intent = getIntent();
        this.filter =
                (SubmitFilter) intent.getExtras().getSerializable(EXTRA_FILTER);
        this.userID = intent.getStringExtra(EXTRA_USER_ID);
        this.allSubmitList = new ArrayList<>();
        this.challengeList = new ArrayList<>();

        loadProgressText = findViewById(R.id.loadProgressText);
        loadProgressText.setVisibility(View.GONE);
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

        loadSubmitList();
    }

    private void loadSubmitList() {
        allSubmitList.clear();
        challengeList.clear();
        listAdapter.clear();
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
