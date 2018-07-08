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

public class ProblemSubmitActivity extends AppCompatActivity {

    public static final String EXTRA_CHALLENGE = "challenge";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_submit);

        Intent intent = getIntent();
        ChallengeInfo challenge =
                (ChallengeInfo) intent.getExtras().getSerializable(EXTRA_CHALLENGE);
        ProblemInfo problem = challenge.problem;

        TextView problemTitle = findViewById(R.id.problemTitle);
        TextView problemTimeLimit = findViewById(R.id.problemTimeLimit);
        TextView problemMemoryLimit = findViewById(R.id.problemMemoryLimit);
        TextView problemURL = findViewById(R.id.problemURL);

        problemTitle.setText(String.format("%s:%s",
                        problem.problemID,problem.problemName));
        problemTimeLimit.setText(String.format("Time Limit : %s sec", problem.timeLimit));
        problemMemoryLimit.setText(String.format("Memory Limit : %s KB", problem.memoryLimit));
        problemURL.setText(String.format(
                "URL : http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=%s",
                problem.problemID));

        ListView listView = findViewById(R.id.submitList);
        ArrayAdapter<SubmitInfo> listAdapter =
                new SubmitListAdapter(this, 0, new ArrayList<SubmitInfo>());
        listView.setAdapter(listAdapter);
        listAdapter.addAll(challenge.submits);
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

                SimpleDateFormat sdf =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                submitDate.setText(sdf.format(submit.submissionDate));
                submitLanguage.setText(submit.language);
                submitStatus.setText(submit.statusShort);
            }
            return view;
        }
    }

}
