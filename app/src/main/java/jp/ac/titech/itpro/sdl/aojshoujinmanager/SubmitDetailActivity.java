package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ProblemInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;

public class SubmitDetailActivity extends AppCompatActivity {

    public static final String EXTRA_SUBMIT = "submit";
    public static final String EXTRA_PROBLEM = "problem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_detail);

        Intent intent = getIntent();
        SubmitInfo submit =
                (SubmitInfo) intent.getExtras().getSerializable(EXTRA_SUBMIT);
        ProblemInfo problem =
                (ProblemInfo) intent.getExtras().getSerializable(EXTRA_PROBLEM);

        TextView submitTitle = findViewById(R.id.submitTitle);
        TextView submitProblem = findViewById(R.id.submitProblem);
        TextView submitDate = findViewById(R.id.submitDate);
        TextView submitLanguage = findViewById(R.id.submitLanguage);
        TextView submitStatus = findViewById(R.id.submitStatus);
        TextView submitCPUTime = findViewById(R.id.submitCPUTime);
        TextView submitMemory = findViewById(R.id.submitMemory);
        TextView submitCodeSize = findViewById(R.id.submitCodeSize);
        TextView submitDetailURL = findViewById(R.id.submitDetailURL);
        TextView submitProblemURL = findViewById(R.id.submitURL);

        submitTitle.setText(String.format("Submission #%s", submit.runID));
        submitProblem.setText(problem.problemName);
        submitDate.setText(submit.submissionDateString);
        submitLanguage.setText(submit.language);
        submitStatus.setText(submit.status);
        submitCPUTime.setText(String.format(Locale.US, "%d.%02d sec",
                submit.cpuTime/100, submit.cpuTime % 100));
        submitMemory.setText(String.format(Locale.US,"%d KB", submit.memory));
        submitCodeSize.setText(String.format(Locale.US, "%d bytes", submit.codeSize));
        submitDetailURL.setText(String.format(
                "http://judge.u-aizu.ac.jp/onlinejudge/review.jsp?rid=%s",
                submit.runID));
        submitProblemURL.setText(String.format(
                "http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=%s",
                problem.problemID));
    }
}
