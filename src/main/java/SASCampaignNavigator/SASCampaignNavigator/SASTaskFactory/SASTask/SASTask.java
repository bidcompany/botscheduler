package SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.XML2String;

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

        // // Focus on the iframe
        // toFind = "sasci_iframe";
        // msg = "Switch to iframe";
        // logger.debug(msg);
        // logger.debug("id]: " + toFind);
        // found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
        // webDriver.switchTo().frame(found);

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
        toFind = "Campaigns";
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
        
        // clicks three times to be sure an Older submitted job does not lead to problems 
        found.click();  // 1st time
        found.click();  // 2nd time
        found.click();  // 3rd time

        // filter with the target campaign name so the page is always focused on it
        toFind="__page0-searchField-I";
        logger.debug("Find element with id " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
        logger.debug("Writing to input field " + campaign);
        found.clear();
        found.sendKeys(campaign);
        found.submit();

        // wait until text field has value campaignToApprove
        logger.debug("Waiting untill the to input field has value " + campaign);
        wait.until(ExpectedConditions.textToBePresentInElementValue(found, campaign));

        // Expand OutBound folder
        toFind = campaignDir;
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
        found.click();
        
        // Expanf Examples folder
        toFind = campaignCategory;
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
        found.click();

        // click on Title
        toFind = campaign;
        logger.debug("Find campaign with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[@title='"+toFind+"']")));  
        found.click();

    }

    protected void closeCampaign()
    {
        String msg = "closing campaign: ";
        String toFind = campaign;
        WebElement found;
        WebDriverWait wait = new WebDriverWait(
            campaignNavigator.webDriver, campaignNavigator.timeout); 

        logger.debug(msg + toFind);
        
        // click on Close button for a cleaner ending.
        toFind = "//button[contains(@id, 'closeButton')]";
        msg = "Click on close campaign button";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
        found.click();

        // end
        msg = "END OK";
        logger.debug(msg);
    }
}