package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.JDOMException;


public class XML2String
{
    public static Element toXML(String str) throws 
        JDOMException, 
        IOException, 
        UnsupportedEncodingException
    {
        InputStream stream = new ByteArrayInputStream(str.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document document =  builder.build(stream);
        return document.getRootElement();
    }

    public static String toString(Element elem)
    {
        return new XMLOutputter().outputString(elem);
    }

    public static String toString(Document doc)
    {
        return new XMLOutputter().outputString(doc); 
    }
} 