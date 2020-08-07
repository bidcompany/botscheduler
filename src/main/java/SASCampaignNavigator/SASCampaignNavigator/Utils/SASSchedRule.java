package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.util.List;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import SASCampaignNavigator.SASCampaignNavigator.SASTaskFactory.SASTask.SASTask;

public class SASSchedRule 
{
    // rule tokens
    public final String SCHED_STARTS      = "Starts";
    public final String SCHED_ENDS        = "Ends";
    public final String SCHED_ENDS_AFTER  = "Ends after";
    public final String SCHED_RECURS_EVRY = "Recurs every";
    public final String SCHED_RECURS_INDF = "Recurs indefinitely";

    // recurs every tokens
    public final String RECURS_HOURLY  = "hour(s)";
    public final String RECURS_DAILY   = "day(s)";
    public final String RECURS_WEEKLY  = "week(s)";
    public final String RECURS_MONTHLY = "month(s)";

    // logger
    private Logger logger = LogManager.getLogger(SASSchedRule.class);

    // rule
    private String startRule = "";
    private String endRule = "";
    private String endAfterRule = "";
    private String recursIndef = "";
    private String recursEveryRule = "";

    // task has campaignSched not null
    public  void fetchRule(String str, SASTask task)
    {
        List<String> schedList = Arrays.asList(str.split("<br/>"));
        logger.debug("Sched lengths is " + schedList.size());
        
        // Start index
        if(schedList.size() < 2)
        {
            logger.debug("Sched rule is too short");
            logger.debug(str);
            return;
        }

        // for cycle
        for (int i = 2; i < schedList.size(); i ++)
        {
            String s = schedList.get(i);
            logger.debug("Look for sched rule for string " +  s); 

            if (s.contains(SCHED_STARTS))
            {
                // fetch Start
                logger.debug("Select Start rule");
                startRule = s;
                //schedStart(s, task);
            }    
     
            else if (s.contains(SCHED_ENDS_AFTER))
            {
                // fetch ends
                logger.debug("Select End After rule");
                endAfterRule = s;
                //schedEnd(s, task);
            }
     

            else if (s.contains(SCHED_ENDS))
            {
                // fetch ends
                logger.debug("Select End rule");
                endRule = s;
                //schedEnd(s, task);
            }
            
            else if (s.contains(SCHED_RECURS_EVRY))
            {
                // fetch recurs every
                logger.debug("Select Recurs Every rule");
                recursEveryRule = s;
                //schedRecursEvery(s, task);
            }
            
            else if (s.contains(SCHED_RECURS_INDF))
            {
                // fetch recurs indefinetly
                logger.debug("Select Recurs indefinitely");
                recursIndef = s;
                //schedRecurs(s, task);
            }
        }

        // fetch recurs pattern
        recursPattern(task);

        // fetch start pattern
        startPattern(task);

        // fetch end pattern
        endPattern(task);
    }

    private void recursPattern(SASTask task)
    {
        // init webdriver stuff
        WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);
        String toFind = "";
        String toWrite = "";
        String msg = "";
        WebElement found;  

        // check recurrence pattern
        if (recursEveryRule.contains(RECURS_HOURLY))
        {
            logger.debug("Select recurs hourly");
            logger.debug("rule: " + recursEveryRule);
            
            // click hourly checkbox
            toFind="//*[text()='Hourly' and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]/ancestor::div[@role='radio']";
            msg = "Find checkbox";
            logger.debug(msg + " Hourly");
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // click on hours-selector
            toFind="//*[@role = 'combobox' and ancestor::tr//*[text()='Recur every:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Select hour in tablist";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // specify hours
            toWrite = recursEveryRule.split(" ")[2];
            toFind="//li[@role = 'option' and text()='"+ toWrite +"' and not(ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule'])]";
            msg = "specify hours in tablist:";
            logger.debug(msg + " " + toWrite);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

        }

        else if (recursEveryRule.contains(RECURS_DAILY))
        {
            logger.debug("Select recurs daily");
            logger.debug("rule: " + recursEveryRule);

            // click daily checkbox
            toFind="//*[text()='Daily' and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]/ancestor::div[@role='radio']";
            msg = "Find checkbox";
            logger.debug(msg + " Daily");
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // specify every-day
            toWrite = recursEveryRule.split(" ")[2];
            toFind = "//input[ancestor::tr//*[text()='Recur every:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Specify every-day";
            logger.debug(msg + ": " + toWrite);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            logger.debug("Write to input field " + toWrite);
            found.clear();
            found.sendKeys(toWrite);
        }

        else if (recursEveryRule.contains(RECURS_WEEKLY))
        {
            logger.debug("Select recurs weekly");
            logger.debug("rule: " + recursEveryRule);

            // click weekly checkbox
            toFind="//*[text()='Weekly' and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]/ancestor::div[@role='radio']";
            msg = "Find checkbox";
            logger.debug(msg + " Weekly");
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // specify every-week
            toWrite = recursEveryRule.split(" ")[2];
            toFind = "//input[ancestor::tr//*[text()='Recur every:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Specify every-week";
            logger.debug(msg + ": " + toWrite);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            logger.debug("Write to input field " + toWrite);
            found.clear();
            found.sendKeys(toWrite);

            // TO FIX: MULTIBOX and RESET ALREADY PRESENT DAYS
            // toFind = "//*[text()='Sunday' or " +
            //              "text()='Monday' or" +
            //              "text()='Tuesday' or" +
            //              "text()='Wednesday' or" +
            //              "text()='Thursday' or" +
            //              "text()='Friday' or" +
            //              "text()='Saturday']/ancestor::*[@role='checkbox' and @aria-checked='true']";

            // //*[text()='Sunday' or ext()='Monday' or text()='Tuesday' or" +
            // "text()='Wednesday' or" +
            // "text()='Thursday' or" +
            // "text()='Friday' or" +
            // "text()='Saturday']/ancestor::*[@role='checkbox' and @aria-checked='true']";

            // click checkbox on-day
            toWrite = recursEveryRule.split(" ")[5];
            toFind = "//*[text()='"+ toWrite +"']/ancestor::*[@role='checkbox']";
            msg = "Specify on day";
            logger.debug(msg + ": " + toWrite);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();
        }

        else if (recursEveryRule.contains(RECURS_MONTHLY))
        {
            logger.debug("Select recurs monthly");
            logger.debug("rule: " + recursEveryRule);

            // click monthly checkbox
            toFind="//*[text()='Monthly' and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]/ancestor::div[@role='radio']";
            msg = "Find checkbox";
            logger.debug(msg + " Monthly");
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // specify evert-month
            toWrite = recursEveryRule.split(" ")[2];
            toFind = "//input[ancestor::tr//*[text()='Recur every:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Specify every-week";
            logger.debug(msg + ": " + toWrite);
            logger.debug("xpath] " + toFind);
            found = task.campaignNavigator.webDriver.findElements(By.xpath(toFind)).get(0);
            found.clear();
            found.sendKeys(toWrite);
            
            // specify on-day
            toWrite = recursEveryRule.split(" ")[6];
            found = task.campaignNavigator.webDriver.findElements(By.xpath(toFind)).get(1);
            logger.debug("Specify on-day: " + toWrite);
            found.clear();
            found.sendKeys(toWrite);
            
        }

        else 
        {
            logger.debug("Select recurs once");
            logger.debug("rule: " + recursEveryRule);

            // click once checkbox
            toFind="//*[text()='Once' and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]/ancestor::div[@role='radio']";
            msg = "Find checkbox";
            logger.debug(msg + " Once");
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

        }
    }

    private void startPattern(SASTask task)
    {
        // init webdriver stuff
        WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);
        String toFind = "";
        String toWrite = "";
        String msg = "";
        WebElement found;  

        // split in space
        String[] words = startRule.split(" ");        

        logger.debug("num of words in rule: " + words.length);
        try
        {
            // words
            logger.debug("Word 0: " +  words[0]);   // keyword
            logger.debug("Word 1: " +  words[1]);   // day
            logger.debug("Word 2: " +  words[2]);   // month
            logger.debug("Word 3: " +  words[3]);   // day,
            logger.debug("Word 4: " +  words[4]);   // year
            logger.debug("Word 5: " +  words[5]);   // hour
            logger.debug("Word 6: " +  words[6]);   // am, pm
            logger.debug("Word 7: " +  words[7]);   // edt

            // this format is important, otherwise is rejected by CIStudio
            // ex: March 18, 2021, 4:17 PM
            toWrite = words[2] + words[3] + " " + words[4] + ", " + 
                             words[5] + " " + words[6];
            logger.debug("START DATE: " +  toWrite);
            
            toFind="//input[@type='text' and ancestor::tr//*[text()='Start date/time:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Find the search box to type the name of the campaign";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            logger.debug("Write to input field " + toWrite);
            found.clear();
            found.sendKeys(toWrite);
            found.submit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.debug("An Exception occured, sched Rule is not valid");
            return;
        }
    }

    private void endPattern (SASTask task)
    {
        // init webdriver stuff
        WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);
        String toFind = "";
        String toWrite = "";
        String msg = "";
        WebElement found;  

        
        // if is Once campaign skip
        if(recursEveryRule.isEmpty())
        {
            logger.debug("Skip fetching end rule for a once campaign");
            return;
        }

        // if Recurse indefinitely select No end Date
        if (!recursIndef.isEmpty())
        {
            toFind = "//*[text()='No end date']/ancestor::div[@role='radio']";
            msg = "Select no end date radio button";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();
            return;
        }
       
        // if end Rule is not empty, check if hourly (day + time format) else (day format) 
        if (!endRule.isEmpty())
        {
            toFind = "//*[contains(text(), 'End date')]/ancestor::div[@role='radio']";
            msg = "Select end date(/time) radio button";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();
            
            // format date/time
            if(recursEveryRule.contains(RECURS_HOURLY))
            {
                String[] words = endRule.split(" ");        
                toWrite = words[2] + words[3] + " " + words[4] + ", " + 
                words[5] + " " + words[6];
                
                logger.debug("End date: " +  toWrite);
                toFind="//input[@type='text' and @role='combobox' and ancestor::tr//*[contains(text(), 'End date')] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
                msg = "Find the input field for End Date ";
                logger.debug(msg);
                logger.debug("xpath] " + toFind);
                found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
                found.clear();
                found.sendKeys(toWrite);
                return;
            }

            // format only date
            else
            {
                String[] words = endRule.split(" ");        
                toWrite = words[2] + words[3] + " " + words[4];
                
                logger.debug("End date: " +  toWrite);
                toFind="//input[@type='text' and @role='combobox' and ancestor::tr//*[contains(text(), 'End date')] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
                msg = "Find the input field for End Date ";
                logger.debug(msg);
                logger.debug("xpath] " + toFind);
                found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
                found.clear();
                found.sendKeys(toWrite);
                return;
            }
        }

        // End After rule - ex:</br> Ends after 4 Occurrences
        if(!endAfterRule.isEmpty())
        {
            // select radio button
            toFind = "//*[text()='End after:']/ancestor::div[@role='radio']";
            msg = "Select end after radio button";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.click();

            // write in first box the num
            toWrite = endAfterRule.split(" ")[2];        
            logger.debug("End after: " +  toWrite);
            toFind="//input[@type='tel' and ancestor::tr//*[text()='End after:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Find the input field for End after ";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(toFind)));  
            found.clear();
            found.sendKeys(toWrite);
        
            // click on event-selector
            toFind="//div[@role = 'combobox' and ancestor::tr//*[text()='End after:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            msg = "Select type event in tablist";
            logger.debug(msg);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            // specify event
            toWrite = endAfterRule.split(" ")[3];
            //li[@role = 'option' and text()='Days' and not(ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule'])]
            toFind="//li[@role = 'option' and text()='"+ toWrite +"' and not(ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule'])]";
            msg = "specify event in tablist:";
            logger.debug(msg + " " + toWrite);
            logger.debug("xpath] " + toFind);
            found = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(toFind)));  
            found.click();

            return;
        }

        logger.debug("Error in end rules. Something wrong in the following rules");
        logger.debug("No End Date: " + recursIndef);
        logger.debug("End Date/time: " + endRule);
        logger.debug("End After rule: " + endAfterRule);

    }
        
}