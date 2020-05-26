package SASCampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.SASCampaignNavigator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main 
{
    // log manager 
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) 
    {
        logger.debug("Started the main");
        logger.debug("Create SAS Campaign Navigator");
        SASCampaignNavigator campaignNavigator = new SASCampaignNavigator();
        logger.debug("Run SAS Campaign Navigator");
        campaignNavigator.run();
    }
}
