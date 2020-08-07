package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASHistory;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASSchedRule;

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

    protected void scheduleCampaign(String communication)
    {
        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug("scheduling campaign " + campaign + " communication " + communication + " ...");

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
        toFind = "//*[text()='" + communication + "'  and ancestor::ul[@role='listbox']]";
        msg = "Waiting that the schedule settings are loaded";
        logger.debug(msg);
        logger.debug("xpath:] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));

        // click on listbox
        found.click();

        // click on send Schedule button
        toFind = "//*[text()='Send Schedule']/ancestor::button";
        msg = "Click on Send Schedule button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // click on radio box to send schedule to program
        toFind = "//div[@role='radio' and descendant::*[contains(text(), 'scheduling software')]]";
        //toFind = "//div[@role='radio' and descendant::*[contains(text(), 'administrator')]]";
        msg = "Click on Send to Scheduling Software button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // update history if send to admin
        //campaignNavigator.history.updateHistory(SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN, true);

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
    
        // skip the send to software error
        toFind = "//button[ancestor::div[@role='alertdialog']]";
        msg = "Check if there is an alert dialog";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(campaignNavigator.webDriver.findElements(By.xpath(toFind)).size() != 0)
        {   
            // catch the button
            campaignNavigator.webDriver.findElement(By.xpath(toFind)).click();
            logger.warn("Camapaign " + campaign + " has shown an alert dialog");
            
            // end approval task
            return;
        }

        // wait until busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait until the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));
    
         // wait until blocker is hidden
        toFind = "//div[@id='sap-ui-blocklayer-popup' and contains(@style, 'visibility: hidden')]";
        msg = "Wait until blocklayer is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));      

        toFind = "//*[@title='Please wait']";
        msg = "Wait until the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));

    }

    protected void editSchedule(String communication)
    {
        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug("Edit schedule campaign " + campaign + " communication " + communication +  "...");

        // do not edit the schedule if it is empty
        if(this.campaignSchedule.isEmpty())
        {
            logger.debug("Edit schedule rules of campaign " + campaign + " are empty.");
            logger.debug("Skip edit schedule.");
            return;
        }

        // navigate to execution page and click edit schedule
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
        toFind = "//*[text()='" + communication + "'  and ancestor::ul[@role='listbox']]";
        msg = "Waiting that the schedule settings are loaded";
        logger.debug(msg);
        logger.debug("xpath:] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));

        // click on the listbox
        found.click();

        // check if we need to remove the schedule before edit
        toFind = "//*[text()='Remove Schedule']/ancestor::button";
        msg = "Check if present Remove Schedule button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(campaignNavigator.webDriver.findElements(By.xpath(toFind)).size() > 0)
        {
            msg = "Click on Remove Schedule button";
            logger.debug(msg);
            campaignNavigator.webDriver.findElement(By.xpath(toFind)).click();
            
            if(!campaign.equals(communication))
            {
                // Removing Schedule of Communication opens different dialog
                toFind = "//*[text()='Remove Schedule']/ancestor::button[ancestor::footer]";
                msg = "Click on Remove Schedule button in dialog [Communication Schedule]";
            }
            else
            {
                // Confirm the Remove Schedule dialog
                toFind = "//*[text()='Yes']/ancestor::button[ancestor::div[@role='alertdialog']]";            
                msg = "Click on Yes in the Remove Sent Schedule dialog [Campaign Schedule]";
            }
            
            logger.debug(msg);
            logger.debug("xpath]: " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();
           
            // wait until busy page is invisible, otherwise it will intercept the click
            toFind = "//*[@title='Please wait']";
            msg = "Wait until the busy overlay is invisible";
            logger.debug(msg);
            logger.debug("xpath]: " + toFind);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));
        }

        // click on Edit Schedule
        toFind = "//*[text()='Edit Schedule']/ancestor::button";
        msg = "Click on Edit Schedule button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // wait the modal Set Schedule is shown
        toFind = "//*[text()='OK']/ancestor::button[ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
        msg = "Wait until it is visible the dialog menu";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        
        // Map Schedule settings
        SASSchedRule schedRule = new SASSchedRule();
        schedRule.fetchRule(mapCampaignSchedule.get(communication), this);

        // stop editing
        found.click();

        // wait until disappear dialog set schedule
        msg = "Wait until the dialog menu disappear";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));

        // save the schedule settings
        toFind = "//button[@title='Save' and ancestor::div[contains(@id, 'jsview')]]";
        msg = "Click on Options menu button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // wait until busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait until the busy overlay is invisible";
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

            /* here we don't do anything */
            skipDialogs();

            // schedule each communication
            for (String communication : this.mapCampaignSchedule.keySet())
            {
                // edit the schedule of campaign 
                editSchedule(communication);

            }
            
            // approve campaigns
            //approveCampaign();

            // schedule campaign
            scheduleCampaign(mapCampaignSchedule.keySet().iterator().next());
            
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