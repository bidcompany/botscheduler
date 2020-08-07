package SASCampaignNavigator.SASCampaignNavigator.Utils;

import java.util.List;
import java.util.Arrays;

public class SASTranslate 
{
    // Italian days
    final static String ITA_MON = "luned/u00ec";
    final static String ITA_TUE = "marted/u00ec";
    final static String ITA_WED = "mercoled/u00ec";
    final static String ITA_THU = "gioved/u00ec";
    final static String ITA_FRI = "venerd/u00ec";
    final static String ITA_SAT = "sabato";
    final static String ITA_SUN = "domenica";

    // Italian Months
    final static String ITA_JAN = "gennaio";
    final static String ITA_FEB = "febbraio";
    final static String ITA_MAR = "marzo";
    final static String ITA_APR = "aprile";
    final static String ITA_MAY = "maggio";
    final static String ITA_JUN = "giugno";
    final static String ITA_JUL = "luglio";
    final static String ITA_AUG = "agosto";
    final static String ITA_SEP = "settembre";
    final static String ITA_OCT = "ottobre";
    final static String ITA_NOV = "novembre";
    final static String ITA_DEC = "dicembre";
    
    // English days
    final static String ENG_MON = "Monday,";
    final static String ENG_TUE = "Tuesday,";
    final static String ENG_WED = "Wednesday,";
    final static String ENG_THU = "Thursday,";
    final static String ENG_FRI = "Friday,";
    final static String ENG_SAT = "Saturday,";
    final static String ENG_SUN = "Sunday,";

    // English Months
    final static String ENG_JAN = "January";
    final static String ENG_FEB = "February";
    final static String ENG_MAR = "March";
    final static String ENG_APR = "April";
    final static String ENG_MAY = "May";
    final static String ENG_JUN = "June";
    final static String ENG_JUL = "July";
    final static String ENG_AUG = "August";
    final static String ENG_SEP = "September";
    final static String ENG_OCT = "October";
    final static String ENG_NOV = "November";
    final static String ENG_DEC = "December";
 
    // English Hours
    final static String ENG_AM = "AM";
    final static String ENG_PM = "PM";
    final static String ENG_ET = "EDT";
    final static String ENG_OC = "o'clock";


    static public String toEnglish(String toTranslate)
    {
        String translated = toTranslate;
  
        // check if format should be changed
        boolean isWrong = isWrongFormat(translated);

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

        // delete o'clock
        translated = translated.replace(ENG_OC, "");

        // reformat
        if (isWrong)
        {
            System.out.println("Is Wrong");

            // check pattern inside rules   
            List<String> schedList = Arrays.asList(translated.split("<br/>"));
            for (String s : schedList)
            {
                System.out.println("#] " + s);

                // convert Starts
                if (s.contains("Starts") || (s.contains("Ends") && !s.contains("by")))
                {
                    String[] startRule = s.split(" ");           
                    String keyStr = startRule[0];
                    String dayStr = startRule[1];
                    String numStr = startRule[2];
                    String monStr = startRule[3];
                    String yyrStr = startRule[4];
                    String horStr = startRule[5];
                    String estStr = startRule[6];
                    String hplStr = ENG_AM;

                    // convert hours
                    horStr = horStr.replace(":", ".");
                    String[] time = horStr.split(":");
                    try 
                    {
                        int hh = Integer.parseInt(time[0]);
                        int mm = Integer.parseInt(time[1]);
                        int ss = Integer.parseInt(time[2]);                            

                        // 12 -> 12 PM
                        if (hh == 12)
                        {
                            hplStr = ENG_PM;
                        }

                        // 0 -> 12 AM
                        else if (hh == 0)
                        {
                            hh = 12;
                            hplStr = ENG_AM;
                        }

                        else if (hh > 12)
                        {
                            hplStr = ENG_PM;
                            hh -= 12;
                        }

                        // replace
                        horStr = "" + hh + ":" + time[1] + ":" + time[2];
                    }
                    catch (Exception e)
                    {
                        System.out.println("eccezzione");
                        e.printStackTrace();
                        return translated;
                    }
                    

                    // rearrange
                    s = keyStr + " " + 
                        dayStr + " " +
                        monStr + " " +
                        numStr + " " +
                        yyrStr + " " +
                        horStr + " " +
                        hplStr + " " +
                        ENG_ET;
                    
                    System.out.println(" -> " + s);
                }

            }

            // merge strings
            translated = String.join("<br/>", schedList);

        }
        
        return translated;
    } 

    static boolean isWrongFormat(String s)
    {
        boolean wrong = false;

        wrong |= s.contains(ITA_MON);
        wrong |= s.contains(ITA_TUE);
        wrong |= s.contains(ITA_WED);
        wrong |= s.contains(ITA_THU);
        wrong |= s.contains(ITA_FRI);
        wrong |= s.contains(ITA_SAT);
        wrong |= s.contains(ITA_SUN);

        wrong |= s.contains(ITA_JAN);
        wrong |= s.contains(ITA_FEB);
        wrong |= s.contains(ITA_MAR);
        wrong |= s.contains(ITA_APR);
        wrong |= s.contains(ITA_MAY);
        wrong |= s.contains(ITA_JUN);
        wrong |= s.contains(ITA_JUL);
        wrong |= s.contains(ITA_AUG);
        wrong |= s.contains(ITA_SEP);
        wrong |= s.contains(ITA_OCT);
        wrong |= s.contains(ITA_NOV);
        wrong |= s.contains(ITA_DEC);

        wrong |= s.contains(ENG_OC);

        return wrong;
    }
}