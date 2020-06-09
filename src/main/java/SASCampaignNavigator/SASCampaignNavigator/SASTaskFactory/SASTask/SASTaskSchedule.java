package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASHistory;

public class SASTaskSchedule extends SASTaskApprove
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskSchedule.class);
        
    // lets set the taskType directly here
    public SASTaskSchedule(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "Schedule";
    }

    protected void scheduleCampaign()
    {
        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug("scheduling campaign " + campaign + "...");

        // click on List of tabs buttons
        toFind = "//button[@title='List of tabs']";
        msg = "Click on List of Tabs button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));         
        found.click();

        // click on Execution menu section
        toFind = "//*[text()='Execution' and ancestor::li[@role='menuitemradio']]/ancestor::li";
        msg = "Click on Execution menu section";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // wait until the page has loaded the schedule settings otherwise clicking on send schedule does nothing 
        toFind = "//*[text()='" + campaign + "'  and ancestor::ul[@role='listbox']]";
        msg = "Waiting that the schedule settings are loaded";
        logger.debug(msg);
        logger.debug("xpath:] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));

        // click on send Schedule button
        toFind = "//*[text()='Send Schedule']/ancestor::button";
        msg = "Click on Send Schedule button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // click on radio box to send schedule to program
        //toFind = "//div[@role='radio' and descendant::*[contains(text(), 'scheduling software')]]";
        toFind = "//div[@role='radio' and descendant::*[contains(text(), 'administrator')]]";
        msg = "Click on Send to Scheduling Software button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // update history if send to admin
        campaignNavigator.history.updateHistory(SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN, true);

        // click again to send Schedule button
        toFind = "//*[text()='Send' and ancestor::div[@role='dialog']]/ancestor::button";
        msg = "Confirm and click on Send Schedule button again";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // wait untill busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait untill the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));
    }

    public void exec()
    {
        try
        {
            // open the campaign
            openCampaign();

            /* we put the code here */
            
            // approve campaign
            //approveCampaign();

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