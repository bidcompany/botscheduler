echo 'Copy the delivery folder in ISP'
xcopy /s/Y %~dp0\delivery %HOMEPATH%

echo 'Overwrite specifics setup file for ISP'
xcopy /Y %~dp0\ISP\chromedriver.exe %HOMEPATH%\SASCampaignNavigator\lib\chromedriver.exe
xcopy /Y %~dp0\ISP\SASCampaignNavigator.cfg %HOMEPATH%\SASCampaignNavigator\bin\SASCampaignNavigator.cfg

echo 'Refresh session files for the new execution in ISP'
if exist %HOMEPATH%\SASCampaignNavigator\bin\campaigns.json del /F %HOMEPATH%\SASCampaignNavigator\bin\campaigns.json
if exist %HOMEPATH%\SASCampaignNavigator\logs\SASCampaignNavigator.log del /F %HOMEPATH%\SASCampaignNavigator\logs\SASCampaignNavigator.log
if exist %HOMEPATH%\SASCampaignNavigator\report\SASCampaignNavigator.html del /F %HOMEPATH%\SASCampaignNavigator\report\SASCampaignNavigator.html