#!/bin/sh

# clean log files
if [[ -d ./delivery/SASCampaignNavigator/logs ]]
then 
    rm -r ./delivery/SASCampaignNavigator/logs/*
fi

# execute the delivery program
./delivery/SASCampaignNavigator/bin/SASCampaignNavigator
