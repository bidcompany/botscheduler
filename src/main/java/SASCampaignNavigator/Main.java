package SASCampaignNavigator;
import SASCampaignNavigator.CampaignNavigator.CampaignNavigator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main 
{
    // log manager 
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) 
    {
        logger.debug("Started the main");
        logger.debug("Create a navigator task");
        CampaignNavigator campaignNavigator = new CampaignNavigator();
        logger.debug("Run a navigator task");
        campaignNavigator.run();

    }
}
