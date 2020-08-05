package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.util.HashMap; 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        String regex = delimiter + "(.*?)" + delimiter;
        logger.debug("Regex rules to extract the rules: " + regex);
        String [] strRules = strSched.split(regex);

        logger.debug("Lets check the rules extracted");
        int i = 1;
        for (String s : strRules)
        {
            logger.debug(i + "] rule: " + s);
            i++;
        }

    }

}