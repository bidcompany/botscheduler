package SASCampaignNavigator.SASCampaignNavigator.CampaignNavigator;
import SASCampaignNavigator.SASCampaignNavigator.Utils.SASHistory;

/* here we store the particular flags used to fix problems for multiple execution tasks */
public class CampaignNavigatorHistory
{

    // flags
    private boolean campaignSectionAlreadyOpen = false;
    private boolean campaignSectionRefreshed = false;
    private boolean campaignSendScheduleAdmin = false;


    public boolean getValue(String flag)
    {
        switch (flag)
        {
            case SASHistory.CAMPAIGN_SECTION_ALREADY_OPEN:
            {
                return campaignSectionAlreadyOpen;
            }
            case SASHistory.CAMPAIGN_SECTION_REFRESHED:
            {
                return campaignSectionRefreshed;
            }
            case SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN:
            {
                return campaignSendScheduleAdmin;
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

            case SASHistory.CAMPAIGN_SECTION_REFRESHED:
            {
                campaignSectionRefreshed = value;
                break;
            }
            
            case SASHistory.CAMPAIGN_SEND_SCHEDULE_ADMIN:
            {
                campaignSendScheduleAdmin = value;
            }

            default:
            {
                break;
            }
        }
    }

    /* to use when bot refreshes the page or it crashes */
    public void resetHistory()
    {
        campaignSectionAlreadyOpen = false;
        campaignSectionRefreshed = false;
        campaignSendScheduleAdmin = false;
    } 
}