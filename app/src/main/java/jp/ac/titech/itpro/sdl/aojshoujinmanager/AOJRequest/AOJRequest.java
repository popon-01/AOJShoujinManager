package jp.ac.titech.itpro.sdl.aojshoujinmanager.AOJRequest;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AOJRequest {
    protected HttpURLConnection connection;
    protected AOJXmlParser parser;

    protected void setParser(URL url) throws IOException, XmlPullParserException {
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.connect();
        if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            throw new IOException();
        parser = new AOJXmlParser(connection.getInputStream());
    }

}
