package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class AOJXmlParser {
    private XmlPullParser xpp;

    public AOJXmlParser(InputStream in) throws XmlPullParserException {
        xpp = Xml.newPullParser();
        xpp.setInput(in, "UTF-8");
    }

    public void nextTag()
            throws IOException, XmlPullParserException {
        xpp.nextTag();
    }

    public void nextStartTag(String name)
            throws IOException, XmlPullParserException {
        xpp.nextTag();
        xpp.require(XmlPullParser.START_TAG, null, name);
    }

    public void nextEndTag(String name)
            throws IOException, XmlPullParserException {
        xpp.nextTag();
        xpp.require(XmlPullParser.END_TAG, null, name);
    }

    public String nextText()
            throws IOException, XmlPullParserException {
        return xpp.nextText().trim();
    }

    public String nextTextTag(String name) throws IOException, XmlPullParserException {
        String res;
        nextStartTag(name);
        res = nextText();
        return res;
    }

    public boolean isStartTag() throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.START_TAG;
    }

    public boolean isEndTag() throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_TAG;
    }

    public void require(int type, String namespace, String name)
            throws IOException, XmlPullParserException {
        xpp.require(type, namespace, name);
    }

    public void requireStartTag(String name)
            throws IOException, XmlPullParserException {
        require(XmlPullParser.START_TAG, null, name);
    }

    public void requireEndTag(String name)
            throws IOException, XmlPullParserException {
        require(XmlPullParser.END_TAG, null, name);
    }

    public void assertEndDocument()
            throws IOException, XmlPullParserException {
        xpp.next();
        xpp.require(XmlPullParser.END_DOCUMENT, null, null);
    }

}