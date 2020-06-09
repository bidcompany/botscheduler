#!/bin/sh

# clean log file
if [[ -d ./delivery/SASCampaignNavigator/logs ]]
then 
    rm -r ./delivery/SASCampaignNavigator/logs/*
fi

# clean report file
if [[ -d ./delivery/SASCampaignNavigator/report ]]
then 
    rm -r ./delivery/SASCampaignNavigator/report/*
fi

# execute the delivery program
./delivery/SASCampaignNavigator/bin/SASCampaignNavigator
