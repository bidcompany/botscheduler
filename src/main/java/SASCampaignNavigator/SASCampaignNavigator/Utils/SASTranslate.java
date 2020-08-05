package SASCampaignNavigator.SASCampaignNavigator.Utils;

public class SASTranslate 
{
    // Italian days
    static String ITA_MON = "luned/u00ec";
    static String ITA_TUE = "marted/u00ec";
    static String ITA_WED = "mercoled/u00ec";
    static String ITA_THU = "gioved/u00ec";
    static String ITA_FRI = "venerd/u00ec";
    static String ITA_SAT = "sabato";
    static String ITA_SUN = "domenica";

    // Italian Months
    static String ITA_JAN = "gennaio";
    static String ITA_FEB = "febbraio";
    static String ITA_MAR = "marzo";
    static String ITA_APR = "aprile";
    static String ITA_MAY = "maggio";
    static String ITA_JUN = "giugno";
    static String ITA_JUL = "luglio";
    static String ITA_AUG = "agosto";
    static String ITA_SEP = "settembre";
    static String ITA_OCT = "ottobre";
    static String ITA_NOV = "novembre";
    static String ITA_DEC = "dicembre";
    
    // English days
    static String ENG_MON = "Monday,";
    static String ENG_TUE = "Tuesday,";
    static String ENG_WED = "Wednesday,";
    static String ENG_THU = "Thursday,";
    static String ENG_FRI = "Friday,";
    static String ENG_SAT = "Saturday,";
    static String ENG_SUN = "Sunday,";

    // English Months
    static String ENG_JAN = "January";
    static String ENG_FEB = "February";
    static String ENG_MAR = "March";
    static String ENG_APR = "April";
    static String ENG_MAY = "May";
    static String ENG_JUN = "June";
    static String ENG_JUL = "July";
    static String ENG_AUG = "August";
    static String ENG_SEP = "September";
    static String ENG_OCT = "October";
    static String ENG_NOV = "November";
    static String ENG_DEC = "December";
 
    // Italian Hours
    static String ITA_CET = "CET";
    static String ITA_CEST = "CEST";


    static public String toEnglish(String toTranslate)
    {
        String translated = toTranslate;
        
        // replace days
        translated = translated.replace(ITA_MON, ENG_MON);       
        translated = translated.replace(ITA_TUE, ENG_TUE);       
        translated = translated.replace(ITA_WED, ENG_WED);       
        translated = translated.replace(ITA_THU, ENG_THU);       
        translated = translated.replace(ITA_FRI, ENG_FRI);       
        translated = translated.replace(ITA_SAT, ENG_SAT);       
        translated = translated.replace(ITA_SUN, ENG_SUN);       
        
        // replace months
        translated = translated.replace(ITA_JAN,  ENG_JAN);       
        translated = translated.replace(ITA_FEB,  ENG_FEB);       
        translated = translated.replace(ITA_MAR,  ENG_MAR);       
        translated = translated.replace(ITA_APR,  ENG_APR);       
        translated = translated.replace(ITA_MAY,  ENG_MAY);       
        translated = translated.replace(ITA_JUN,  ENG_JUN);       
        translated = translated.replace(ITA_JUL,  ENG_JUL);       
        translated = translated.replace(ITA_AUG,  ENG_AUG);       
        translated = translated.replace(ITA_SEP,  ENG_SEP);       
        translated = translated.replace(ITA_OCT,  ENG_OCT);       
        translated = translated.replace(ITA_NOV,  ENG_NOV);       
        translated = translated.replace(ITA_DEC,  ENG_DEC);       

        return translated;
    } 

}