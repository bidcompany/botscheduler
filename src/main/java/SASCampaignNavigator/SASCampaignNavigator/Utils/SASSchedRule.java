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
    final String SCHED_RECURS_EVRY = "Recurs every";
    final String SCHED_RECURS      = "Recurs";

    // logger
    private Logger logger = LogManager.getLogger(SASSchedRule.class);


    // task has campaignSched not null
    public  void fetchRule(String str, SASTask task)
    {
        // apply regex on start
        //Starts(.)*<br\/>.
        
        // apply regex on end
        // Ends(.)*<br\/>

        // apply regex on recurs
        // Recurs(.)*<br\/>

        List<String> schedList = Arrays.asList(str.split("<br/>"));
        logger.debug("Sched lengths is %d", schedList.size());
        
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
                schedStart(s, task);
            }    
            
            else if (s.contains(SCHED_ENDS))
            {
                // fetch ends
                logger.debug("Select End rule");
                schedEnd(s, task);
            }
            
            else if (s.contains(SCHED_RECURS_EVRY))
            {
                // fetch recurs every
                logger.debug("Select Recurs Every rule");
                schedRecursEvery(s, task);
            }
            
            else if (s.contains(SCHED_RECURS))
            {
                // fetch recurs
                logger.debug("Select Recurs rule");
                schedRecurs(s, task);
            }
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
            logger.debug("Word 1: " +  words[1]);   // starts
            logger.debug("Word 2: " +  words[2]);   // day,
            logger.debug("Word 3: " +  words[3]);   // month
            logger.debug("Word 4: " +  words[4]);   // num,
            logger.debug("Word 5: " +  words[5]);   // year ! add ','
            logger.debug("Word 6: " +  words[6]);   // hour
            logger.debug("Word 7: " +  words[7]);   // AM, PM

            String toWrite = words[3] + " " + words[4] + " " + 
                             words[5] + "," + " " + words[6] + " " + words[7];

            logger.debug("START DATE: " +  toWrite);

            // id="__picker18-inner"
            // filter with the target campaign name so the page is always focused on it 
            WebDriverWait wait = new WebDriverWait(task.campaignNavigator.webDriver, task.campaignNavigator.timeout);

            String toFind="__picker18-inner";
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

    }

    private void schedRecurs (String str, SASTask task)
    {

    }

    private void schedRecursEvery (String str, SASTask task)
    {

    }
}