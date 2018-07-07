package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.support.annotation.Nullable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.SubmitInfo;
import jp.ac.titech.itpro.sdl.aojshoujinmanager.LoginActivity;

public class SubmitLogRequest extends AOJRequest{

    private String userID;
    private String problemID;
    private int start;
    private int limit;

    public SubmitLogRequest(String userID){
        this.userID = userID;
        this.problemID = null;
        this.start = -1;
        this.limit = -1;
    }

    public SubmitLogRequest setProblemID(String problemID){
        this.problemID = problemID;
        return this;
    }

    public SubmitLogRequest setStart(int start){
        this.start = start;
        return this;
    }

    public SubmitLogRequest setLimit(int limit){
        this.limit = limit;
        return this;
    }

    private SubmitInfo parseStatus() throws IOException, XmlPullParserException {
        SubmitInfo res = new SubmitInfo();

        res.runID = Integer.parseInt(parser.nextTextTag("run_id"));
        res.userID = parser.nextTextTag("user_id");
        res.problemID = parser.nextTextTag("problem_id");
        res.submissionDate =
                new Date(Long.parseLong(parser.nextTextTag("submission_date")));
        parser.nextTextTag("submission_date_str");
        res.status = parser.nextTextTag("status");
        res.language = parser.nextTextTag("language");
        res.cpuTime = Integer.parseInt(parser.nextTextTag("cputime"));
        res.memory = Integer.parseInt(parser.nextTextTag("memory"));
        res.codeSize = Integer.parseInt(parser.nextTextTag("code_size"));

        return res;
    }

    private ArrayList<SubmitInfo> parseStatusList() throws IOException, XmlPullParserException {
        ArrayList<SubmitInfo> res = new ArrayList<>();

        parser.nextStartTag("status_list");
        while (true) {
            parser.nextTag();
            if(parser.isEndTag())
                break;

            parser.require(XmlPullParser.START_TAG, null, "status");
            res.add(parseStatus());
            parser.nextEndTag("status");
        }
        parser.requireEndTag("status_list");
        parser.assertEndDocument();

        return res;
    }

    private URL genURL()
            throws UnsupportedEncodingException, MalformedURLException {
        String urlString = "http://judge.u-aizu.ac.jp/onlinejudge/webservice/status_log"
                + String.format("?user_id=%s", URLEncoder.encode(userID, "UTF-8"));
        if (problemID != null)
            urlString += String.format("&problem_id=%s",
                    URLEncoder.encode(problemID, "UTF-8"));
        if(start >= 0)
            urlString += String.format("&start=%s", start);
        if(limit > 0)
            urlString += String.format("&limit=%s", limit);
        return new URL(urlString);
    }

    @Nullable
    public ArrayList<SubmitInfo> request() {
        ArrayList<SubmitInfo> res = null;
        Long beginT = System.currentTimeMillis();
        try {
            setParser(genURL());
            res = parseStatusList();
        } catch (Exception e){
            e.printStackTrace();
            res = null;
        } finally {
            if(connection != null)
                connection.disconnect();
        }
        Long endT = System.currentTimeMillis();
        Log.d("SubmitLogRequest", String.format("finish request1 in %s msec", endT-beginT));
        return res;
    }
}
