package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AOJRequest {
    protected HttpURLConnection connection;
    protected XmlPullParser xpp;

    protected void setParser(URL url) throws IOException, XmlPullParserException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.connect();
        if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new IOException();
        xpp = Xml.newPullParser();
        xpp.setInput(connection.getInputStream(), "UTF-8");
    }

    private void consume(int eventType, String namespace, String name)
            throws IOException, XmlPullParserException {
        xpp.require(eventType, namespace, name);
        xpp.next();
    }

    protected void consumeStartDoc()
            throws IOException, XmlPullParserException {
        consume(XmlPullParser.START_DOCUMENT,"", "");
    }

    protected void consumeEndDoc()
        throws IOException, XmlPullParserException {
        consume(XmlPullParser.END_DOCUMENT, "", "");
    }

    protected void consumeStartTag(String name)
            throws IOException, XmlPullParserException {
        consume(XmlPullParser.START_TAG, "", name);
    }

    protected void consumeEndTag(String name)
        throws IOException, XmlPullParserException {
        consume(XmlPullParser.END_TAG, "", name);
    }

    protected String consumeTextTag(String name) throws IOException, XmlPullParserException {
        String res;
        consumeStartTag(name);
        res = xpp.getText();
        xpp.next();
        consumeEndTag(name);
        return res;
    }
}
