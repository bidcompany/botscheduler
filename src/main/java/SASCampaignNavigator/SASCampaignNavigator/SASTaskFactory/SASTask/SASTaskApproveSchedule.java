package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;

public class SASTaskApproveSchedule extends SASTaskSchedule
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskApproveSchedule.class);
        
    // lets set the taskType directly here
    public SASTaskApproveSchedule(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "ApprovalSchedule";
    }

    public void exec()
    {
        try
        {
             // open the campaign
             openCampaign();

             /* here we don't do anything */
             skipDialogs();
 
             // edit the schedule of campaign 
             editSchedule();
 
             // approve campaign
             approveCampaign();
 
             // schedule campaign
             scheduleCampaign();
 
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