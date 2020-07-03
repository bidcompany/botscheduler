package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import java.util.regex.Pattern;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.XML2String;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASHistory;

// Abstract class of a SASTask.
// the bot will do for all task the following operation: openCampaign, runTask, closeCampaign 
public abstract class SASTask
{
    private Logger logger = LogManager.getLogger(SASTask.class);
    
    // data fetched from config file
    protected String campaign;
    protected String taskType;
    protected String campaignPath;

    // WebDriver instance from SASCampaignNavigator
    //protected WebDriver webDriver;
    protected CampaignNavigator campaignNavigator; 

    // implemented in childern class
    public abstract void exec();

    // put in the html report info about this sas task 
    public void report(String msg)
    {
        Integer num = Integer.parseInt(System.getProperty("num")) + 1;
        System.setProperty("num", num.toString());
        //System.setProperty("campaign", campaignPath + "\\" + campaign);
        System.setProperty("campaign", campaignPath);
        System.setProperty("taskType", taskType);
            
        switch(msg)
        {
            case "SUCCESS":
            {
                logger.info("</td><td bgcolor='#ABEFD0'>" + msg);
                break;
            }
            case "FAILED":
            {
                logger.info("</td><td bgcolor='#EFABB1'>" + msg);
                break;
            }
            default:
                break;
        }
    }

    public SASTask(CampaignNavigator campaignNavigator, String config)
    {
        // bind webDriver
        this.campaignNavigator = campaignNavigator;

        // parse config string
        try
        {
            // get xml 
            Element root = XML2String.toXML(config);
            this.campaign = root.getChild("name").getValue();
            logger.debug("set target campaign name: " + campaign);

            this.campaignPath = root.getChild("path").getValue();
            logger.debug("set target campaign path: " + campaignPath);

            if (campaign == null)
                throw new NullPointerException("campaign name is null");
            // if (campaignDir == null)
            //     throw new NullPointerException("campaign directory is null");
            // if (campaignCategory == null)
            //     throw new NullPointerException("campaign category is null");
        }
        catch (Exception e)
        {   
            // collect exception coming from 
            //  - the xml2string parsing 
            //  - Null values 
    
            logger.error( "Impossible to create the task due to the following exception " + e.toString());
            e.printStackTrace();
        }
    }
    
    protected void openCampaign()
    {
        String msg = "START OK";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout);

        logger.debug(msg);
        logger.debug("open campaign " + campaign + "...");

        // if it is refreshed we need to focus on the iframe again
        if(campaignNavigator.history.getValue(SASHistory.CAMPAIGN_SECTION_REFRESHED))
        {
            toFind = "sasci_iframe";
            msg = "Switch to iframe";
            logger.debug("This page has been refreshed. we need to focus on the iframe again");
            logger.debug(msg);
            logger.debug("id]: " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            campaignNavigator.webDriver.switchTo().frame(found);

            // reset the flag
            campaignNavigator.history.updateHistory(SASHistory.CAMPAIGN_SECTION_REFRESHED, false);
        }

        // Click Designer Button
        toFind = "//*[text()='Designer' and ancestor::div[@role='tab']]/ancestor::div[@role='tab']";
        msg = "Click on Designer menu button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();
        
        // Wait untill busy page is invisible, otherwise it will intercept the click
        toFind = "//*[@title='Please wait']";
        msg = "Wait untill the busy overlay is invisible";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));  

        // Click Hierarchy view button 
        toFind = "//*[@title='Hierarchy view' and @role='radio']";
        msg = "Click on Hierarchy View button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // Click campaign selector button
        toFind = "__node0";
        msg = "Click on Campaign expand button";
        logger.debug(msg);
        logger.debug("id]: " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
        found.click();
        
        // Expand Campaign folder
        toFind = "//*[@title='Campaigns']/..//*[@role='button']";
        msg = "Expand Campaign directory tree";
        logger.debug(msg);
        logger.debug("xpath] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
        found.click();  // 1st time

        // if campaign section is already open we need to click 2 times. 
        if(campaignNavigator.history.getValue(SASHistory.CAMPAIGN_SECTION_ALREADY_OPEN))
        {
            found.click();  // 2nd time
        }
        
        // filter with the target campaign name so the page is always focused on it
        toFind="__page0-searchField-I";
        msg = "Find the search box to type the name of the campaign";
        logger.debug(msg);
        logger.debug("id] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
        logger.debug("Write to input field " + campaign);
        found.clear();
        found.sendKeys(campaign);
        found.submit();

        // wait until text field has value campaignToApprove
        logger.debug("Wait until the to input field has value " + campaign);
        wait.until(ExpectedConditions.textToBePresentInElementValue(found, campaign));

        // parse the campaign path
        // if it is empty it means it is in the root folder ("Campaigns/name_campaign")
        if(!campaignPath.isEmpty())
        {
            String[] dirs = campaignPath.split(Pattern.quote("\\")); 
            for (int i = 0; i < dirs.length - 1; i++)
            {
                String dir = dirs[i];

                // skip Campaigns. Already did it in the hardcoded code above
                if(dir.equals("Campaigns"))
                    continue;

                // Expand the path folder
                toFind = "//*[@title='"+ dir +"']/..//*[@role='button']";
                msg = "Expand " + dir + " directory tree";
                logger.debug(msg);
                logger.debug("xpath] " + toFind);
                found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
                found.click();                
            }
        }

        // click on Title
        toFind = "//*[@title='"+ campaign +"']";
        msg = "Open the campaign " + campaign + " in the directory tree";
        logger.debug(msg);
        logger.debug("xpath] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
        found.click();
        
        // next task will find the campaign task already opened
        campaignNavigator.history.updateHistory(SASHistory.CAMPAIGN_SECTION_ALREADY_OPEN, true);
    }

    protected void closeCampaign()
    {
        String msg = "close campaign: ";
        String toFind = campaign;
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout); 
        logger.debug(msg + toFind);
        
        // publish campaign
        toFind = "//button[@title='Options' and ancestor::div[contains(@id, 'jsview')]]";
        msg = "Click on Options menu button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        toFind = "//*[text()='Publish Campaign']/ancestor::*[@role='menuitem']";
        msg = "Click on publish campaign";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        toFind = "//*[text()='Yes']/ancestor::button[ancestor::*[@role='alertdialog']]";
        msg = "Click on Yes in the publish dialog";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        toFind = "//*[text()='Close']/ancestor::button[ancestor::*[@role='alertdialog']]";
        msg = "Click on Close in the publish dialog";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();


        // click on Close button for a cleaner ending.
        toFind = "//*[contains(text(), 'Close')]/ancestor::button[ancestor::header[//div[@role='toolbar']]]";
        msg = "Click on close campaign button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // if send schedule was sent to admin it will spawn an error dialog
        if(campaignNavigator.history.getValue(SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN))
        {   
            toFind = "//*[text()='Close']/ancestor::button[ancestor::*[@role='alertdialog']]";
            msg = "Check if a send to administrator error dialog is shown ";
            logger.debug(msg);
            logger.debug("xpath]: " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();
            
            // update history to false
            campaignNavigator.history.updateHistory(SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN, false);
        }

        // end
        msg = "END OK";
        logger.debug(msg);
    }
}