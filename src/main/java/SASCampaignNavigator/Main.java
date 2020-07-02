package SASCampaignNavigator;
import org.openqa.selenium.json.Json;

import SASCampaignNavigator.SASCampaignNavigator.SASCampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.Json2Xml;

public class Main 
{

    public static void main(String[] args) 
    {
        // create the cfg from json
        Json2Xml json2Xml = new Json2Xml();
        json2Xml.createSASCampaignNavigatorCFG();

        SASCampaignNavigator campaignNavigator = new SASCampaignNavigator();
        //campaignNavigator.run();
    }
}
