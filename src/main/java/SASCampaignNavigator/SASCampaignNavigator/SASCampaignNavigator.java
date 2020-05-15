package SASCampaignNavigator.SASCampaignNavigator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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
        logger.debug("Navigate to to " + url);
        
        // digit username
        toFind = "username";
        logger.debug("Find element with id " + toFind);
        webDriver.findElement(By.id(toFind)).sendKeys("sasdemo");
    
        // digit password
        toFind = "password";
        logger.debug("Find element with id " + toFind);
        webDriver.findElement(By.id(toFind)).sendKeys("Orion123");
        logger.debug("Navigate to to " + url);

        // submit
        toFind = "btn-submit";
        logger.debug("Find element with class " + toFind);
        webDriver.findElement(By.className(toFind)).submit();
    }


    private void SASApproveCampaign(String campaignToApprove)
    {
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(webDriver, 60); // timeout 1 min

        logger.debug("start: approving campaign " + campaignToApprove);

        try
        {
            // switch to focus on the iframe
            toFind = "sasci_iframe";
            logger.debug("Find element with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            webDriver.switchTo().frame(found);

            // click the Designer Tab
            /*toFind = "sapMITBContentArrow";
            logger.debug("Find element with class " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();*/

            // click the hierarchy button
            /*toFind = "__button54";
            logger.debug("Find element with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();*/

            // click the campaign selector button
            toFind = "__node0";
            logger.debug("Find element with id " + toFind);
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
            
            // click on OutBound Sections
            toFind = "Examples";
            logger.debug("Find element with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']/..//*[@role='button']")));  
            found.click();

            // click by title
            toFind = campaignToApprove;
            logger.debug("Find campaign with title " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[@title='"+toFind+"']")));  
            found.click();

            // click on Approval by id
            /*toFind = "__filter204-text";
            logger.debug("Find approval tab with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();*/

            logger.debug("Find Approval tab");
            found = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[text()='Approval']/parent::div[@role='tab']")));  
            found.click();

            // click on Approve first Confirm Button
            toFind = "__button1369";
            logger.debug("Find approve button with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();

            // click on Approve second Confirm Button
            toFind = "__button1498";
            logger.debug("Find approve button with id " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            found.click();

            // end
            logger.debug("end: campaign " + campaignToApprove + " approved correctly");
        }
        catch(Exception e)
        {
            // we come up here if no elements are found in the html dom
            logger.error("No element with key " + toFind + " is found in the dom");
            logger.error(e.toString());

            // print the source html which selenium is working on
            //String html = webDriver.getPageSource(); //webDriver.getPageSource();
            //logger.debug("Source html:\n" + html);
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