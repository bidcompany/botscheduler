package SASCampaignNavigator.SASCampaignNavigator.Utils;

// here we put all the flsg keys related to the bot history. 
// the bot will use this  keys to store dynamic values during task execution
// this class is shared between the SasCampaignNavigator (bot) and the SasTasks   

public class SASHistory 
{
    // String keys  (... we could also use int or other stuff ... we don't care )
    public final static String CAMPAIGN_SECTION_ALREADY_OPEN = "campaignSectionAlreadyOpen";
    public final static String CAMPAIGN_SECTION_REFRESHED = "campaignSectionRefreshed";
    public final static String CAMPAIGN_SEND_SCHEDULE_ADMIN = "campaignSendScheduleAdmin";

}