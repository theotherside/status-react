#!/usr/bin/expect -f
set timeout 600
# cd [file dirname $argv0]
exec cd ..
spawn -ignore HUP lein figwheel android ios
expect -ex "Prompt will show when Figwheel connects to your application"
send_user "Figwheel Initialized"
expect_background
exit 0