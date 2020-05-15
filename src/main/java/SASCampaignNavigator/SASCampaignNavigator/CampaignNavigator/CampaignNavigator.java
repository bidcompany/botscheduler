package SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/* Navigator bot class. It Should be able to connect to target web page, detect the target html elements
 and perform actions. It has also some data, a list of campaigns */

/* it should be cool to be multithread */
public class CampaignNavigator 
{       
    // log manager 
    private Logger logger = LogManager.getLogger(CampaignNavigator.class);
    
    public String driverType;
    public String driverPath;
    public WebDriver webDriver;

    // Constructor
    public CampaignNavigator( /* a list of campaigns */)    /* implements Runnable*/
    {
        // allocate stuff 
        // ..

        driverType = "webdriver.chrome.driver";

        // get current working path
        try
        {
            // production: lib\chromedriver.exe
            File jarFile = new File (getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            String url = jarFile.getParentFile().getPath();
            driverPath = url + "\\chromedriver.exe";
            logger.debug("driverPath: " + driverPath);
        }
        catch(Exception e)
        {
            logger.warn("Impossible to get relative path of webdriver due to" + 
                "the following exception: " + e.toString());
            logger.warn("Webdriver test relative path will be used");
            driverPath = "\\external\\chromedriver.exe";
        }
    }

    // Selenium example: perform a wikipedia search 
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
        
        // Go to wikipedia
        String url = "https://en.wikipedia.org/wiki/Main_Page";
        webDriver.navigate().to(url);
        logger.debug("Navigate to to " + url);
        
        // Type in the search-field: "WebDriver"
        webDriver.findElement(By.id("searchInput")).sendKeys("WebDriver");

        // submitting the search query
        webDriver.findElement(By.id("searchInput")).submit();

        // Test if Wikipedia redirects to the correct article:
        // "Selenium (software)"
        String textFound = webDriver.findElement(By.cssSelector("h1")).getText();
        if (textFound.contains("Selenium (software)")) 
        {
            System.out.println("Test passes!");
        } 
        else 
        {
            System.out.println("Test fails!");
        }

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