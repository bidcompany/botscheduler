package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;

public class SASTaskTemplate extends SASTask
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskTemplate.class);
        
    // lets set the taskType directly here
    public SASTaskTemplate(WebDriver webDriver, String config)
    {
        super(webDriver, config);
        taskType = "Template";
    }

    public void exec()
    {
        openCampaign();

        /* we put the code here */
        
        closeCampaign();
    }
}