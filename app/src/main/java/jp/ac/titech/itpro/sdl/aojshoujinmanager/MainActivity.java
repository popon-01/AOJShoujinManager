package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ChallengeInfo> challengeList;
    private ArrayAdapter<ChallengeInfo> listAdapter;
    private TaskRequestChallenge currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.challengeList = new ArrayList<>();

        ListView listView = findViewById(R.id.solvedProblemList);
        this.listAdapter = new SolvedProblemListAdapter(this,0,
                new ArrayList<ChallengeInfo>());
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        ChallengeInfo challenge = challengeList.get(pos);
                        /*
                        Intent intent = new Intent(getApplication(), ImprudentTweetActivity.class);
                        intent.putExtra("phrase", phraseBody);
                        startActivity(intent);
                        */
                    }
                };
        listView.setOnItemClickListener(clickListener);
        updateChallenge();
    }

    private void updateChallenge() {
        if(currentTask != null && currentTask.getStatus() != AsyncTask.Status.FINISHED) {
            currentTask.cancel(true);
        }
        challengeList.clear();
        listAdapter.clear();
        currentTask = new TaskRequestChallenge(this, "Ashurnasirpal");
        currentTask.execute();
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
                titleText.setText(challenge.problem.problemName);
            }
            return view;
        }
    }

}
