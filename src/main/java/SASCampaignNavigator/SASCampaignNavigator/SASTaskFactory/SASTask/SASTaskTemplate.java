package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;

public class SASTaskTemplate extends SASTask
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskTemplate.class);
        
    // lets set the taskType directly here
    public SASTaskTemplate(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "Template";
    }

    public void exec()
    {
        try
        {
            // open the campaign
            openCampaign();

            /* we put the code here */
 
            // close the campaign
            closeCampaign();

            // report the task success
            report("SUCCESS");

        }
        catch (Exception e)
        {
            logger.error( "Stop execution of task due to the following exception " + e.toString());           
            e.printStackTrace();

            // report the task failure
             report("FAILED");
           
            campaignNavigator.refresh();
        }
     }
}