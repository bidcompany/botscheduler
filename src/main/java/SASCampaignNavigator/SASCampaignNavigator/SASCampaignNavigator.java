package SASCampaignNavigator.SASCampaignNavigator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;

public class SASCampaignNavigator extends CampaignNavigator
{           
    // log manager 
    private Logger logger = LogManager.getLogger(SASCampaignNavigator.class);
    
    private void SASLogin()
    {
        String url = "http://sas-aap.demo.sas.com/SASCIStudio/";
        String toFind;
        webDriver.navigate().to(url);
        logger.debug("Navigate to " + url);
        
        // digit username
        toFind = "username";
        logger.debug("Find id " + toFind);
        webDriver.findElement(By.id(toFind)).sendKeys("sasdemo");
    
        // digit password
        toFind = "password";
        logger.debug("Find id " + toFind);
        webDriver.findElement(By.id(toFind)).sendKeys("Orion123");
        logger.debug("Navigate to to " + url);

        // submit
        toFind = "btn-submit";
        logger.debug("Find class " + toFind);
        webDriver.findElement(By.className(toFind)).submit();
    }


    private void SASApproveCampaign(String campaignToApprove)
    {
        String msg = "START OK";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(webDriver, 60); // timeout 1 min

        logger.debug(msg);
        logger.debug("approving campaign " + campaignToApprove + "...");

        try
        {
            // Focus on the iframe
            toFind = "sasci_iframe";
            msg = "Switch to iframe";
            logger.debug(msg);
            logger.debug("id]: " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            webDriver.switchTo().frame(found);

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
            found.click();

            // filter with the target campaign name so the page is always focused on it
            toFind="__page0-searchField-I";
            logger.debug("Find element with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            logger.debug("Writing to input field " + campaignToApprove);
            found.clear();
            found.sendKeys(campaignToApprove);
            found.submit();

            // wait until text field has value campaignToApprove
            logger.debug("Waiting untill the to input field has value " + campaignToApprove);
            wait.until(ExpectedConditions.textToBePresentInElementValue(found, campaignToApprove));

            // Expand OutBound folder
            toFind = "Outbound";
            logger.debug("Find element with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
            found.click();
            
            // Expanf Examples folder
            toFind = "Examples";
            logger.debug("Find element with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
            found.click();

            // click on Title
            toFind = campaignToApprove;
            logger.debug("Find campaign with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']")));  
            found.click();

            /* 
                here it may spawn a dialog will asking to open the campaign in edit mode.
                It happens only if a bot crashed in the previous execution. => the bot will crash

                here it may spawn a dialog saying the campaign is approved and no changes may be applied.
                It happens only if the campaign is in Apporved state. => the bot will crash
            */

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
            logger.debug(campaignToApprove + " approved correctly!");
        }
        catch(Exception e)
        {
            /* 
                The bot crashes. We come up here if:
                    * timeout has ended because the specified element was not found in the html dom.
                    * timeout has ended while the page was still loading the html.
                    * html captures the click that the bot is performing. This happens when a 
                        "busy page" div overlaps the html page.  
            */
            msg = "The bot crashed due to the following exception.";
            logger.error(msg);
            logger.error(e.toString());
        }
        
    }

    @Override
    public void run()
    {
        // Setting the location of the chrome driver in the system properties
        logger.debug("Set webdriver to " + driverType);
        logger.debug("looking for webdriver bin to " + driverPath);
        System.setProperty(driverType, driverPath);

        // using Chrome
        logger.debug("Start the driver");
        webDriver = new ChromeDriver();

        // Setting the browser size
        Dimension broswerSize = new Dimension(1024, 768);
        webDriver.manage().window().setSize(broswerSize);
        logger.debug("Set webdriver window size to " + broswerSize.toString());

        // Task to perform the login to CI
        SASLogin();
        logger.debug("Login to CI completed");

        // Task to perform the approve of a campaign
        String campaignToApprove = "Navigator-01";
        SASApproveCampaign(campaignToApprove);
       
        // Closing the browser and webdriver
        logger.debug("Webdriver closing");
        webDriver.close();
        logger.debug("Webdriver quitting");
        webDriver.quit();
    }
}