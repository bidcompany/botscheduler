package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;

public class SASTaskApprove extends SASTask
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskApprove.class);
        
    // lets set the taskType directly here
    public SASTaskApprove(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "Approval";
    }

    private void approveCampaign()
    {
        String msg = "START OK";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug(msg);
        logger.debug("approving campaign " + campaign + "...");

        // click on List of tabs buttons
        toFind = "//button[@title='List of tabs']";
        msg = "Click on List of Tabs button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // click on Approval menu section
        toFind = "//*[text()='Approval' and ancestor::li[@role='menuitemradio']]/ancestor::li";
        msg = "Click on Approval menu section";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // click on Approve fst Confirm Button
        toFind = "//*[text()='Approve' and ancestor::section]/ancestor::button";
        msg = "Click on Approve button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
        found.click();

        // click again on Approve inside the Confirm dialog
        toFind = "//*[text()='Approve' and ancestor::div[@role='dialog']]/ancestor::button";
        msg = "Click again on Approve button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
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
        openCampaign();
 
        /* we put the code here */
        try
        {
            approveCampaign();
        }
        catch (Exception e)
        {
            logger.error("The bot crashed due to the following exception. " + e.toString());           
            e.printStackTrace();
        }

        closeCampaign();
    }
}