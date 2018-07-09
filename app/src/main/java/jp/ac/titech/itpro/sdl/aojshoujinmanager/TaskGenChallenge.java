package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ProblemInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest.ProblemRequest;

public class TaskGenChallenge extends AsyncTask<Void, ChallengeInfo, Void>{
    private WeakReference<SubmitListActivity> activityRef;
    private ArrayList<SubmitInfo> submitList;
    private SubmitFilter filter;

    TaskGenChallenge(SubmitListActivity activity,
                     ArrayList<SubmitInfo> submitList, SubmitFilter filter) {
        activityRef = new WeakReference<>(activity);
        this.submitList = submitList;
        this.filter = filter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(submitList == null)
            return null;

        HashMap<String, ArrayList<SubmitInfo>> challenges = new HashMap<>();
        for (SubmitInfo submit : submitList) {
            if(!filter.isOK(submit))
                continue;
            if (!challenges.containsKey(submit.problemID)) {
                challenges.put(submit.problemID, new ArrayList<SubmitInfo>());
            }
            challenges.get(submit.problemID).add(submit);
        }
        ArrayList<String> problemIDs = new ArrayList<>(challenges.keySet());
        Collections.sort(problemIDs);

        for(String id : problemIDs) {
            ArrayList<SubmitInfo> log = challenges.get(id);
            Collections.sort(log, new Comparator<SubmitInfo>() {
                @Override
                public int compare(SubmitInfo o1, SubmitInfo o2) {
                    return - (o1.submissionDate.compareTo(o2.submissionDate));
                }
            });
            ProblemInfo problem = new ProblemRequest(id).request();
            if(problem != null){
                ChallengeInfo challenge = new ChallengeInfo();
                challenge.problem = problem;
                challenge.submits = log;
                publishProgress(challenge);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(ChallengeInfo... challenges){
        SubmitListActivity activity = activityRef.get();
        ChallengeInfo challenge = challenges[0];
        if (activity == null || activity.isFinishing() || challenge == null)
            return;
        activity.updateView(challenge);
    }

}
