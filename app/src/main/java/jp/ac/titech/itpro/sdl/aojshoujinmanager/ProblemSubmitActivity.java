package jp.ac.titech.itpro.sdl.aojshoujinmanager;

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
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ProblemInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest.ProblemRequest;

public class ProblemSubmitActivity extends AppCompatActivity {

    public static final String EXTRA_CHALLENGE = "challenge";
    private ChallengeInfo challenge;
    private ProblemInfo problem;
    private ArrayList<SubmitInfo> submitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_submit);

        Intent intent = getIntent();
        this.challenge =
                (ChallengeInfo) intent.getExtras().getSerializable(EXTRA_CHALLENGE);
        this.problem = challenge.problem;
        this.submitList = challenge.submits;

        initTextView();
        initListView();
    }

    private void initTextView() {
        TextView problemTitle = findViewById(R.id.problemTitle);
        TextView problemTimeLimit = findViewById(R.id.problemTimeLimit);
        TextView problemMemoryLimit = findViewById(R.id.problemMemoryLimit);
        TextView problemURL = findViewById(R.id.problemURL);

        problemTitle.setText(String.format("%s:%s",
                problem.problemID,problem.problemName));
        problemTimeLimit.setText(String.format(Locale.US, "Time Limit : %d sec",
                problem.timeLimit));
        problemMemoryLimit.setText(String.format(Locale.US, "Memory Limit : %d KB",
                problem.memoryLimit));
        problemURL.setText(String.format(
                "URL : http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=%s",
                problem.problemID));
    }

    private void initListView(){
        ListView listView = findViewById(R.id.submitList);
        ArrayAdapter<SubmitInfo> listAdapter =
                new SubmitListAdapter(this, 0, new ArrayList<SubmitInfo>());

        AdapterView.OnItemClickListener clickListener =
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        SubmitInfo submit = submitList.get(pos);
                        Intent intent = new Intent(getApplication(), SubmitDetailActivity.class);
                        intent.putExtra(SubmitDetailActivity.EXTRA_SUBMIT, submit);
                        intent.putExtra(SubmitDetailActivity.EXTRA_PROBLEM, problem);
                        startActivity(intent);
                    }
                };
        listView.setOnItemClickListener(clickListener);

        listView.setAdapter(listAdapter);
        listAdapter.addAll(submitList);
        listAdapter.notifyDataSetChanged();
    }

    private class SubmitListAdapter extends ArrayAdapter<SubmitInfo> {
        SubmitListAdapter
                (@NonNull Context context, int resource, @NonNull List<SubmitInfo> objects) {
            super(context, resource, objects);
        }

        @Override @NonNull
        public View getView(int pos, @Nullable View view, @NonNull ViewGroup parent) {
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.submit_list_item,
                        parent, false);
            }
            SubmitInfo submit = getItem(pos);
            if (submit != null) {
                TextView submitDate= view.findViewById(R.id.submitDate);
                TextView submitLanguage = view.findViewById(R.id.submitLanguage);
                TextView submitStatus = view.findViewById(R.id.submitStatus);
                submitDate.setText(submit.submissionDateString);
                submitLanguage.setText(submit.language);
                submitStatus.setText(submit.statusShort);
            }
            return view;
        }
    }

}
