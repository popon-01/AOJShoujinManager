package jp.ac.titech.itpro.sdl.aojshoujinmanager;

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

public class TaskRequestSubmits extends AsyncTask<Void, Integer, ArrayList<SubmitInfo>> {
    private WeakReference<SubmitListActivity> activityRef;
    private String userID;

    TaskRequestSubmits(SubmitListActivity activity, String userID) {
        activityRef = new WeakReference<>(activity);
        this.userID = userID;
    }

    @Override
    protected ArrayList<SubmitInfo> doInBackground(Void... voids) {
        Long beginT = System.currentTimeMillis();

        ArrayList<SubmitInfo> submitList = new ArrayList<>();
        int start = 0;
        final int STEP = 100;
        while(true){
            ArrayList<SubmitInfo> response = new SubmitLogRequest(userID)
                    .setStart(start).setLimit(STEP).request();
            if (response == null)
                return null;
            else if (response.isEmpty())
                break;
            start += response.size();
            publishProgress(start);
            submitList.addAll(response);
        }
        Long endT = System.currentTimeMillis();
        Log.d("SubmitLogRequest", String.format("finish request in %s msec", endT-beginT));

        return submitList;
    }

    @Override
    protected void onProgressUpdate(Integer... args){
        SubmitListActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing())
            return;
        int progress = args[0];
        activity.updateLoadProgress(progress);
    }

    @Override
    protected void onPostExecute(ArrayList<SubmitInfo> res){
        SubmitListActivity activity = activityRef.get();
        if (activity == null || activity.isFinishing())
            return;
        activity.onFinishLoad(res);
    }
}
