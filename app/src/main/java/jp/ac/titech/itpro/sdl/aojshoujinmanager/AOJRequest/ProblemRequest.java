package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.support.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJData.ProblemInfo;

public class ProblemRequest extends AOJRequest {
    private String problemID;

    public ProblemRequest(String problemID) {
        this.problemID = problemID;
    }

    @Nullable
    private ProblemInfo parseProblem() throws IOException, XmlPullParserException {
        ProblemInfo res = new ProblemInfo();

        consumeStartDoc();
        consumeStartTag("problem");

        if(xpp.getEventType() == XmlPullParser.END_TAG &&
                xpp.getName().equals("problem")){
            consumeEndTag("problem");
            consumeEndDoc();
            return null;
        }

        res.problemID= consumeTextTag("id");
        res.problemName = consumeTextTag("name");
        consumeTextTag("available");
        res.memoryLimit = Integer.parseInt(consumeTextTag("problemtimelimit"));
        res.timeLimit = Integer.parseInt(consumeTextTag("problemmemorylimit"));
        parseStatus();

        consumeEndTag("problem");
        consumeEndDoc();
        return res;
    }

    void parseStatus() throws IOException, XmlPullParserException {
        consumeStartTag("status");

        consumeTextTag("submission");
        consumeTextTag("accepted");
        consumeTextTag("wronganswer");
        consumeTextTag("timelimit");
        consumeTextTag("memorylimit");
        consumeTextTag("outputlimit");
        consumeTextTag("runtimeerror");

        consumeEndTag("status");
    }

    @Nullable
    public ProblemInfo request() {
        ProblemInfo res = null;
        try {
            String urlString =
                    "http://judge.u-aizu.ac.jp/onlinejudge/webservice/problem?status=false"
                    + String.format("&user_id=%s", URLEncoder.encode(problemID, "UTF-8"));
            setParser(new URL(urlString));
            res = parseProblem();
        } catch (Exception e) {
            e.printStackTrace();
            res = null;
        } finally {
            if(connection != null)
                connection.disconnect();
        }
        return res;
    }
}
