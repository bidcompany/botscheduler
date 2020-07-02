package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.commons.io.FileUtils;

import org.json.JSONObject;
import org.json.XML;
import org.json.JSONArray;
import org.json.JSONException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter; 
import org.jdom2.Element;
import org.jdom2.JDOMException;


// Open the campaign.json from metadata server and create the SASCampaignNavigator.cfg
public class Json2Xml 
{
    private Logger logger; // = LogManager.getLogger(Json2Xml.class);

    public void createSASCampaignNavigatorCFG()
    {

        // open the json file

        // get production path \bin
        File jarFile;
        try 
        {
            jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()) ;
        }
        catch (URISyntaxException e)
        {
            logger.warn("failed to get the uri of the jar file. Impossible to access the json file.");
            logger.warn("The SASCampaignNavigator.cfg will not be re-created and it will be used the one already present.");
            return;
        }    

        File libDir = jarFile.getParentFile();
        String jsonPath = libDir.getParentFile().getPath() + "\\bin\\campaigns.json";
        
        // set logger filepath
        System.setProperty("logPath", libDir.getParentFile().getPath());
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();
        logger = LogManager.getLogger(Json2Xml.class);

        // json parser
        logger.debug("look for the campaign json file at: " + jsonPath);
        File jsonFile = new File(jsonPath);
        try  
        {
            String content = FileUtils.readFileToString(jsonFile, "utf-8");
            JSONObject campList = new JSONObject(content);

            // filter, modify adjust the json string
            JSONArray jsonArray = campList.getJSONArray("campList");
            jsonArray.forEach( item -> 
            {
                JSONObject o = (JSONObject) item;
                // o.remove("owner");   keep it
                // o.remove("folder");  keep it
                o.remove("modifiedDt");
                o.remove("campCode");
                o.remove("metaURI");
                // o.remove("name");    keep it
                o.remove("sk");
                o.remove("lastSavedDate");
                o.remove("lastModifiedUser");
                o.remove("desc");

                // add taskType
                if(o.getString("sched").isEmpty())
                {
                    o.put("taskType", "Publish");
                }
                else
                {
                    o.put("taskType", "ApprovalSchedule");
                }

                // change attrib name
                o.put("path", o.getString("folder"));
                o.remove("folder");
            });

            // config of xml
            JSONObject json = new JSONObject(); 
            JSONObject config = new JSONObject();
            JSONObject campaign = new JSONObject();
            campaign.put("campaign", campList.getJSONArray("campList"));
            config.put("timeout", 60);
            config.put("campaignList", campaign);
            json.put("config", config); 

            // parse it in a string
            String jsonStr = json.toString();
            JSONObject jsonParsed = new JSONObject(jsonStr);

            // convert it to xml
            String xml = XML.toString(jsonParsed);
            //logger.debug(xml);

            // add xml of the bot
            try
            {
                Element el = XML2String.toXML(xml);
                //String xmlPretty = new XMLOutputter(Format.getPrettyFormat()).outputString(el);
                //logger.debug(xmlPretty);

                // write in the SASCampaignNavigator.cfg
                String cfgPath = libDir.getParentFile().getPath() + "\\bin\\SASCampaignNavigator.cfg";
                new XMLOutputter(Format.getPrettyFormat()).output(el, new FileWriter(cfgPath)); 
                
                logger.info("created successfully a new SASCampaignNavigator.cfg from the campaign.json");  
                return;
            }
            catch (JDOMException e)
            {
              logger.warn("Error in conversion from json to xml.");
              logger.warn("The SASCampaignNavigator.cfg will not be re-created and it will be used the one already present.");
              return;
            }
            catch (IOException e)
            {
                logger.warn("Error in writing the xml converted from json to config file bin\\SASCampaignNavigator.cfg");
                logger.warn("The SASCampaignNavigator.cfg will not be re-created and it will be used the one already present.");
                return;
            }
        }
        catch (IOException e) 
        {
            // if no json file is found, we skip this operation
            logger.warn("no json file found in path: " + jsonPath);
            logger.warn("The SASCampaignNavigator.cfg will not be re-created and it will be used the one already present.");
            return;
        } 
        
        catch (JSONException e) 
        {
            // if json is broken, we skip this operation
            logger.warn("parsing failed. Check json file is valid at: " + jsonPath);
            logger.warn("The SASCampaignNavigator.cfg will not be re-created and it will be used the one already present.");
            return;
        }
   }
    
}