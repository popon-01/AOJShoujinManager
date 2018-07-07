package jp.ac.titech.itpro.sdl.aojshoujinmanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ChallengeInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ProblemInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest.ProblemRequest;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest.SubmitLogRequest;

public class TaskRequestChallenge extends AsyncTask<Void, ChallengeInfo, Void> {
    private WeakReference<MainActivity> activityRef;
    private String userID;

    TaskRequestChallenge(MainActivity activity, String userID) {
        activityRef = new WeakReference<>(activity);
        this.userID = userID;
    }

    @Nullable
    private HashMap<String, ArrayList<SubmitInfo>> requestAll(){
        Long beginT = System.currentTimeMillis();

        HashMap<String, ArrayList<SubmitInfo>> challenges = new HashMap<>();
        int start = 0;
        final int STEP = 100;
        while(true){
            ArrayList<SubmitInfo> submitLog = new SubmitLogRequest(userID)
                    .setStart(start).setLimit(STEP).request();
            start += STEP;
            if (submitLog == null)
                return null;
            else if (submitLog.isEmpty())
                break;
            for (SubmitInfo status : submitLog) {
                if (!challenges.containsKey(status.problemID)) {
                    challenges.put(status.problemID, new ArrayList<SubmitInfo>());
                }
                challenges.get(status.problemID).add(status);
            }
        }
        Long endT = System.currentTimeMillis();
        Log.d("SubmitLogRequest", String.format("finish request in %s msec", endT-beginT));
        return challenges;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(userID == null)
            return null;
        HashMap<String, ArrayList<SubmitInfo>> challenges = requestAll();

        for(Map.Entry<String, ArrayList<SubmitInfo>> entry : challenges.entrySet()) {
            String id = entry.getKey();
            ArrayList<SubmitInfo> log = entry.getValue();
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
        MainActivity activity = activityRef.get();
        ChallengeInfo challenge = challenges[0];
        if (activity == null || activity.isFinishing() || challenge == null)
            return;
        activity.updateView(challenge);
    }
}
