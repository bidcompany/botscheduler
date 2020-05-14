package SASCampaignNavigator.SASCampaignNavigator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
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
        String toFind;
        WebElement found;
        WebDriverWait wait = new WebDriverWait(webDriver, 60); // timeout 1 min

        logger.debug("start: approving campaign " + campaignToApprove);

        // click the campaign selector button
        toFind = "sapUiTreeIcon sapUiIcon";
        logger.debug("Find element with class " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.className(toFind)));  
        found.click();
        
        //webDriver.findElement(By.className(toFind)).click();

        // click on Campaigns Sections
        toFind = "Campaigns";
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[title^='"+toFind+"']")));  
        found.click();
        //webDriver.find_element_by_xpath('//*[@title="' + toFind + '"]').click();

        /* another solution to check
        String title="SOME TITLE";
        driver.findElement(By.cssSelector("[title^='"+title+"']")).click();*/

        // click on OutBound Sections
        toFind = "Outbound";
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[title^='"+toFind+"']")));  
        found.click();
        
        // click on OutBound Sections
        toFind = "Examples";
        logger.debug("Find element with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[title^='"+toFind+"']")));  
        found.click();
        
        // click by title
        toFind = campaignToApprove;
        logger.debug("Find campaign with title " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[title^='"+toFind+"']")));  
        found.click();
        
        // click on Approval by id
        toFind = "__filter204-text";
        logger.debug("Find approval tab with id " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
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

        // Waiting a little bit before closing
        try 
        {
            Thread.sleep(7000);
        }
        
        catch (Exception e) 
        {
            logger.error("The following exception occurred: " + e.toString());
        }
        // Closing the browser and webdriver
        logger.debug("Webdriver closing");
        webDriver.close();
        logger.debug("Webdriver quitting");
        webDriver.quit();
    }
}