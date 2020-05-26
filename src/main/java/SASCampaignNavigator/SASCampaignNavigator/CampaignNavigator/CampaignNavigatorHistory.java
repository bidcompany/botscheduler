package SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASHistory;

/* here we store the particular flags used to fix problems for multiple execution tasks */
public class CampaignNavigatorHistory
{

    // flags
    private boolean campaignSectionAlreadyOpen = false;
    
    public boolean getValue(String flag)
    {
        switch (flag)
        {
            case SASHistory.CAMPAIGN_SECTION_ALREADY_OPEN:
            {
                return campaignSectionAlreadyOpen;
            }
            default:
            {
                return false;
            }
        }
    }

    public void updateHistory(String flag, boolean value)
    {
        switch (flag)
        {
            case SASHistory.CAMPAIGN_SECTION_ALREADY_OPEN:
            {
                campaignSectionAlreadyOpen = value;
                break;
            }

            default:
            {
                break;
            }
        }
    }

    /* to use when bot refreshes the page or it crashes */
    public void refreshHistory()
    {
        campaignSectionAlreadyOpen = false;
    } 
}