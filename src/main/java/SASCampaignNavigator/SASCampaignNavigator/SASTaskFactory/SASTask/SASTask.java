package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

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
    protected String campaignDir;
    protected String campaignCategory;
    protected String taskType;
    
    // WebDriver instance from SASCampaignNavigator
    //protected WebDriver webDriver;
    protected CampaignNavigator campaignNavigator; 

    // implemented in childern class
    public abstract void exec();

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

            this.campaignDir = root.getChild("directory").getValue();
            logger.debug("set target campaign directory: " + campaignDir);

            this.campaignCategory = root.getChild("category").getValue();
            logger.debug("set target campaign category: " + campaignCategory);
            
            //this.timeout = Integer.parseInt(root.getChild("timeout").getValue());            
            //logger.debug("set target campaign timeout: " + timeout);
            
            if (campaign == null)
                throw new NullPointerException("campaign name is null");
            if (campaignDir == null)
                throw new NullPointerException("campaign directory is null");
            if (campaignCategory == null)
                throw new NullPointerException("campaign category is null");
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

        // Expand OutBound folder
        toFind = "//*[@title='"+ campaignDir +"']/..//*[@role='button']";
        msg = "Expand " + campaignDir + " directory tree";
        logger.debug(msg);
        logger.debug("xpath] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
        found.click();
        
        // Expand Examples folder
        toFind = "//*[@title='"+ campaignCategory +"']/..//*[@role='button']";
        msg = "Expand " + campaignCategory + " directory tree";
        logger.debug(msg);
        logger.debug("xpath] " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
        found.click();

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
        
        // click on Close button for a cleaner ending.
        toFind = "//*[contains(text(), 'Close')]/ancestor::button[ancestor::header[//div[@role='toolbar']]]";
        //toFind = "//button[contains(@id, 'closeButton')]";
        msg = "Click on close campaign button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // check if the sending schedule to administrator has failed. 
        toFind = "//*[text()='Close']/ancestor::button[ancestor::*[@role='alertdialog']]";
        msg = "Check if a send to administrator error dialog is shown ";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(campaignNavigator.webDriver.findElements(By.xpath(toFind)).size() != 0)
        {   
            // click cancel and continue
            campaignNavigator.webDriver.findElement(By.xpath(toFind)).click();
            logger.warn("Camapaign " + campaign + " send schedule to administrator failed");
            
        }

        // end
        msg = "END OK";
        logger.debug(msg);
    }
}