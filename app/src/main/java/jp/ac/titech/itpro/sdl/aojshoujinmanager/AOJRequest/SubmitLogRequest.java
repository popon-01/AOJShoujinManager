package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;

public class SubmitLogRequest extends AOJRequest{

    private String userID;
    private String problemID;

    public SubmitLogRequest(String userID){
        this(userID, null);
    }

    public SubmitLogRequest(String userID, String problemID) {
        this.userID = userID;
        this.problemID = problemID;
    }

    private SubmitInfo parseStatus() throws IOException, XmlPullParserException {
        SubmitInfo res = new SubmitInfo();

        consumeStartTag("status");
        res.runID = Integer.parseInt(consumeTextTag("run_id"));
        res.userID = consumeTextTag("user_id");
        res.problemID = consumeTextTag("problem_id");
        res.submissionDate =
                new Date(Long.parseLong(consumeTextTag("submission_date")));
        res.status = consumeTextTag("status");
        res.cpuTime = Integer.parseInt(consumeTextTag("cpu_time"));
        res.memory = Integer.parseInt(consumeTextTag("memory"));
        res.codeSize = Integer.parseInt(consumeTextTag("code_size"));
        consumeEndTag("status");

        return res;
    }

    private ArrayList<SubmitInfo> parseStatusList() throws IOException, XmlPullParserException {
        ArrayList<SubmitInfo> res = new ArrayList<>();

        consumeStartDoc();
        consumeStartTag("status_list");
        while (xpp.getEventType() == XmlPullParser.START_TAG &&
                xpp.getName().equals("status")) {
            res.add(parseStatus());
        }
        consumeEndTag("status_list");
        consumeEndDoc();

        return res;
    }

    @Nullable
    private ArrayList<SubmitInfo> request1(int pos, int size) {
        ArrayList<SubmitInfo> res = null;
        try {
            String urlString = "http://judge.u-aizu.ac.jp/onlinejudge/webservice/status_log"
                    + String.format("?user_id=%s", URLEncoder.encode(userID, "UTF-8"))
                    + String.format("&start=%s", pos)
                    + String.format("&limit=%s", size);
            if (problemID != null)
                urlString += String.format("&problem_id=%s",
                        URLEncoder.encode(problemID, "UTF-8"));

            setParser(new URL(urlString));
            res = parseStatusList();
        } catch (Exception e){
            e.printStackTrace();
            res = null;
        } finally {
            if(connection != null)
                connection.disconnect();
        }
        return res;
    }

    @Nullable
    public HashMap<String, ArrayList<SubmitInfo>> request() {
        return request(0,-1);
    }

    @Nullable
    public  HashMap<String, ArrayList<SubmitInfo>> request(int size) {
        return request(0, size);
    }

    // negative size means size is not specified
    @Nullable
    public HashMap<String, ArrayList<SubmitInfo>> request(int pos, int size) {
        HashMap<String, ArrayList<SubmitInfo>> res = new HashMap<>();
        int rest = size;
        while (true) {
            int nextSize;
            if (size < 0) {
                nextSize = 100;
            } else {
                nextSize = Math.min(rest, 100);
                if (nextSize == 0) break;
                rest -= nextSize;
            }
            pos += nextSize;

            ArrayList<SubmitInfo> statusList = request1(pos, nextSize);
            if (statusList == null || statusList.isEmpty())
                break;
            for (SubmitInfo status : statusList) {
                if (!res.containsKey(status.problemID)) {
                    res.put(status.problemID, new ArrayList<SubmitInfo>());
                }
                res.get(status.problemID).add(status);
            }
        }
        return res;
    }
}
