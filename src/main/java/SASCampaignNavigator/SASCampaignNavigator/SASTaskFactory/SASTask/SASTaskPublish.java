package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class SASTaskPublish extends SASTask
{
    // logger
    private Logger logger = LogManager.getLogger(SASTaskPublish.class);
        
    // lets set the taskType directly here
    public SASTaskPublish(CampaignNavigator campaignNavigator, String config)
    {
        super(campaignNavigator, config);
        taskType = "Publish";
    }

    protected void skipDialogs()
    {
        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug("publish campaign " + campaign + "...");

        // wait to appear the close campaign button
        toFind = "//button[@title='View all items' and not(contains(@class, 'sapMBtnDisabled'))]";
        msg = "Wait that the view all items is not disabled";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
    
        // skip the crash alertdialog: "Open Campaign in Edit Mode"
        toFind = "//*[text()='Yes']/ancestor::button[ancestor::div[@role='alertdialog']]";
        msg = "Check if there is the Edit Mode alert dialog";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(campaignNavigator.webDriver.findElements(By.xpath(toFind)).size() != 0)
        {   
            // catch the button
            campaignNavigator.webDriver.findElement(By.xpath(toFind)).click();
            logger.warn("Campaign " + campaign + " has shown the Edit Mode alert dialog");
        }

        // Wait to appear the list of tabs buttons
        toFind = "//button[@title='List of tabs']";
        msg = "Wait the List of Tabs button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  

        // skip any generic alert dialog: Already Approved Campaign, Optimization applied ...
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

        // end
        return;
    }

    public void exec()
    {
        try
        {
            // open the campaign
            openCampaign();

            /* here we don't do anything */
            skipDialogs();

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