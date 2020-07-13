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
    final String SCHED_STARTS      = "Starts";
    final String SCHED_ENDS        = "Ends";
    final String SCHED_ENDS_AFTER  = "Ends after";
    final String SCHED_RECURS_EVRY = "Recurs every";
    final String SCHED_RECURS      = "Recurs";

    // recurs every tokens
    final String RECURS_HOURLY  = "hour(s)";
    final String RECURS_DAILY   = "day(s)";
    final String RECURS_WEEKLY  = "week(s)";
    final String RECURS_MONTHLY = "month(s)";

    // logger
    private Logger logger = LogManager.getLogger(SASSchedRule.class);

    // rule
    private String startRule = "";
    private String endRule = "";
    private String endAfterRule = "";
    private String recursRule = "";
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
            
            else if (s.contains(SCHED_RECURS))
            {
                // fetch recurs
                logger.debug("Select Recurs rule");
                recursRule = s;
                //schedRecurs(s, task);
            }
        }

        // fetch recurs pattern
        recursPattern(task);

        // fetch start pattern

        // fetch end pattern
    }

    private void recursPattern(SASTask task)
    {
        // init webdriver stuff
        WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);
        String toFind = "";
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

            // specify every-hours
        
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

            // click checkbox on-day

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

            // specify on-day
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

    private void schedStart (String str, SASTask task)
    {
        // remove commas
        //str.replace(",", "");
        
        // split in space
        String[] words = str.split(" ");

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

            String toWrite = words[2] + words[3] + " " + words[4] + ", " + 
                             words[5] + " " + words[6];

            logger.debug("START DATE: " +  toWrite);

            // id="__picker18-inner"
            // filter with the target campaign name so the page is always focused on it 
            WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);

            String toFind="//input[@type='text' and ancestor::tr//*[text()='Start date/time:'] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            String msg = "Find the search box to type the name of the campaign";
            logger.debug(msg);
            logger.debug("id] " + toFind);
            WebElement found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
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

    private void schedEnd (String str, SASTask task)
    {
        // remove commas
        //str.replace(",", "");
        
        // split in space
        String[] words = str.split(" ");

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

            String toWrite = words[2] + words[3] + " " + words[4] + ", " + 
                             words[5] + " " + words[6];

            logger.debug("END DATE: " +  toWrite);

            // id="__picker18-inner"
            // filter with the target campaign name so the page is always focused on it 
            WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);

            String toFind="//input[@type='text' and not(@readonly = 'readonly') and ancestor::tr//*[contains(text(), 'End date')] and ancestor::div[@role = 'dialog' ]//*[text()='Set Schedule']]";
            String msg = "Find the search box to type the name of the campaign";
            logger.debug(msg);
            logger.debug("id] " + toFind);
            WebElement found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
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

    private void schedRecurs (String str, SASTask task)
    {
        String[] words = str.split(" ");
        logger.debug("num of words in rule: " + words.length);
        try
        {
            // words
            logger.debug("Word 1: " +  words[1]);   // Recurs
            
            if(words[1].equals("indefinitely"))
            {
                // select no end
            }
            
            // else there will be an end to select
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.debug("An Exception occured, sched Rule is not valid");
            return;
        }
    }

    private void debugRule (String str)
    {
        String[] words = str.split(" ");
        int i = 0;
        for (String s : words)
        {
            logger.debug("Word " + i + ": " + s);
        }
    }

    private void schedRecursEvery (String str, SASTask task)
    {
        String[] words = str.split(" ");

        logger.debug("num of words in rule: " + words.length);
        try
        {
            // words
            logger.debug("Word 0: " +  words[0]);   // Recurs
            logger.debug("Word 1: " +  words[1]);   // every
            logger.debug("Word 2: " +  words[2]);   // num
            logger.debug("Word 3: " +  words[3]);   // week(s)
            logger.debug("Word 4: " +  words[4]);   // on
            logger.debug("Word 5: " +  words[5]);   // Day
            
            String num = words[2];
            String time = words[3]; // to clean 
            String day = words[5];

            logger.debug("Recurse every: " +  num + " " + time + ":" + day);

            // id="__picker18-inner"
            // filter with the target campaign name so the page is always focused on it 
            /*WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);

            String toFind="__picker35-inner";
            String msg = "Find the search box to type the name of the campaign";
            logger.debug(msg);
            logger.debug("id] " + toFind);
            WebElement found = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(toFind)));  
            logger.debug("Write to input field " + toWrite);
            found.clear();
            found.sendKeys(toWrite);
            found.submit();
            */
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.debug("An Exception occured, sched Rule is not valid");
            return;
        }
    }
}