package com.takayoshi.android.hatenareader.utils;

import android.util.Log;
import android.util.Xml;

import com.takayoshi.android.hatenareader.models.HatenaRss;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * はてなホッテントリRSSパーサー
 * @author takayoshi uchida
 */
public class HatenaRssParser {

    private List<HatenaRss> hotentries = new ArrayList<>();

    public List<HatenaRss> parse(InputStream is) {
        try {
            // XML の解析
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(is, "UTF-8");

            String elementName;
            int eventType = xpp.getEventType();
            boolean isEndChannel = false;
            HatenaRss hotentry = new HatenaRss();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    // 何もしない (ドキュメントの始まり)
                } else if(eventType == XmlPullParser.START_TAG) {
                    // 最初のchannel要素は無視する
                    if (!isEndChannel) { eventType = xpp.next(); continue; }

                    elementName = xpp.getName();
                    if(elementName.equals("title")){
                        // タイトル
                        eventType = xpp.next();
                        hotentry = new HatenaRss();
                        if(eventType == XmlPullParser.TEXT) {
                            Log.d("XmlPullParser", "Title = " + xpp.getText());
                            hotentry.title = xpp.getText();
                        }
                    }
                    if(elementName.equals("link")){
                        // URL
                        eventType = xpp.next();
                        if(eventType == XmlPullParser.TEXT) {
                            Log.d("XmlPullParser","Link = " + xpp.getText());
                            hotentry.link = xpp.getText();
                        }
                    }
                    if(elementName.equals("description")){
                        // 要約
                        eventType = xpp.next();
                        if(eventType == XmlPullParser.TEXT) {
                            Log.d("XmlPullParser","Description = " + xpp.getText());
                            hotentry.description = xpp.getText();
                            hotentries.add(hotentry);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    elementName = xpp.getName();
                    if (elementName.equals("channel")) {
                        isEndChannel = true;
                    }
                }
                eventType = xpp.next();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return hotentries;
    }
}
