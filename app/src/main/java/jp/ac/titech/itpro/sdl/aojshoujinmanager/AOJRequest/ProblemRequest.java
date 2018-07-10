package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.support.annotation.Nullable;
import android.util.Log;

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

        parser.nextStartTag("problem");

        parser.nextTag();
        if(parser.isEndTag()) {
            parser.require(XmlPullParser.END_TAG, null, "problem");
            parser.assertEndDocument();
            return null;
        }

        parser.requireStartTag("id");
        res.problemID= parser.nextText();

        res.problemName = parser.nextTextTag("name");
        parser.nextTextTag("available");
        res.timeLimit = Integer.parseInt(parser.nextTextTag("problemtimelimit"));
        res.memoryLimit = Integer.parseInt(parser.nextTextTag("problemmemorylimit"));

        parser.nextStartTag("status");
        parseStatus();
        parser.nextEndTag("status");

        parser.nextEndTag("problem");
        parser.assertEndDocument();
        return res;
    }

    void parseStatus() throws IOException, XmlPullParserException {
        parser.nextTextTag("submission");
        parser.nextTextTag("accepted");
        parser.nextTextTag("wronganswer");
        parser.nextTextTag("timelimit");
        parser.nextTextTag("memorylimit");
        parser.nextTextTag("outputlimit");
        parser.nextTextTag("runtimeerror");
    }

    @Nullable
    public ProblemInfo request() {
        ProblemInfo res = null;
        try {
            String urlString =
                    "http://judge.u-aizu.ac.jp/onlinejudge/webservice/problem?status=false"
                    + String.format("&id=%s", URLEncoder.encode(problemID, "UTF-8"));
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
