package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;

public class SASTaskApprove extends SASTaskPublish
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskApprove.class);
        
    // lets set the taskType directly here
    public SASTaskApprove(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "Approval";
    }

    protected void approveCampaign()
    {
        /* 
            here it may spawn a dialog will asking to open the campaign in edit mode.
            It happens only if a bot crashed in the previous execution. => the bot will crash

            here it may spawn a dialog saying the campaign is approved and no changes may be applied.
            It happens only if the campaign is in Apporved state. => the bot will crash
        */

        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug("approving campaign " + campaign + "...");

        // click on List of tabs buttons
        toFind = "//button[@title='List of tabs']";
        msg = "Click on List of Tabs button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        
        // check if the already approved button is present 
        toFind = "//button[ancestor::div[@role='alertdialog']//*[contains(text(), 'approved')]]";
        msg = "Check if there is an already approved button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(campaignNavigator.webDriver.findElements(By.xpath(toFind)).size() != 0)
        {   
            // catch the button
            campaignNavigator.webDriver.findElement(By.xpath(toFind)).click();
            logger.warn("Camapaign " + campaign + " is already approved");
            
            // end approval task
            return;
        }
        
        found.click();

        // click on Approval menu section
        toFind = "//*[text()='Approval' and ancestor::li[@role='menuitemradio']]/ancestor::li";
        msg = "Click on Approval menu section";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // wait until busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait untill the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));

        // wait until busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait untill the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));

        // click on Approve fst Confirm Button
        toFind = "//*[text()='Approve' and ancestor::section]/ancestor::button";
        msg = "Click on Approve button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // click again on Approve inside the Confirm dialog
        toFind = "//*[text()='Approve' and ancestor::div[@role='dialog']]/ancestor::button";
        msg = "Click again on Approve button";
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

            // skip the dialogs
            skipDialogs();

            /* we put the code here */
            approveCampaign();

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