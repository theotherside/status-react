#!/usr/bin/env bash
# neither options work, lets go ghetto?
appium &
sleep 3

#!/usr/bin/expect -f
# set timeout 600
# # cd [file dirname $argv0]
# exec cd ..
# spawn -ignore HUP appium
# expect -ex "Appium REST http interface listener started"
# send_user "Appium Initialized\n"
# expect_background
# exit 0