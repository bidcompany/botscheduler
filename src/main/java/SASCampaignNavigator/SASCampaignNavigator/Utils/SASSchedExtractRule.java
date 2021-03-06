package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.util.HashMap; 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASTranslate;

public class SASSchedExtractRule 
{
    // logger
    private Logger logger = LogManager.getLogger(SASSchedExtractRule.class);

    public void extractRule(HashMap<String, String> campaignSched, String strSched)
    {
        // name of campaign used as delimiter between communication
        String delimiter = strSched.split("<br/>")[0] + "<br/>";
        logger.debug("Keyword used as delimiter to parse communication sched settings: " + delimiter);

       // split with regex
        String [] strRules = strSched.split(delimiter);

        logger.debug("Lets check the rules extracted");
        int i = 1;
        for (String s : strRules)
        {
            if (s.isEmpty())
            {
                continue;
            }

            logger.debug(i + "] rule: " + s);
            i++;

            // save with communication string
            String communication = s.split("<br/>")[1];
            
            // schedule campaign if at position of communication we have the sched rule
            if (communication.contains("Starts"))
            {
                communication = delimiter.replace("<br/>", "");
            }

            String communicationSetting = delimiter + s;

            // translate ita to eng
            logger.debug("Original rule:] " + communicationSetting);
            communicationSetting = SASTranslate.toEnglish(communicationSetting);
            logger.debug("Translated rule:] " + communicationSetting);
            
            campaignSched.put(communication, communicationSetting);
            logger.debug("Save for communication [" + communication + "] the schedule setting [" +communicationSetting + "]");
        }       
    }

}