package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.*;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.*;

// singleton factory class to fetch the task to run
public class SASTaskFactory
{   
    static SASTaskFactory instance;
    static Logger logger = LogManager.getLogger(SASTaskFactory.class);
    
    // private constructor of the singleton
    private SASTaskFactory(){};

    // use this to get the factory reference
    public static SASTaskFactory getFactory()
    {
        if (instance == null)
        {
            logger.debug("Create the Task factory");
            instance = new SASTaskFactory(); 
            return instance;
        }
        else
        {
            return instance;
        }
    }

    // this should fetch the task from input data
    public SASTask fetchTask(CampaignNavigator campaignNavigator, String taskType, String config )  /* simple string or a xml config row of the job ? */
    {
        switch(taskType)
        {
            case("Approval"):
            {
                logger.debug("Create an 'Approval' task");
                return new SASTaskApprove(campaignNavigator, config);
            }
            default:
            {
                logger.error("Impossible to fetch the input taskType: " + taskType);
                return null;
            }
        }
    }
}