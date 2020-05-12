package SASCampaignNavigator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main 
{
    // log manager 
    private static final Logger logger = LogManager.getLogger(Main.class);

    public String getGreeting() 
    {
        return "Hello world.";
    }

    public static void main(String[] args) 
    {
        logger.debug(new Main().getGreeting());
    }
}
