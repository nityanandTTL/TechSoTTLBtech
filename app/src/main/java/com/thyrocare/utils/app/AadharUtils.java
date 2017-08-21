package com.thyrocare.utils.app;

import com.thyrocare.models.data.AadharDataModel;
import com.thyrocare.utils.api.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Orion on 12/9/2016.
 */
public class AadharUtils {

    public static AadharDataModel getAadharDataModelFromXML(String xml) {
        xml = fixAadharXMLString(xml);

        XmlPullParserFactory xmlFactoryObject = null;
        AadharDataModel aadharDataModel = null;

        try {
            aadharDataModel = new AadharDataModel();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser aadharparser = xmlFactoryObject.newPullParser();
            aadharparser.setInput(new StringReader(xml));

            int event = aadharparser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = aadharparser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.END_TAG:
                        if (name != null && name.equals("PrintLetterBarcodeData")) {
                            aadharDataModel.setAadharNumber(aadharparser.getAttributeValue(null, "uid"));
                            aadharDataModel.setName(aadharparser.getAttributeValue(null, "name"));

                            String yob = aadharparser.getAttributeValue(null, "yob");

                            Calendar c = Calendar.getInstance();
                            c.set(Integer.parseInt(yob), 1, 1);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            aadharDataModel.setDob(sdf.format(c.getTime()));

                            String gender = aadharparser.getAttributeValue(null, "gender");
                            if (gender.equals("M") || gender.equals("MALE"))
                                gender = "Male";
                            else if (gender.equals("F") || gender.equals("FEMALE"))
                                gender = "Female";

                            aadharDataModel.setGender(gender);
                            aadharDataModel.setHouse(getAttributeValue(aadharparser, "house"));
                            aadharDataModel.setStreet(getAttributeValue(aadharparser, "street"));
                            aadharDataModel.setLm(getAttributeValue(aadharparser, "lm"));
                            aadharDataModel.setPo(getAttributeValue(aadharparser, "po"));
                            aadharDataModel.setDist(getAttributeValue(aadharparser, "dist"));
                            aadharDataModel.setSubdist(getAttributeValue(aadharparser, "subdist"));
                            aadharDataModel.setState(getAttributeValue(aadharparser, "state"));
                            aadharDataModel.setPincode(getAttributeValue(aadharparser, "pc"));
                            aadharDataModel.setVtc(getAttributeValue(aadharparser, "vtc"));
                            aadharDataModel.setLoc(getAttributeValue(aadharparser,"loc"));
                            Logger.debug("xml" + getAttributeValue(aadharparser, "loc"));
                            aadharDataModel.setAddress(getAttributeValue(aadharparser, "house") +" "+
                                    getAttributeValue(aadharparser, "street") +" "+
                                    getAttributeValue(aadharparser, "lm") +" "+
                                    getAttributeValue(aadharparser, "po") +" "+
                                    getAttributeValue(aadharparser, "dist") +" "+
                                    getAttributeValue(aadharparser, "subdist") +" "+
                                    getAttributeValue(aadharparser, "state") +" "+
                                    getAttributeValue(aadharparser, "pc")
                            );
                        }
                        break;
                }
                event = aadharparser.next();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return aadharDataModel;
    }

    private static String getAttributeValue(XmlPullParser aadharParser, String attributeName) {
        String value = aadharParser.getAttributeValue(null, attributeName);
        if (value == null)
            value = "";
        return value;
    }

    private static String fixAadharXMLString(String xml) {
        if (xml.startsWith("&lt;?xml")) {
            int firstDeclarationTagEnd = xml.indexOf("&gt;");
            return xml.substring(firstDeclarationTagEnd + 1);
        }
        return xml;
    }
}