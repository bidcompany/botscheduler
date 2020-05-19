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
            // switch to focus on the iframe
            toFind = "sasci_iframe";
            msg = "Switch to iframe";
            logger.debug(msg);
            logger.debug("id:" + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            webDriver.switchTo().frame(found);

            // click Designer Button
            //*[text()='Designer' and ancestor::div[@role='tab']]/ancestor::div[@role='tab']
            toFind = "//*[text()='Designer' and ancestor::div[@role='tab']]/ancestor::div[@role='tab']";
            msg = "Click on Designer menu button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();
            
            // wait untill busy page is invisible, otherwise it will intercept the click
            toFind = "//*[@title='Please wait']";
            msg = "Wait untill the busy overlay is invisible";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));  

            // click Hierarchy view button 
            //*[@title='Hierarchy view' and @role="radio"]
            toFind = "//*[@title='Hierarchy view' and @role='radio']";
            msg = "Click on Hierarchy View button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // click the campaign selector button
            toFind = "__node0";
            msg = "Click on Campaign expand button";
            logger.debug(msg);
            logger.debug("id:" + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();
            
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

            // click on OutBound Sections
            toFind = "Outbound";
            logger.debug("Find element with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
            found.click();
            
            // click on Examples Sections
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
                It happens only if a bot crashed in the previous execution.
            */

            // click on List of tabs buttons
            toFind = "//button[@title='List of tabs']";
            msg = "Click on List of Tabs button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // click on Approval menuradio
            toFind = "//*[text()='Approval' and ancestor::li[@role='menuitemradio']]/ancestor::li";
            msg = "Click on Approval menu section";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();
            
            // click on Approve fst Confirm Button
            toFind = "//*[text()='Approve' and ancestor::section]/ancestor::button";
            msg = "Click on Approve button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // click on Approve second Confirm Button
            toFind = "//*[text()='Approve' and ancestor::div[@role='dialog']]/ancestor::button";
            msg = "Click again on Approve button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // wait untill busy page is invisible, otherwise it will intercept the click
            toFind = "//*[@title='Please wait']";
            msg = "Wait untill the busy overlay is invisible";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));

            // click on Close button
            toFind = "//button[contains(@id, 'closeButton')]";
            msg = "Click on close campaign button";
            logger.debug(msg);
            logger.debug("xpath:" + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // end
            msg = "END OK";
            logger.debug(msg);
            logger.debug(campaignToApprove + " approved correctly!");
        }
        catch(Exception e)
        {
            // we come up here if no elements are found in the html dom
            logger.error("No element with key " + toFind + " is found in the dom");
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