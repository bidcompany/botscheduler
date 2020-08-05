echo 'Copy the delivery folder in ISP'
xcopy /s/Y %~dp0\delivery E:\tmp\A

echo 'Overwrite specifics setup file for ISP'
xcopy /Y %~dp0\ISP\chromedriver.exe E:\tmp\A\SASCampaignNavigator\lib\chromedriver.exe
xcopy /Y %~dp0\ISP\SASCampaignNavigator.cfg E:\tmp\A\SASCampaignNavigator\bin\SASCampaignNavigator.cfg

echo 'Refresh session files for the new execution in ISP'
if exist E:\tmp\A\SASCampaignNavigator\bin\campaigns.json del /F E:\tmp\A\SASCampaignNavigator\bin\campaigns.json
if exist E:\tmp\A\SASCampaignNavigator\logs\SASCampaignNavigator.log del /F E:\tmp\A\SASCampaignNavigator\logs\SASCampaignNavigator.log
if exist E:\tmp\A\SASCampaignNavigator\report\SASCampaignNavigator.html del /F E:\tmp\A\SASCampaignNavigator\report\SASCampaignNavigator.html