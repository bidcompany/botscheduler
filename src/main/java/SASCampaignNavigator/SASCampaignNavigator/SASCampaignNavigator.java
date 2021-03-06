package SASCampaignNavigator.SASCampaignNavigator;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.*;
import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.*;
import SASCampaignNavigator.SASCampaignNavigator.Utils.XML2String;

public class SASCampaignNavigator extends CampaignNavigator
{           
    // log manager 
    private Logger logger = LogManager.getLogger(SASCampaignNavigator.class);
    
    private void SASLogin()
    {
        String msg = "not Initialized";
        String toFind = "not Initialized";
        WebElement found;
        WebDriverWait wait = new WebDriverWait(webDriver, timeout);

        // Navigate to the url
        String url = this.url;
        msg = "Navigate to url: " + url;
        logger.debug(msg);
        webDriver.navigate().to(url);
        
        // wait if ISP sign in is present
        toFind = "//img[@alt='IntesaSanpaolo']"; 
        msg = "Check if ISP sign in is present";
        logger.debug(msg);
        logger.debug("xpath]: " + toFind);
        if(webDriver.findElements(By.xpath(toFind)).size() > 0)
        {
            msg = "Please sign in with ISP credentials.";
            logger.debug(msg);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(toFind)));
            
            // Focus on the iframe
            toFind = "sasci_iframe";
            msg = "Switch to iframe";
            logger.debug(msg);
            logger.debug("id]: " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            webDriver.switchTo().frame(found);

            // Select Business Context
            toFind = "//*[text()='Intesa']/ancestor::*[@role='option']";
            msg = "Select the Intesa business context button";
            logger.debug(msg);
            logger.debug("xpath]: " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // Click Ok 
            toFind = "//*[text()='OK']/ancestor::button";
            msg = "Click on the OK button";
            logger.debug(msg);
            logger.debug("xpath]: " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();
            return;
        }
        
        // digit username
        toFind = "username";
        msg = "Insert username: " + this.user;
        logger.debug(msg);
        logger.debug("id]: " + toFind);
        found = wait.until(ExpectedConditions.elementToBeClickable(By.id(toFind)));  
        found.sendKeys(this.user);
        
        // digit password
        toFind = "password";
        msg = "Insert password: " + this.password.replaceAll(".", "*");
        logger.debug(msg);
        logger.debug("id]: " + toFind);
        webDriver.findElement(By.id(toFind)).sendKeys(this.password);
        logger.debug("Navigate to to " + url);

        // submit
        toFind = "btn-submit";
        logger.debug("Find class " + toFind);
        webDriver.findElement(By.className(toFind)).submit();

        // Focus on the iframe
        toFind = "sasci_iframe";
        msg = "Switch to iframe";
        logger.debug(msg);
        logger.debug("id]: " + toFind);
        found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
        webDriver.switchTo().frame(found);

    }

    @Override
    public void run()
    {
        // Setting the location of the chrome driver in the system properties
        logger.debug("Set webdriver to " + driverType);
        logger.debug("looking for webdriver bin to " + driverPath);
        System.setProperty(driverType, driverPath);

        // using Chrome
        logger.debug("Start the webdriver");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("useAutomationExtension", false);
        logger.debug("Set useAutomationExtension to false");
        
        webDriver = new ChromeDriver(options);
        
        // Setting the browser size
        Dimension broswerSize = new Dimension(1024, 768);
        webDriver.manage().window().setSize(broswerSize);
        logger.debug("Set webdriver window size to " + broswerSize.toString());

        // Task to perform the login to CI
        SASLogin();
        logger.debug("Login to CI completed");

        // for each campaign in config file execute the task
        try 
        {
            // convert xml string config in tree document
            Element root = XML2String.toXML(xmlConfig);
            List<Element> campaignList = root.getChild("campaignList").getChildren("campaign");
            logger.debug("Num of task to perform " + campaignList.size());
            
            // for each campaign create a task
            for (Element campaign : campaignList)
            {
                // convert campaign dom element to string to pass it to the task factory
                String campaignConfig = XML2String.toString(campaign);
                logger.debug("create a task from following xml:\n\t" + campaignConfig);

                // get task type from campaign dom element
                String taskType = campaign.getChild("taskType").getValue();
                
                // get the task factory
                SASTaskFactory taskFactory = SASTaskFactory.getFactory();
                SASTask task = taskFactory.fetchTask(this, taskType, campaignConfig);

                // execute the task
                if (task != null)
                    task.exec();    // exception coming from here should not crash the entire for cycle
            }            
        }  
        catch(Exception e)
        {
            // config file is empty
            // config file is not formatted as expected
            // task is null due to not implemented taskType

            logger.error("The bot crashed due to the following exception. " + e.toString());
            e.printStackTrace();
        }
        
        // Closing the browser and webdriver
        logger.debug("Webdriver closing");
        webDriver.close();
        logger.debug("Webdriver quitting");
        webDriver.quit();
    }
}