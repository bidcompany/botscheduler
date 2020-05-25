package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        openCampaign();

        /* we put the code here */
        
        closeCampaign();
    }
}